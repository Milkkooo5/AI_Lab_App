package com.example.thebestapp2026.domain.usecase


import android.content.Context
import android.net.Uri

import com.example.thebestapp2026.data.repository.AnalysisRepository

class UploadAnalysisUseCase(
    private val repository: AnalysisRepository
) {

    suspend operator fun invoke(
        context: Context,
        userId: String,
        fileUri: Uri
    ) = repository.uploadAnalysis(
        context,
        userId,
        fileUri
    )
}