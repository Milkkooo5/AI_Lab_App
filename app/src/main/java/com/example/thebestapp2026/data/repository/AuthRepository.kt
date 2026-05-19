package com.example.thebestapp2026.data.repository

import com.example.thebestapp2026.domain.User

interface AuthRepository {

    suspend fun register(
        name: String,
        surname: String,
        email: String,
        password: String,
        birthDate: String,
        city: String,
        gender: String
    ): Result<User>

    suspend fun login(
        email: String,
        password: String
    ): Result<User>

    suspend fun getCurrentUser(): User?

    suspend fun logout()
}