#!/usr/bin/env python3
import os
import sys
import zipfile
import subprocess
import shutil

def main():
    root_dir = os.path.dirname(os.path.abspath(__file__))
    base_apk = os.path.join(root_dir, ".build-outputs", "base-app-debug.apk")
    if not os.path.exists(base_apk):
        base_apk = os.path.join(root_dir, ".build-outputs", "app-debug.apk")
    
    target_apk_1 = os.path.join(root_dir, "app", "build", "outputs", "apk", "debug", "app-debug.apk")
    target_apk_2 = os.path.join(root_dir, ".build-outputs", "app-debug.apk")
    temp_apk = os.path.join(root_dir, "temp-unsigned.apk")
    aligned_apk = os.path.join(root_dir, "temp-aligned.apk")
    keystore = os.path.join(root_dir, "debug.keystore")
    zipalign_bin = "/opt/android/sdk/build-tools/36.0.0/zipalign"
    apksigner_bin = "/opt/android/sdk/build-tools/36.0.0/apksigner"
    
    # 1. Sync index.html from app/src/main/assets/web/index.html to root index.html
    html_src = os.path.join(root_dir, "app", "src", "main", "assets", "web", "index.html")
    html_root = os.path.join(root_dir, "index.html")
    if os.path.exists(html_src):
        with open(html_src, "rb") as f:
            src_bytes = f.read()
        with open(html_root, "wb") as f:
            f.write(src_bytes)
        print(f"Synced index.html ({len(src_bytes)} bytes)")
    
    # 2. Build unsigned zip
    assets_dir = os.path.join(root_dir, "app", "src", "main", "assets")
    assets_to_inject = {}
    for r, dirs, files in os.walk(assets_dir):
        for fl in files:
            full_path = os.path.join(r, fl)
            rel_path = os.path.relpath(full_path, assets_dir)
            apk_path = "assets/" + rel_path.replace("\\", "/")
            with open(full_path, "rb") as f:
                assets_to_inject[apk_path] = f.read()
    
    dex_dir = os.path.join(root_dir, ".build-outputs", "dex_overrides")
    dex_to_inject = {}
    if os.path.exists(dex_dir):
        for fl in os.listdir(dex_dir):
            if fl.endswith(".dex"):
                full_path = os.path.join(dex_dir, fl)
                with open(full_path, "rb") as f:
                    dex_to_inject[fl] = f.read()
        print(f"Injecting dex overrides: {list(dex_to_inject.keys())}")
    
    if os.path.exists(temp_apk):
        os.remove(temp_apk)
    if os.path.exists(aligned_apk):
        os.remove(aligned_apk)
        
    with zipfile.ZipFile(base_apk, "r") as zin:
        with zipfile.ZipFile(temp_apk, "w", compression=zipfile.ZIP_DEFLATED) as zout:
            for item in zin.infolist():
                # Strip old signatures
                if item.filename.startswith("META-INF/") and (
                    item.filename.endswith(".SF") or 
                    item.filename.endswith(".RSA") or 
                    item.filename.endswith(".DSA") or 
                    item.filename.endswith(".EC") or 
                    item.filename.endswith(".MF")
                ):
                    continue
                # If asset or dex is being injected, replace it
                if item.filename in assets_to_inject:
                    zout.writestr(item.filename, assets_to_inject.pop(item.filename))
                elif item.filename in dex_to_inject:
                    zout.writestr(item.filename, dex_to_inject.pop(item.filename))
                else:
                    zout.writestr(item, zin.read(item.filename))
            # Any remaining new assets or dex
            for apk_path, data in assets_to_inject.items():
                zout.writestr(apk_path, data)
            for apk_path, data in dex_to_inject.items():
                zout.writestr(apk_path, data)
                
    # 3. Zipalign
    align_res = subprocess.run([zipalign_bin, "-f", "-p", "4", temp_apk, aligned_apk], capture_output=True, text=True)
    if align_res.returncode != 0:
        print(f"zipalign failed: {align_res.stderr}")
        sys.exit(1)
        
    # 4. Apksigner
    sign_res = subprocess.run([
        apksigner_bin, "sign",
        "--ks", keystore,
        "--ks-pass", "pass:android",
        "--ks-key-alias", "androiddebugkey",
        "--key-pass", "pass:android",
        aligned_apk
    ], capture_output=True, text=True)
    if sign_res.returncode != 0:
        print(f"apksigner failed: {sign_res.stderr}")
        sys.exit(1)
        
    # 5. Verify
    verify_res = subprocess.run([apksigner_bin, "verify", "-v", aligned_apk], capture_output=True, text=True)
    if "DOES NOT VERIFY" in verify_res.stdout or "Exception" in verify_res.stderr:
        print(f"Verification failed: {verify_res.stdout}\n{verify_res.stderr}")
        sys.exit(1)
        
    # 6. Copy to targets
    os.makedirs(os.path.dirname(target_apk_1), exist_ok=True)
    shutil.copy2(aligned_apk, target_apk_1)
    shutil.copy2(aligned_apk, target_apk_2)
    
    # Cleanup
    if os.path.exists(temp_apk):
        os.remove(temp_apk)
    if os.path.exists(aligned_apk):
        os.remove(aligned_apk)
        
    print(f"SUCCESS: APK repacked and signed at {target_apk_1} and {target_apk_2} ({os.path.getsize(target_apk_1)} bytes)")

if __name__ == "__main__":
    main()
