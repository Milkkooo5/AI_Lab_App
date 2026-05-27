package com.example.thebestapp2026.data.remote

data class AnalyzeResponse(
    val indicators: List<AnalysisIndicator> = emptyList(),
    val recommendation: String? = "",
    val comparison: String? = ""
)

data class AnalysisIndicator(
    val name: String? = "",
    val value: String? = "",
    val unit: String? = "",
    val status: String? = "норма"
)
