package com.example.thebestapp2026.data.repository

import android.content.Context
import android.net.Uri

import com.example.thebestapp2026.domain.Analysis

interface AnalysisRepository {

    suspend fun uploadAnalysis(
        context: Context,
        userId: String,
        fileUri: Uri
    ): Result<Analysis>

    suspend fun getHistory(
        userId: String
    ): List<Analysis>

    suspend fun getLastAnalysis(): Analysis?
}