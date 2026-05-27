package com.example.thebestapp2026.data.remote

import android.content.Context
import android.net.Uri
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): AuthResponse

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): AuthResponse

    @Multipart
    @POST("analysis/upload")
    suspend fun uploadAnalysis(
        @Header("Authorization")
        token: String,
        @Part("userId") userId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("birthDate") birthDate: RequestBody,
        @Part("age") age: RequestBody,
        @Part("previousAnalysis") previousAnalysis: RequestBody,
        @Part("previousAnalyses") previousAnalyses: RequestBody,
        @Part file: MultipartBody.Part
    ): AnalyzeResponse

    @GET("analysis/history/{userId}")
    suspend fun getHistory(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): List<AnalyzeResponse>
}
