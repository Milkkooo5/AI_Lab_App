package com.example.thebestapp2026.data.remote

import android.content.Context
import android.net.Uri

interface ApiService {

    suspend fun register(
        request: RegisterRequest
    ): AuthResponse

    suspend fun login(
        request: LoginRequest
    ): AuthResponse

    suspend fun uploadAnalysis(
        context: Context,
        userId: String,
        fileUri: Uri
    ): AnalyzeResponse
}