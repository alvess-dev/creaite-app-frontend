// app/src/main/java/com/example/ocreaite/viewmodels/WardrobeViewModel.kt
package com.example.ocreaite.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocreaite.data.api.ClothesApiService
import com.example.ocreaite.data.local.TokenManager
import com.example.ocreaite.data.models.ClothingItem
import com.example.ocreaite.data.models.ProcessingStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WardrobeViewModel(context: Context) : ViewModel() {

    private val TAG = "WardrobeViewModel"
    private val tokenManager = TokenManager(context)
    private val apiService = ClothesApiService(tokenManager)

    private val _clothesState = MutableStateFlow<ClothesState>(ClothesState.Idle)
    val clothesState: StateFlow<ClothesState> = _clothesState

    sealed class ClothesState {
        object Idle : ClothesState()
        object Loading : ClothesState()
        data class Success(val items: List<ClothingItem>) : ClothesState()
        data class Error(val message: String) : ClothesState()
    }

    fun loadUserClothes(category: String? = null) {
        viewModelScope.launch {
            Log.d(TAG, "=== Loading User Clothes ===")
            _clothesState.value = ClothesState.Loading

            when (val result = apiService.getUserClothes(category)) {
                is ClothesApiService.ClothesResult.ListSuccess -> {
                    Log.d(TAG, "✅ Loaded ${result.items.size} items")
                    _clothesState.value = ClothesState.Success(result.items)

                    // Inicia polling para itens em processamento
                    startPollingForProcessingItems(result.items)
                }
                is ClothesApiService.ClothesResult.Error -> {
                    Log.e(TAG, "❌ Load failed: ${result.message}")
                    _clothesState.value = ClothesState.Error(result.message)
                }
                else -> {
                    Log.e(TAG, "❌ Unexpected result type")
                    _clothesState.value = ClothesState.Error("Unexpected error")
                }
            }
        }
    }

    private fun startPollingForProcessingItems(items: List<ClothingItem>) {
        val processingItems = items.filter {
            it.processingStatus == ProcessingStatus.PENDING ||
                    it.processingStatus == ProcessingStatus.PROCESSING
        }

        if (processingItems.isEmpty()) {
            Log.d(TAG, "No items in processing")
            return
        }

        Log.d(TAG, "Starting polling for ${processingItems.size} processing items")

        viewModelScope.launch {
            while (true) {
                delay(5000) // Verifica a cada 5 segundos

                var hasUpdates = false

                processingItems.forEach { item ->
                    when (val result = apiService.getClothingStatus(item.id)) {
                        is ClothesApiService.ClothesResult.Success -> {
                            val updated = result.item

                            if (updated.processingStatus == ProcessingStatus.COMPLETED ||
                                updated.processingStatus == ProcessingStatus.FAILED) {

                                Log.d(TAG, "✅ Item ${item.id} finished processing")
                                hasUpdates = true
                            }
                        }
                        else -> {
                            Log.e(TAG, "Failed to get status for ${item.id}")
                        }
                    }
                }

                // Se houve atualizações, recarrega a lista
                if (hasUpdates) {
                    Log.d(TAG, "Reloading clothes due to processing updates")
                    loadUserClothes()
                    break // Para o polling
                }
            }
        }
    }

    fun deleteClothing(id: String) {
        viewModelScope.launch {
            Log.d(TAG, "=== Deleting Clothing ===")
            Log.d(TAG, "ID: $id")

            when (val result = apiService.deleteClothing(id)) {
                is ClothesApiService.ClothesResult.Success -> {
                    Log.d(TAG, "✅ Deleted successfully")
                    loadUserClothes() // Recarrega a lista
                }
                is ClothesApiService.ClothesResult.Error -> {
                    Log.e(TAG, "❌ Delete failed: ${result.message}")
                    _clothesState.value = ClothesState.Error(result.message)
                }
                else -> {
                    Log.e(TAG, "❌ Unexpected result type")
                }
            }
        }
    }

    fun resetState() {
        _clothesState.value = ClothesState.Idle
    }
}