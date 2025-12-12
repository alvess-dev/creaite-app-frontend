// app/src/main/java/com/example/ocreaite/data/models/ClothingMetadata.kt
package com.example.ocreaite.data.models

import android.net.Uri

data class ClothingMetadata(
    val imageUri: Uri,
    val imageBase64: String,
    var name: String = "New Item",
    var category: String = "SHIRT",
    var color: String = "Unknown",
    var brand: String = "Unknown",
    var description: String = ""
)