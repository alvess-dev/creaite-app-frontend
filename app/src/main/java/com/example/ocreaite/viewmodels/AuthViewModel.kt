package com.example.ocreaite.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocreaite.data.local.TokenManager
import com.example.ocreaite.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(context: Context) : ViewModel() {

    private val tokenManager = TokenManager(context)
    private val repository = AuthRepository(tokenManager)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val userName: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            when (val result = repository.login(email, password)) {
                is AuthRepository.AuthResult.Success -> {
                    _authState.value = AuthState.Success(result.response.name)
                }
                is AuthRepository.AuthResult.Error -> {
                    _authState.value = AuthState.Error(result.message)
                }
            }
        }
    }

    fun register(
        email: String,
        password: String,
        username: String,
        name: String,
        birthDate: String?
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            when (val result = repository.register(email, password, username, name, birthDate)) {
                is AuthRepository.AuthResult.Success -> {
                    _authState.value = AuthState.Success(result.response.name)
                }
                is AuthRepository.AuthResult.Error -> {
                    _authState.value = AuthState.Error(result.message)
                }
            }
        }
    }

    fun googleLogin(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            when (val result = repository.googleLogin(idToken)) {
                is AuthRepository.AuthResult.Success -> {
                    _authState.value = AuthState.Success(result.response.name)
                }
                is AuthRepository.AuthResult.Error -> {
                    _authState.value = AuthState.Error(result.message)
                }
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}