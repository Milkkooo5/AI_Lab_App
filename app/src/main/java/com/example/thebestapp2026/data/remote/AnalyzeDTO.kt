package com.example.thebestapp2026.data.remote


import kotlinx.serialization.Serializable

@Serializable
data class AnalyzeResponse(

    val userId: String,

    val fileName: String,

    val extractedText: String
)