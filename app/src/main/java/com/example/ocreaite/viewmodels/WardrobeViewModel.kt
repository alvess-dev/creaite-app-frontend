// app/src/main/java/com/example/ocreaite/viewmodels/WardrobeViewModel.kt
package com.example.ocreaite.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocreaite.data.api.ClothesApiService
import com.example.ocreaite.data.local.TokenManager
import com.example.ocreaite.data.models.ClothingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WardrobeViewModel(private val context: Context) : ViewModel() {

    private val tokenManager = TokenManager(context)
    private val apiService = ClothesApiService(tokenManager)

    sealed class ClothesState {
        object Loading : ClothesState()
        data class Success(val items: List<ClothingItem>) : ClothesState()
        data class Error(val message: String) : ClothesState()
    }

    private val _clothesState = MutableStateFlow<ClothesState>(ClothesState.Loading)
    val clothesState: StateFlow<ClothesState> = _clothesState

    // ✅ Carrega roupas reais da API
    fun loadUserClothes(category: String? = null) {
        viewModelScope.launch {
            _clothesState.value = ClothesState.Loading

            when (val result = apiService.getUserClothes(category)) {
                is ClothesApiService.ClothesResult.ListSuccess -> {
                    _clothesState.value = ClothesState.Success(result.items)
                }
                is ClothesApiService.ClothesResult.Error -> {
                    _clothesState.value = ClothesState.Error(result.message)
                }
                else -> {
                    _clothesState.value = ClothesState.Error("Unexpected error")
                }
            }
        }
    }

    // ✅ Toggle favorito
    fun toggleFavorite(id: String) {
        viewModelScope.launch {
            when (val result = apiService.toggleFavorite(id)) {
                is ClothesApiService.ClothesResult.Success -> {
                    // Atualiza o item na lista local
                    val currentState = _clothesState.value
                    if (currentState is ClothesState.Success) {
                        val updatedItems = currentState.items.map { item ->
                            if (item.id == id) {
                                result.item
                            } else {
                                item
                            }
                        }
                        _clothesState.value = ClothesState.Success(updatedItems)
                    }
                }
                is ClothesApiService.ClothesResult.Error -> {
                    // Pode mostrar toast de erro
                }
                else -> {}
            }
        }
    }

    fun deleteClothing(id: String) {
        viewModelScope.launch {
            when (apiService.deleteClothing(id)) {
                is ClothesApiService.ClothesResult.Success -> {
                    loadUserClothes()
                }
                is ClothesApiService.ClothesResult.Error -> {
                    // Pode mostrar toast de erro
                }
                else -> {}
            }
        }
    }
}