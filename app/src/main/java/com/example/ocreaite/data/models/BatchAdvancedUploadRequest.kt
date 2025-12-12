// app/src/main/java/com/example/ocreaite/data/models/BatchAdvancedUploadRequest.kt
package com.example.ocreaite.data.models

import kotlinx.serialization.Serializable

@Serializable
data class BatchAdvancedUploadRequest(
    val items: List<BatchAdvancedItem>,
    val processWithAI: Boolean
)

@Serializable
data class BatchAdvancedItem(
    val imageBase64: String,
    val name: String,
    val category: String,
    val color: String,
    val brand: String,
    val description: String?
)