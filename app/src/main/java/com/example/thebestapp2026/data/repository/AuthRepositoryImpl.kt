package com.example.thebestapp2026.data.repository


import android.content.Context
import com.example.thebestapp2026.data.local.TokenStorage
import com.example.thebestapp2026.data.remote.LoginRequest
import com.example.thebestapp2026.data.remote.RegisterRequest
import com.example.thebestapp2026.data.remote.RetrofitClient
import com.example.thebestapp2026.domain.User

class AuthRepositoryImpl(
    context: Context
) : AuthRepository {

    private val api = RetrofitClient.api
    private val tokenStorage = TokenStorage(context)

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = api.login(LoginRequest(email, password))
            tokenStorage.saveToken(response.token)

            Result.success(
                User(
                    userId = response.userId,
                    name = response.name,
                    surname = response.surname,
                    email = response.email,
                    birthDate = response.birthDate,
                    city = response.city,
                    gender = response.gender
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(
        name: String,
        surname: String,
        email: String,
        password: String,
        birthDate: String,
        city: String,
        gender: String
    ): Result<User> {
        return try {
            val response = api.register(
                RegisterRequest(name, surname, email, password, birthDate, city, gender)
            )

            tokenStorage.saveToken(response.token)

            Result.success(
                User(
                    userId = response.userId,
                    name = response.name,
                    surname = response.surname,
                    email = response.email,
                    birthDate = response.birthDate,
                    city = response.city,
                    gender = response.gender
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): User? = null

    override suspend fun logout() {
        tokenStorage.clear()
    }
}