package com.example.thebestapp2026.data.repository

import android.content.Context
import androidx.room.Room
import com.example.thebestapp2026.data.local.AppDatabase
import com.example.thebestapp2026.data.local.TokenStorage
import com.example.thebestapp2026.data.local.UserEntity
import com.example.thebestapp2026.data.remote.LoginRequest
import com.example.thebestapp2026.data.remote.RegisterRequest
import com.example.thebestapp2026.data.remote.RetrofitClient
import com.example.thebestapp2026.data.session.SessionManager
import com.example.thebestapp2026.domain.User

class AuthRepositoryImpl(
    private val context: Context
) : AuthRepository {

    private val api = RetrofitClient.api
    private val tokenStorage = TokenStorage(context)

    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "app_database"
    ).build()
    private val userDao = db.userDao()

    override suspend fun login(
        email: String,
        password: String
    ): Result<User> {
        return try {
            val response = api.login(LoginRequest(email, password))

            tokenStorage.saveToken(response.token)
            val savedUser = userDao.getUserById(response.userId)

            val user = User(
                userId = response.userId,
                name = savedUser?.name?.ifBlank { response.name } ?: response.name,
                surname = savedUser?.surname?.ifBlank { response.surname } ?: response.surname,
                email = response.email,
                birthDate = savedUser?.birthDate?.ifBlank { response.birthDate } ?: response.birthDate,
                city = savedUser?.city?.ifBlank { response.city } ?: response.city,
                gender = savedUser?.gender?.ifBlank { response.gender } ?: response.gender
            )
            userDao.saveUser(
                UserEntity(
                    userId = user.userId,
                    name = user.name,
                    surname = user.surname,
                    email = user.email,
                    birthDate = user.birthDate,
                    city = user.city,
                    gender = user.gender
                )
            )

            SessionManager.save(context, user, response.token)

            Result.success(user)
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
                RegisterRequest(
                    name = name,
                    surname = surname,
                    email = email,
                    password = password,
                    birthDate = birthDate,
                    city = city,
                    gender = gender
                )
            )

            tokenStorage.saveToken(response.token)

            val user = User(
                userId = response.userId,
                name = response.name,
                surname = response.surname,
                email = response.email,
                birthDate = response.birthDate.ifBlank { birthDate },
                city = response.city.ifBlank { city },
                gender = response.gender.ifBlank { gender }
            )

            userDao.saveUser(
                UserEntity(
                    userId = user.userId,
                    name = user.name,
                    surname = user.surname,
                    email = user.email,
                    birthDate = user.birthDate,
                    city = user.city,
                    gender = user.gender
                )
            )

            SessionManager.save(context, user, response.token)

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): User? {
        if (tokenStorage.getToken().isBlank() && SessionManager.token.isBlank()) {
            return null
        }

        val cachedUser = SessionManager.currentUser

        if (cachedUser != null) {
            return cachedUser
        }

        val userEntity = userDao.getCurrentUser()
            ?: return null

        val user = User(
            userId = userEntity.userId,
            name = userEntity.name,
            surname = userEntity.surname,
            email = userEntity.email,
            birthDate = userEntity.birthDate,
            city = userEntity.city,
            gender = userEntity.gender
        )

        SessionManager.save(context, user, tokenStorage.getToken())

        return user
    }

    override suspend fun logout() {
        tokenStorage.clear()
        SessionManager.clear(context)
    }
}
