// app/src/main/java/com/example/ocreaite/viewmodels/WardrobeViewModel.kt
package com.example.ocreaite.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocreaite.data.MockData
import com.example.ocreaite.data.models.ClothingItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WardrobeViewModel(private val context: Context) : ViewModel() {

    sealed class ClothesState {
        object Loading : ClothesState()
        data class Success(val items: List<ClothingItem>) : ClothesState()
        data class Error(val message: String) : ClothesState()
    }

    private val _clothesState = MutableStateFlow<ClothesState>(ClothesState.Loading)
    val clothesState: StateFlow<ClothesState> = _clothesState

    // ✅ Carrega roupas mockadas
    fun loadUserClothes(category: String? = null) {
        viewModelScope.launch {
            _clothesState.value = ClothesState.Loading

            // Simula delay de rede
            delay(500)

            try {
                // Pega as roupas mockadas
                val clothes = if (category != null) {
                    MockData.mockClothes.filter { it.category == category }
                } else {
                    MockData.mockClothes
                }

                _clothesState.value = ClothesState.Success(clothes)
            } catch (e: Exception) {
                _clothesState.value = ClothesState.Error("Failed to load clothes")
            }
        }
    }

    // ✅ Carrega outfit de academia pré-definido
    fun loadGymOutfit() {
        viewModelScope.launch {
            _clothesState.value = ClothesState.Loading
            delay(300)
            _clothesState.value = ClothesState.Success(MockData.gymOutfit)
        }
    }

    fun deleteClothing(id: String) {
        // Simulação - não faz nada
        viewModelScope.launch {
            loadUserClothes()
        }
    }
}