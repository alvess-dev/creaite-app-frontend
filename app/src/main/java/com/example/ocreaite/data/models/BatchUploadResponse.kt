// app/src/main/java/com/example/ocreaite/data/models/BatchUploadResponse.kt
package com.example.ocreaite.data.models

import kotlinx.serialization.Serializable

@Serializable
data class BatchUploadResponse(
    val clothingIds: List<String>,
    val message: String,
    val totalUploaded: Int
)