package com.example.thebestapp2026.data.session

import android.content.Context
import com.example.thebestapp2026.domain.Analysis
import com.example.thebestapp2026.domain.User

object SessionManager {
    var currentUser: User? = null
    var token: String = ""
    var lastAnalysis: Analysis? = null

    fun init(context: Context) {
        val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)

        token = prefs.getString("token", "") ?: ""

        val userId = prefs.getString("userId", "") ?: ""
        if (token.isNotBlank() && userId.isNotBlank()) {
            currentUser = User(
                userId = userId,
                name = prefs.getString("name", "") ?: "",
                surname = prefs.getString("surname", "") ?: "",
                email = prefs.getString("email", "") ?: "",
                birthDate = prefs.getString("birthDate", "") ?: "",
                city = prefs.getString("city", "") ?: "",
                gender = prefs.getString("gender", "") ?: ""
            )
        } else {
            currentUser = null
            token = ""
        }
    }

    fun save(context: Context, user: User, newToken: String = token) {
        if (currentUser?.userId != user.userId) {
            lastAnalysis = null
        }

        currentUser = user
        token = newToken

        context.getSharedPreferences("session", Context.MODE_PRIVATE)
            .edit()
            .putString("token", token)
            .putString("userId", user.userId)
            .putString("name", user.name)
            .putString("surname", user.surname)
            .putString("email", user.email)
            .putString("birthDate", user.birthDate)
            .putString("city", user.city)
            .putString("gender", user.gender)
            .apply()
    }

    fun clear(context: Context) {
        currentUser = null
        token = ""
        lastAnalysis = null

        context.getSharedPreferences("session", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()

        context.getSharedPreferences("auth", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}
