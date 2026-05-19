package com.example.thebestapp2026.domain.usecase

import com.example.thebestapp2026.data.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        repository.login(email, password)
}