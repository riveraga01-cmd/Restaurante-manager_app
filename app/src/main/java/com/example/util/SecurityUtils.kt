package com.example.util

import java.security.MessageDigest

object SecurityUtils {

    /**
     * Hashes a 4-digit PIN string using SHA-256 with a salt for secure storage in Room DB and Firestore.
     */
    fun hashPin(pin: String): String {
        val trimmed = pin.trim()
        if (trimmed.isBlank()) return ""
        val salted = "RIVERA_POS_SALT_$trimmed"
        val bytes = salted.toByteArray(Charsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    /**
     * Verifies if a raw 4-digit numeric PIN matches the stored encrypted hash or raw string.
     */
    fun verifyPin(rawPin: String, storedPinOrHash: String): Boolean {
        val trimmedRaw = rawPin.trim()
        val trimmedStored = storedPinOrHash.trim()
        if (trimmedStored.isBlank()) return false

        // Check salted SHA-256 hash
        val saltedHash = hashPin(trimmedRaw)
        if (saltedHash == trimmedStored) return true

        // Check simple unsalted SHA-256 hash
        val simpleHash = simpleSha256(trimmedRaw)
        if (simpleHash == trimmedStored) return true

        // Direct string equality fallback (legacy unencrypted)
        return trimmedRaw == trimmedStored
    }

    private fun simpleSha256(pin: String): String {
        val bytes = pin.toByteArray(Charsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    /**
     * Helper to check if a string is already a 64-character SHA-256 hex string.
     */
    fun isEncryptedHash(pin: String): Boolean {
        return pin.length == 64 && pin.all { it.isDigit() || it in 'a'..'f' || it in 'A'..'F' }
    }
}
