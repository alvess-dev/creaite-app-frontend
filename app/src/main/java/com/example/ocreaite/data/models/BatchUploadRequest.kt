// app/src/main/java/com/example/ocreaite/data/models/BatchUploadRequest.kt
package com.example.ocreaite.data.models

import kotlinx.serialization.Serializable

@Serializable
data class BatchUploadRequest(
    val imagesBase64: List<String>,
    val processWithAI: Boolean
)