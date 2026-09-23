package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.BentoPrimary

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebMenuSimulatorDialog(
    initialTable: String = "Mesa 1",
    customBaseUrl: String? = null,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var currentTable by remember { mutableStateOf(initialTable) }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val tableOptions = listOf("Mesa 1", "Mesa 2", "Mesa 3", "Mesa 4", "Mesa 5", "A Domicilio")
    val rawBaseUrl = customBaseUrl?.trim()?.ifBlank { null } ?: "https://riveraga01-cmd.github.io/Restaurante/"
    val baseWebUrl = if (rawBaseUrl.endsWith("/")) rawBaseUrl else "$rawBaseUrl/"

    fun getWebUrl(table: String): String {
        return if (table.contains("domicilio", ignoreCase = true)) {
            "${baseWebUrl}?tipo=domicilio"
        } else {
            val num = table.filter { it.isDigit() }.ifBlank { "1" }
            "${baseWebUrl}?tipo=mesa&num=$num"
        }
    }

    var rendererCrashed by remember { mutableStateOf(false) }
    var webViewKey by remember { mutableStateOf(0) }
    var useLocalAssetOnly by remember { mutableStateOf(true) }

    fun resolveCurrentUrl(table: String, local: Boolean = useLocalAssetOnly): String {
        return if (local) {
            if (table.contains("domicilio", ignoreCase = true)) {
                "file:///android_asset/web/index.html?tipo=domicilio"
            } else {
                val num = table.filter { it.isDigit() }.ifBlank { "1" }
                "file:///android_asset/web/index.html?tipo=mesa&num=$num"
            }
        } else {
            getWebUrl(table)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            try {
                webViewRef?.stopLoading()
                val parent = webViewRef?.parent as? android.view.ViewGroup
                parent?.removeView(webViewRef)
                webViewRef?.destroy()
            } catch (_: Throwable) {}
            webViewRef = null
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .padding(8.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header Bar
                Surface(
                    color = BentoPrimary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Simulador de Menú Web Público",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                                Text(
                                    text = "Vista cliente responsiva (HTML5/CSS3/JS)",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.8f))
                                )
                            }
                        }

                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                        }
                    }
                }

                // Table Selector Bar
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Simular URL: /menu?mesa=",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))

                            var expanded by remember { mutableStateOf(false) }
                            Box {
                                FilterChip(
                                    selected = true,
                                    onClick = { expanded = true },
                                    label = { Text(currentTable) },
                                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
                                )

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    tableOptions.forEach { t ->
                                        DropdownMenuItem(
                                            text = { Text(t) },
                                            onClick = {
                                                currentTable = t
                                                expanded = false
                                                isLoading = true
                                                webViewRef?.loadUrl(resolveCurrentUrl(t))
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            FilterChip(
                                selected = useLocalAssetOnly,
                                onClick = {
                                    val nextMode = !useLocalAssetOnly
                                    useLocalAssetOnly = nextMode
                                    isLoading = true
                                    webViewRef?.loadUrl(resolveCurrentUrl(currentTable, nextMode))
                                },
                                label = { Text(if (useLocalAssetOnly) "Local" else "Nube") },
                                leadingIcon = {
                                    Icon(
                                        if (useLocalAssetOnly) Icons.Default.FlashOn else Icons.Default.Cloud,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            IconButton(
                                onClick = {
                                    isLoading = true
                                    webViewRef?.reload()
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Recargar")
                            }

                            FilledTonalButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        data = Uri.parse(getWebUrl(currentTable))
                                    }
                                    try {
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        // Fallback to local asset
                                        val fallbackIntent = Intent(Intent.ACTION_VIEW).apply {
                                            data = Uri.parse("file:///android_asset/web/index.html?mesa=${Uri.encode(currentTable)}")
                                        }
                                        try { context.startActivity(fallbackIntent) } catch (_: Exception) {}
                                    }
                                },
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("btn_abrir_navegador")
                            ) {
                                Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Navegador", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }

                // WebView Container (Phone Frame Look)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF8FAFC))
                ) {
                    if (rendererCrashed) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "El renderizador del navegador web se reinició.",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Puedes recargar el simulador o abrirlo directamente en el navegador del sistema.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Button(
                                    onClick = {
                                        rendererCrashed = false
                                        isLoading = true
                                        useLocalAssetOnly = true
                                        webViewKey++
                                    }
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Reintentar (Modo Seguro)")
                                }
                                OutlinedButton(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getWebUrl(currentTable)))
                                        try {
                                            context.startActivity(intent)
                                        } catch (_: Exception) {}
                                    }
                                ) {
                                    Icon(Icons.Default.OpenInBrowser, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Abrir en Navegador")
                                }
                            }
                        }
                    } else {
                        androidx.compose.runtime.key(webViewKey) {
                            AndroidView(
                                factory = { ctx ->
                                    WebView(ctx).apply {
                                        // Use software rendering in emulator to avoid MESA GPU rendernode crashes
                                        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                                        settings.javaScriptEnabled = true
                                        settings.domStorageEnabled = true
                                        settings.loadWithOverviewMode = true
                                        settings.useWideViewPort = true
                                        settings.allowFileAccess = true
                                        settings.setGeolocationEnabled(false)
                                        settings.mediaPlaybackRequiresUserGesture = true
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                            settings.safeBrowsingEnabled = false
                                        }
                                        webChromeClient = WebChromeClient()
                                        webViewClient = object : WebViewClient() {
                                            override fun onPageFinished(view: WebView?, url: String?) {
                                                super.onPageFinished(view, url)
                                                isLoading = false
                                            }

                                            override fun onReceivedError(
                                                view: WebView?,
                                                errorCode: Int,
                                                description: String?,
                                                failingUrl: String?
                                            ) {
                                                // If network is offline, gracefully fall back to local asset index.html
                                                if (failingUrl?.startsWith("http") == true) {
                                                    val localUrl = if (currentTable.contains("domicilio", ignoreCase = true)) {
                                                        "file:///android_asset/web/index.html?tipo=domicilio"
                                                    } else {
                                                        val num = currentTable.filter { it.isDigit() }.ifBlank { "1" }
                                                        "file:///android_asset/web/index.html?tipo=mesa&num=$num"
                                                    }
                                                    view?.loadUrl(localUrl)
                                                }
                                            }

                                            override fun onReceivedHttpError(
                                                view: WebView?,
                                                request: WebResourceRequest?,
                                                errorResponse: android.webkit.WebResourceResponse?
                                            ) {
                                                super.onReceivedHttpError(view, request, errorResponse)
                                                val statusCode = errorResponse?.statusCode ?: 200
                                                // If HTTP 404 (Not Found) or server error occurs on main page, load offline web asset
                                                if (request?.isForMainFrame == true && statusCode >= 400) {
                                                    val localUrl = if (currentTable.contains("domicilio", ignoreCase = true)) {
                                                        "file:///android_asset/web/index.html?tipo=domicilio"
                                                    } else {
                                                        val num = currentTable.filter { it.isDigit() }.ifBlank { "1" }
                                                        "file:///android_asset/web/index.html?tipo=mesa&num=$num"
                                                    }
                                                    view?.post {
                                                        view.loadUrl(localUrl)
                                                    }
                                                }
                                            }

                                            override fun onRenderProcessGone(
                                                view: WebView?,
                                                detail: RenderProcessGoneDetail?
                                            ): Boolean {
                                                // Handled gracefully: detach from parent and notify UI
                                                val parent = view?.parent as? android.view.ViewGroup
                                                parent?.removeView(view)
                                                try {
                                                    view?.stopLoading()
                                                    view?.destroy()
                                                } catch (_: Throwable) {}
                                                webViewRef = null
                                                useLocalAssetOnly = true
                                                rendererCrashed = true
                                                isLoading = false
                                                return true // return true indicates host application handled the crash
                                            }

                                            override fun shouldOverrideUrlLoading(
                                                view: WebView?,
                                                request: WebResourceRequest?
                                            ): Boolean {
                                                val uri = request?.url ?: return false
                                                if (uri.scheme == "https" && uri.host?.contains("wa.me") == true) {
                                                    val waIntent = Intent(Intent.ACTION_VIEW, uri)
                                                    ctx.startActivity(waIntent)
                                                    return true
                                                }
                                                return false
                                            }
                                        }
                                        val initialUrl = resolveCurrentUrl(currentTable)
                                        loadUrl(initialUrl)
                                        webViewRef = this
                                    }
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}
