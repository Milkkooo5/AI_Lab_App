package com.example.thebestapp2026.domain.usecase

import com.example.thebestapp2026.data.repository.AuthRepository

class RegisterUseCase(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(
        name: String,
        surname: String,
        email: String,
        password: String,
        birthDate: String,
        city: String,
        gender: String
    ) = repository.register(
        name,
        surname,
        email,
        password,
        birthDate,
        city,
        gender
    )
}