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
    val isFavorite: Boolean?,
    val processingStatus: ProcessingStatus,
    val processingError: String?,
    val createdAt: String?,
    val updatedAt: String?
)

// ✅ ENUM CORRIGIDO - Sincronizado com o backend
@Serializable
enum class ProcessingStatus {
    PENDING,
    PROCESSING,
    PROCESSING_AI,          // ✅ ADICIONADO
    REMOVING_BACKGROUND,    // ✅ ADICIONADO
    COMPLETED,
    FAILED
}