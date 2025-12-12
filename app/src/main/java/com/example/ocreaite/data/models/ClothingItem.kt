import kotlinx.serialization.Serializable

// app/src/main/java/com/example/ocreaite/data/models/ClothingItem.kt (Atualizar enum)

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

enum class ProcessingStatus {
    PENDING,
    PROCESSING,
    PROCESSING_AI,
    REMOVING_BACKGROUND,
    COMPLETED,
    FAILED
}