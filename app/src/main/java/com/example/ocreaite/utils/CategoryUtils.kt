package com.example.ocreaite.utils

object CategoryUtils {

    fun normalizeCategory(category: String?): String {
        if (category == null) return "other"
        return category.trim().lowercase()
    }

    fun desiredCategoriesForRows(
        rows: Int,
        available: Set<String>
    ): List<String> {

        val list = available.toList()

        if (list.isEmpty()) return List(rows) { "other" }

        return List(rows) { index ->
            list[index % list.size]
        }
    }
}
