package com.example.ocreaite.data.repository

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

class AuthRepository(private val tokenManager: TokenManager) {

    private val TAG = "AuthRepository"
    private val BASE_URL = "http://192.168.68.107:8080"

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    sealed class AuthResult {
        data class Success(val response: AuthResponse) : AuthResult()
        data class Error(val message: String) : AuthResult()
    }

    sealed class EmailCheckResult {
        object Available : EmailCheckResult()
        object Taken : EmailCheckResult()
        data class Error(val message: String) : EmailCheckResult()
    }

    suspend fun checkEmailExists(email: String): EmailCheckResult {
        return try {
            Log.d(TAG, "=== Checking Email ===")
            Log.d(TAG, "URL: $BASE_URL/auth/check-email")
            Log.d(TAG, "Email: $email")

            val response = client.get("$BASE_URL/auth/check-email") {
                parameter("email", email.trim().lowercase())
            }

            Log.d(TAG, "Response status: ${response.status}")

            when (response.status) {
                HttpStatusCode.OK -> {
                    Log.d(TAG, "✅ Email available")
                    EmailCheckResult.Available
                }
                HttpStatusCode.Conflict -> {
                    Log.d(TAG, "❌ Email already taken")
                    EmailCheckResult.Taken
                }
                else -> {
                    Log.e(TAG, "❌ Unexpected status: ${response.status}")
                    EmailCheckResult.Error("Error checking email")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Email check failed: ${e.message}")
            Log.e(TAG, "Stack trace: ${e.stackTraceToString()}")
            EmailCheckResult.Error(e.message ?: "Email check failed")
        }
    }

    suspend fun login(email: String, password: String): AuthResult {
        return try {
            Log.d(TAG, "=== Login Request ===")
            Log.d(TAG, "URL: $BASE_URL/auth/login")
            Log.d(TAG, "Email: $email")

            val response: AuthResponse = client.post("$BASE_URL/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email.trim().lowercase(), password))
            }.body()

            Log.d(TAG, "✅ Login successful")
            Log.d(TAG, "User: ${response.name}")

            tokenManager.saveToken(response.token)
            AuthResult.Success(response)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Login failed: ${e.message}")
            Log.e(TAG, "Stack trace: ${e.stackTraceToString()}")
            AuthResult.Error(e.message ?: "Login failed")
        }
    }

    suspend fun register(
        email: String,
        password: String,
        username: String,
        name: String,
        birthDate: String?
    ): AuthResult {
        return try {
            Log.d(TAG, "=== Register Request ===")
            Log.d(TAG, "URL: $BASE_URL/auth/register")
            Log.d(TAG, "Email: $email, Username: $username")

            val response: AuthResponse = client.post("$BASE_URL/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(
                    email.trim().lowercase(),
                    password,
                    username,
                    name,
                    birthDate
                ))
            }.body()

            Log.d(TAG, "✅ Register successful")

            tokenManager.saveToken(response.token)
            AuthResult.Success(response)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Register failed: ${e.message}")
            AuthResult.Error(e.message ?: "Registration failed")
        }
    }

    suspend fun googleLogin(idToken: String): AuthResult {
        return try {
            Log.d(TAG, "=== Google Login Request ===")
            Log.d(TAG, "URL: $BASE_URL/auth/google")

            val response: AuthResponse = client.post("$BASE_URL/auth/google") {
                contentType(ContentType.Application.Json)
                setBody(GoogleLoginRequest(idToken))
            }.body()

            Log.d(TAG, "✅ Google Login successful")
            Log.d(TAG, "User: ${response.name}")

            tokenManager.saveToken(response.token)
            AuthResult.Success(response)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Google Login failed: ${e.message}")
            Log.e(TAG, "Stack trace: ${e.stackTraceToString()}")
            AuthResult.Error(e.message ?: "Google login failed")
        }
    }
}