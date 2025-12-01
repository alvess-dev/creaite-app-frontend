package com.example.ocreaite.data.models

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val username: String,
    val name: String,
    val birthDate: String?,
    val language: String
)