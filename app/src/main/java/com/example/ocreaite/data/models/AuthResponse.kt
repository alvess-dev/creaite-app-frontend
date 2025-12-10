package com.example.ocreaite.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val name: String,
    val token: String,
    val hasCompletedOnboarding: Boolean  // ✅ NOVO
)