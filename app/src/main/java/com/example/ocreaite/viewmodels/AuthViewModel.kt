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

    private val _emailValidationState = MutableStateFlow<EmailValidationState>(EmailValidationState.Idle)
    val emailValidationState: StateFlow<EmailValidationState> = _emailValidationState

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val userName: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }

    sealed class EmailValidationState {
        object Idle : EmailValidationState()
        object Loading : EmailValidationState()
        object Valid : EmailValidationState()
        data class Invalid(val message: String) : EmailValidationState()
    }

    fun validateEmail(email: String) {
        viewModelScope.launch {
            _emailValidationState.value = EmailValidationState.Loading

            // Validação básica de formato
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _emailValidationState.value = EmailValidationState.Invalid("Invalid email format")
                return@launch
            }

            // Verifica se o email já existe no backend
            when (val result = repository.checkEmailExists(email)) {
                is AuthRepository.EmailCheckResult.Available -> {
                    _emailValidationState.value = EmailValidationState.Valid
                }
                is AuthRepository.EmailCheckResult.Taken -> {
                    _emailValidationState.value = EmailValidationState.Invalid("Email already registered")
                }
                is AuthRepository.EmailCheckResult.Error -> {
                    _emailValidationState.value = EmailValidationState.Invalid(result.message)
                }
            }
        }
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

    fun resetEmailValidation() {
        _emailValidationState.value = EmailValidationState.Idle
    }
}