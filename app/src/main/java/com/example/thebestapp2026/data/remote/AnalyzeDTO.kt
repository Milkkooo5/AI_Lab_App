package com.example.thebestapp2026.data.remote


import kotlinx.serialization.Serializable

@Serializable
data class AnalyzeResponse(
    val indicators: List<AnalysisIndicator>,
    val recommendation: String
)

@Serializable
data class AnalysisIndicator(
    val name: String,
    val value: String,
    val unit: String = "",
    val status: String
)