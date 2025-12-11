// app/src/main/java/com/example/ocreaite/data/models/ClothingItem.kt
package com.example.ocreaite.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ClothingItem(
    val id: String,
    val name: String?,
    val category: String?,
    val color: String?,
    val brand: String?,
    val clothingPictureUrl: String,
    val originalImageUrl: String?,
    val description: String?,
    val isPublic: Boolean?,
    val isFavorite: Boolean?,  // ✅ NOVO
    val processingStatus: ProcessingStatus,
    val processingError: String?,
    val createdAt: String?,
    val updatedAt: String?
)

enum class ProcessingStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED
}