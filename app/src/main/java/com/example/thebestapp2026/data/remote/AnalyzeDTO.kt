package com.example.thebestapp2026.data.remote

data class AnalyzeResponse(
    val indicators: List<AnalysisIndicator> = emptyList(),
    val recommendation: String? = "",
    val comparison: String? = "",
    val id: Int? = null,
    val userId: String? = null,
    val fileName: String? = null,
    val aiText: String? = null,
    val resultJson: String? = null,
    val createdAt: String? = null,
    val status: String? = null
)

data class AnalysisIndicator(
    val name: String? = "",
    val value: String? = "",
    val unit: String? = "",
    val status: String? = "норма"
)
