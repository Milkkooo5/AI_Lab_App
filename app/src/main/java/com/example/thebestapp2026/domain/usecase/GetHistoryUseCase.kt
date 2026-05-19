package com.example.thebestapp2026.domain.usecase

import com.example.thebestapp2026.data.repository.AnalysisRepository

class GetHistoryUseCase(
    private val repository: AnalysisRepository
) {

    suspend operator fun invoke(
        userId: String
    ) = repository.getHistory(userId)
}