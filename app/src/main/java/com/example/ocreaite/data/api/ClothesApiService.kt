// app/src/main/java/com/example/ocreaite/data/api/ClothesApiService.kt
package com.example.ocreaite.data.api

import android.util.Log
import com.example.ocreaite.data.local.TokenManager
import com.example.ocreaite.data.models.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ClothesApiService(private val tokenManager: TokenManager) {

    private val TAG = "ClothesApiService"
    private val BASE_URL = "http://192.168.68.107:8080" // 🔴 ALTERE PARA SEU IP

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
            })
        }

        engine {
            requestTimeout = 60_000 // 60 segundos para uploads
        }
    }

    sealed class ClothesResult {
        data class Success(val item: ClothingItem) : ClothesResult()
        data class BatchSuccess(val response: BatchUploadResponse) : ClothesResult()
        data class ListSuccess(val items: List<ClothingItem>) : ClothesResult()
        data class Error(val message: String) : ClothesResult()
    }

    suspend fun uploadClothing(imageBase64: String, processWithAI: Boolean): ClothesResult {
        return try {
            Log.d(TAG, "=== Upload Clothing ===")
            Log.d(TAG, "Process with AI: $processWithAI")

            val token = tokenManager.getAccessToken()
            if (token == null) {
                Log.e(TAG, "No token available")
                return ClothesResult.Error("No authentication token")
            }

            val response: ClothingItem = client.post("$BASE_URL/clothes/upload") {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                setBody(ClothesUploadRequest(imageBase64, processWithAI))
            }.body()

            Log.d(TAG, "✅ Upload successful - ID: ${response.id}")
            ClothesResult.Success(response)

        } catch (e: Exception) {
            Log.e(TAG, "❌ Upload failed: ${e.message}", e)
            ClothesResult.Error(e.message ?: "Upload failed")
        }
    }

    suspend fun uploadBatch(imagesBase64: List<String>, processWithAI: Boolean): ClothesResult {
        return try {
            Log.d(TAG, "=== Batch Upload ===")
            Log.d(TAG, "Number of images: ${imagesBase64.size}")
            Log.d(TAG, "Process with AI: $processWithAI")

            val token = tokenManager.getAccessToken()
            if (token == null) {
                Log.e(TAG, "No token available")
                return ClothesResult.Error("No authentication token")
            }

            val response: BatchUploadResponse = client.post("$BASE_URL/clothes/upload/batch") {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                setBody(BatchUploadRequest(imagesBase64, processWithAI))
            }.body()

            Log.d(TAG, "✅ Batch upload successful - ${response.totalUploaded} items")
            ClothesResult.BatchSuccess(response)

        } catch (e: Exception) {
            Log.e(TAG, "❌ Batch upload failed: ${e.message}", e)
            ClothesResult.Error(e.message ?: "Batch upload failed")
        }
    }

    suspend fun getClothingStatus(id: String): ClothesResult {
        return try {
            Log.d(TAG, "=== Get Clothing Status ===")
            Log.d(TAG, "ID: $id")

            val token = tokenManager.getAccessToken()
            if (token == null) {
                Log.e(TAG, "No token available")
                return ClothesResult.Error("No authentication token")
            }

            val response: ClothingItem = client.get("$BASE_URL/clothes/status/$id") {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
            }.body()

            Log.d(TAG, "✅ Status retrieved - Status: ${response.processingStatus}")
            ClothesResult.Success(response)

        } catch (e: Exception) {
            Log.e(TAG, "❌ Get status failed: ${e.message}", e)
            ClothesResult.Error(e.message ?: "Failed to get status")
        }
    }

    suspend fun getUserClothes(category: String? = null): ClothesResult {
        return try {
            Log.d(TAG, "=== Get User Clothes ===")
            Log.d(TAG, "Category filter: $category")

            val token = tokenManager.getAccessToken()
            if (token == null) {
                Log.e(TAG, "No token available")
                return ClothesResult.Error("No authentication token")
            }

            val response: List<ClothingItem> = client.get("$BASE_URL/user/clothes") {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                if (category != null) {
                    parameter("category", category)
                }
            }.body()

            Log.d(TAG, "✅ Retrieved ${response.size} items")
            ClothesResult.ListSuccess(response)

        } catch (e: Exception) {
            Log.e(TAG, "❌ Get clothes failed: ${e.message}", e)
            ClothesResult.Error(e.message ?: "Failed to get clothes")
        }
    }

    suspend fun deleteClothing(id: String): ClothesResult {
        return try {
            Log.d(TAG, "=== Delete Clothing ===")
            Log.d(TAG, "ID: $id")

            val token = tokenManager.getAccessToken()
            if (token == null) {
                Log.e(TAG, "No token available")
                return ClothesResult.Error("No authentication token")
            }

            client.delete("$BASE_URL/clothes/$id") {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
            }

            Log.d(TAG, "✅ Clothing deleted successfully")
            ClothesResult.Success(ClothingItem(
                id = id,
                name = null,
                category = null,
                color = null,
                brand = null,
                clothingPictureUrl = "",
                originalImageUrl = null,
                description = null,
                isPublic = null,
                processingStatus = ProcessingStatus.COMPLETED,
                processingError = null,
                createdAt = null,
                updatedAt = null
            ))

        } catch (e: Exception) {
            Log.e(TAG, "❌ Delete failed: ${e.message}", e)
            ClothesResult.Error(e.message ?: "Delete failed")
        }
    }
}