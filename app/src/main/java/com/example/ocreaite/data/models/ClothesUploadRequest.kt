// app/src/main/java/com/example/ocreaite/data/models/ClothesUploadRequest.kt
package com.example.ocreaite.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ClothesUploadRequest(
    val imageBase64: String,
    val processWithAI: Boolean
)