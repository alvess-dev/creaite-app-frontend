package com.example.ocreaite.utils

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

/**
 * Utilitários para imagens Base64 / URLs
 */
@Composable
fun rememberBase64Bitmap(base64String: String): ImageBitmap? {
    return remember(base64String) {
        try {
            // Remove o prefixo "data:image/...;base64," se existir
            val cleanBase64 = if (base64String.contains(",")) {
                base64String.split(",")[1]
            } else {
                base64String
            }

            val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            bitmap?.asImageBitmap()
        } catch (e: Exception) {
            android.util.Log.e("ImageUtils", "Failed to decode Base64 image", e)
            null
        }
    }
}

/**
 * Verifica se uma URL é uma imagem Base64 (data URI)
 */
fun isBase64Image(url: String?): Boolean {
    return url?.startsWith("data:image") == true
}

/**
 * Normaliza a URL da imagem:
 * - Se já é data URI (data:image/...), retorna como está.
 * - Se é uma URL http(s) => retorna como está.
 * - Caso contrário, assume-se que é base64 cru e adiciona o prefixo data:image/jpeg;base64,
 */
fun normalizeImageUrl(raw: String?): String? {
    if (raw.isNullOrBlank()) return null
    val trimmed = raw.trim()
    if (trimmed.startsWith("data:", ignoreCase = true)) return trimmed
    if (trimmed.startsWith("http://", ignoreCase = true) ||
        trimmed.startsWith("https://", ignoreCase = true)) return trimmed

    // aparentemente é base64 sem prefixo
    return "data:image/jpeg;base64,$trimmed"
}
