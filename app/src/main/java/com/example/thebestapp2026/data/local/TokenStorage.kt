package com.example.thebestapp2026.data.local



import android.content.Context
class TokenStorage(context: Context) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    fun saveToken(token: String) {
        prefs.edit().putString("token", token).apply()
    }
    fun getToken(): String {
        return prefs.getString("token", "") ?: ""
    }
    fun clear() {
        prefs.edit().clear().apply()
    }
}