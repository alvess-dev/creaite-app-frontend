package com.example.ocreaite.data.models

import kotlinx.serialization.Serializable

@Serializable
data class GoogleLoginRequest(
    val idToken: String
)