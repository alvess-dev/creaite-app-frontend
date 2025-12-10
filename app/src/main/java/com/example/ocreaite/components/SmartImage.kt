// app/src/main/java/com/example/ocreaite/components/SmartImage.kt
package com.example.ocreaite.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ocreaite.utils.isBase64Image
import com.example.ocreaite.utils.rememberBase64Bitmap

/**
 * Componente inteligente que carrega imagens de qualquer fonte:
 * - URLs HTTP/HTTPS (via Coil)
 * - Imagens Base64 (via decodificação manual)
 */
@Composable
fun SmartImage(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    if (imageUrl == null) {
        // Imagem vazia
        return
    }

    if (isBase64Image(imageUrl)) {
        // É uma imagem Base64
        val bitmap = rememberBase64Bitmap(imageUrl)

        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = modifier
            )
        }
    } else {
        // É uma URL normal - usa Coil
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    }
}