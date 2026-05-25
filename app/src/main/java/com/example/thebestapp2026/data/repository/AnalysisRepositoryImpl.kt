package com.example.thebestapp2026.data.repository

import android.content.Context
import android.net.Uri
import com.example.thebestapp2026.data.local.TokenStorage
import com.example.thebestapp2026.data.remote.RetrofitClient
import com.example.thebestapp2026.domain.Analysis
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AnalysisRepositoryImpl(
    private val context: Context
) : AnalysisRepository {
    private val api = RetrofitClient.api
    private val tokenStorage = TokenStorage(context)

    override suspend fun uploadAnalysis(
        context: Context,
        userId: String,
        fileUri: Uri
    ): Result<Analysis> {
        return try {
            val token = tokenStorage.getToken()

            val file = uriToFile(context, fileUri)

            val userIdBody = userId.toRequestBody("text/plain".toMediaType())

            val filePart = MultipartBody.Part.createFormData(
                "file",
                file.name,
                file.asRequestBody("application/pdf".toMediaType())
            )

            val response = api.uploadAnalysis(
                token = "Bearer $token",
                userId = userIdBody,
                file = filePart
            )

            val text = response.indicators.joinToString("\n") {
                "${it.name}: ${it.value} ${it.unit}, ${it.status}"
            } + "\n\nРекомендация: ${response.recommendation}"

            Result.success(
                Analysis(
                    userId = userId,
                    fileName = file.name,
                    aiText = text,
                    createdAt = "",
                    status = "done"
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHistory(userId: String): List<Analysis> {
        return emptyList()
    }

    override suspend fun getLastAnalysis(): Analysis? {
        return null
    }

    private fun uriToFile(context: Context, uri: Uri): File {
        val file = File(context.cacheDir, "analysis.pdf")
        context.contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file
    }
}