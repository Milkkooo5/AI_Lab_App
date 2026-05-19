package com.example.thebestapp2026.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(

    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val birthDate: String,
    val city: String,
    val gender: String
)

@Serializable
data class LoginRequest(

    val email: String,
    val password: String
)

@Serializable
data class AuthResponse(

    val userId: String,
    val name: String,
    val surname: String,
    val email: String,
    val birthDate: String,
    val city: String,
    val gender: String
)