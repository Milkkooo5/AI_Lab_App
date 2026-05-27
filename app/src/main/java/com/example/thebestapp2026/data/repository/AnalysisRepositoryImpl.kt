package com.example.thebestapp2026.data.repository

import android.content.Context
import android.net.Uri
import androidx.room.Room
import com.example.thebestapp2026.data.local.AnalysisEntity
import com.example.thebestapp2026.data.local.AppDatabase
import com.example.thebestapp2026.data.local.TokenStorage
import com.example.thebestapp2026.data.remote.RetrofitClient
import com.example.thebestapp2026.data.session.SessionManager
import com.example.thebestapp2026.domain.Analysis
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AnalysisRepositoryImpl(
    private val appContext: Context
) : AnalysisRepository {

    private val api = RetrofitClient.api
    private val tokenStorage = TokenStorage(appContext)

    private val db = Room.databaseBuilder(
        appContext.applicationContext,
        AppDatabase::class.java,
        "app_database"
    ).build()

    private val analysisDao = db.analysisDao()

    override suspend fun uploadAnalysis(
        context: Context,
        userId: String,
        fileUri: Uri
    ): Result<Analysis> {
        return try {
            val token = tokenStorage.getToken()

            if (token.isBlank()) {
                return Result.failure(
                    Exception("Токен не найден. Войдите заново.")
                )
            }

            val file = uriToFile(context, fileUri)
            val currentUser = SessionManager.currentUser
            val normalizedBirthDate = normalizeBirthDate(currentUser?.birthDate ?: "")
            val age = calculateAge(normalizedBirthDate)

            val userIdBody = userId.toRequestBody(
                "text/plain".toMediaType()
            )

            val nameBody = (currentUser?.name ?: "").toRequestBody(
                "text/plain".toMediaType()
            )

            val genderBody = (currentUser?.gender ?: "").toRequestBody(
                "text/plain".toMediaType()
            )

            val birthDateBody = normalizedBirthDate.toRequestBody(
                "text/plain".toMediaType()
            )

            val ageBody = age.toRequestBody(
                "text/plain".toMediaType()
            )

            val previousAnalysis = analysisDao.getHistory(userId).firstOrNull()
            val previousAnalysisText = if (previousAnalysis != null) {
                "${previousAnalysis.fileName}\n${previousAnalysis.resultJson}"
            } else {
                "Предыдущий анализ отсутствует."
            }

            val previousAnalysisBody = previousAnalysisText.toRequestBody(
                "text/plain".toMediaType()
            )

            val previousAnalysesBody = previousAnalysisText.toRequestBody(
                "text/plain".toMediaType()
            )

            val filePart = MultipartBody.Part.createFormData(
                name = "file",
                filename = file.name,
                body = file.asRequestBody(
                    "application/pdf".toMediaType()
                )
            )

            val response = api.uploadAnalysis(
                token = "Bearer $token",
                userId = userIdBody,
                name = nameBody,
                gender = genderBody,
                birthDate = birthDateBody,
                age = ageBody,
                previousAnalysis = previousAnalysisBody,
                previousAnalyses = previousAnalysesBody,
                file = filePart
            )

            val indicatorsText = response.indicators.joinToString("\n") { indicator ->
                val name = indicator.name ?: "Показатель"
                val value = indicator.value ?: ""
                val unit = indicator.unit ?: ""
                val status = indicator.status ?: "норма"

                val valueWithUnit = listOf(value, unit)
                    .filter { part -> part.isNotBlank() }
                    .joinToString(" ")

                "$name: $valueWithUnit, $status"
            }

            val recommendationText = buildPersonalRecommendation(
                recommendation = response.recommendation.orEmpty(),
                name = currentUser?.name ?: "",
                gender = currentUser?.gender ?: "",
                age = age
            )
            val comparisonText = response.comparison
                ?.takeIf { text -> text.isNotBlank() }
                ?: buildComparisonText(
                    previousText = previousAnalysis?.resultJson,
                    currentIndicators = response.indicators
                )

            val aiText = listOf(
                indicatorsText,
                "Рекомендация: $recommendationText",
                comparisonText.takeIf { text -> text.isNotBlank() }
                    ?.let { text -> "Сравнение: $text" }
            )
                .filterNotNull()
                .filter { text -> text.isNotBlank() }
                .joinToString("\n\n")

            val analysis = Analysis(
                userId = userId,
                fileName = file.name,
                aiText = aiText,
                createdAt = currentDate(),
                status = "done"
            )

            analysisDao.saveAnalysis(
                AnalysisEntity(
                    userId = analysis.userId,
                    fileName = analysis.fileName,
                    resultJson = analysis.aiText,
                    createdAt = analysis.createdAt
                )
            )

            SessionManager.lastAnalysis = analysis

            Result.success(analysis)
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Сервер долго отвечает. Попробуйте ещё раз."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHistory(
        userId: String
    ): List<Analysis> {
        return analysisDao.getHistory(userId).map { entity ->
            Analysis(
                id = entity.id,
                userId = entity.userId,
                fileName = entity.fileName,
                aiText = entity.resultJson,
                createdAt = entity.createdAt,
                status = "done"
            )
        }
    }

    override suspend fun getLastAnalysis(): Analysis? {
        val currentUserId = SessionManager.currentUser?.userId ?: return null

        val entity = analysisDao.getLastAnalysis(currentUserId)
            ?: return null

        return Analysis(
            id = entity.id,
            userId = entity.userId,
            fileName = entity.fileName,
            aiText = entity.resultJson,
            createdAt = entity.createdAt,
            status = "done"
        )
    }

    private fun uriToFile(
        context: Context,
        uri: Uri
    ): File {
        val file = File(
            context.cacheDir,
            "analysis_${System.currentTimeMillis()}.pdf"
        )

        context.contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return file
    }

    private fun currentDate(): String {
        return SimpleDateFormat(
            "dd.MM.yyyy HH:mm",
            Locale.getDefault()
        ).format(Date())
    }

    private fun calculateAge(birthDate: String): String {
        return try {
            val normalizedDate = normalizeBirthDate(birthDate)
            val parts = normalizedDate.split(".")
            if (parts.size != 3) return ""

            val day = parts[0].toInt()
            val month = parts[1].toInt() - 1
            val year = parts[2].toInt()

            val birthCalendar = Calendar.getInstance()
            birthCalendar.set(year, month, day)

            val today = Calendar.getInstance()
            var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

            val birthdayThisYear = Calendar.getInstance()
            birthdayThisYear.set(
                today.get(Calendar.YEAR),
                birthCalendar.get(Calendar.MONTH),
                birthCalendar.get(Calendar.DAY_OF_MONTH)
            )

            if (today.before(birthdayThisYear)) {
                age--
            }

            age.toString()
        } catch (e: Exception) {
            ""
        }
    }

    private fun normalizeBirthDate(birthDate: String): String {
        val cleanDate = birthDate.trim()
        val digits = cleanDate.filter { it.isDigit() }

        return when {
            cleanDate.contains(".") -> cleanDate
            cleanDate.contains("-") && digits.length == 8 -> {
                val year = digits.substring(0, 4)
                val month = digits.substring(4, 6)
                val day = digits.substring(6, 8)
                "$day.$month.$year"
            }
            digits.length == 8 -> {
                val day = digits.substring(0, 2)
                val month = digits.substring(2, 4)
                val year = digits.substring(4, 8)
                "$day.$month.$year"
            }
            else -> cleanDate
        }
    }

    private fun buildPersonalRecommendation(
        recommendation: String,
        name: String,
        gender: String,
        age: String
    ): String {
        val personalInfo = listOf(
            name.takeIf { it.isNotBlank() }?.let { "имя: $it" },
            gender.takeIf { it.isNotBlank() }?.let { "пол: $it" },
            age.takeIf { it.isNotBlank() }?.let { "возраст: $it" }
        )
            .filterNotNull()
            .joinToString(", ")

        val baseText = recommendation.ifBlank {
            "Покажите результат врачу при наличии жалоб."
        }

        return if (personalInfo.isBlank()) {
            baseText
        } else {
            "С учетом ваших данных ($personalInfo): $baseText"
        }
    }

    private fun buildComparisonText(
        previousText: String?,
        currentIndicators: List<com.example.thebestapp2026.data.remote.AnalysisIndicator>
    ): String {
        if (previousText.isNullOrBlank()) {
            return "Предыдущий анализ отсутствует."
        }

        val previousIndicators = parseIndicators(previousText)
        if (previousIndicators.isEmpty()) {
            return "Предыдущий анализ найден, но сравнить показатели не удалось."
        }

        val improved = mutableListOf<String>()
        val worsened = mutableListOf<String>()
        val same = mutableListOf<String>()

        currentIndicators.forEach { indicator ->
            val name = indicator.name.orEmpty().trim()
            if (name.isBlank()) return@forEach

            val previous = previousIndicators[name.lowercase()] ?: return@forEach
            val currentStatus = normalizeStatus(indicator.status.orEmpty())

            when {
                previous == "отклонение" && currentStatus == "норма" -> improved.add(name)
                previous == "норма" && currentStatus == "отклонение" -> worsened.add(name)
                previous == currentStatus -> same.add(name)
            }
        }

        val parts = mutableListOf<String>()

        if (improved.isNotEmpty()) {
            parts.add("Улучшились: ${improved.joinToString(", ")}.")
        }

        if (worsened.isNotEmpty()) {
            parts.add("Требуют внимания: ${worsened.joinToString(", ")}.")
        }

        if (same.isNotEmpty()) {
            parts.add("Без заметных изменений: ${same.take(5).joinToString(", ")}.")
        }

        return parts.ifEmpty {
            listOf("Предыдущий анализ найден, но явных изменений по статусам не обнаружено.")
        }.joinToString(" ")
    }

    private fun parseIndicators(text: String): Map<String, String> {
        return text.lines()
            .filter { line ->
                line.contains(":") &&
                        !line.startsWith("Рекомендация", ignoreCase = true) &&
                        !line.startsWith("Сравнение", ignoreCase = true)
            }
            .associate { line ->
                val name = line.substringBefore(":").trim().lowercase()
                val status = line.substringAfter(",", "норма")
                name to normalizeStatus(status)
            }
    }

    private fun normalizeStatus(status: String): String {
        val cleanStatus = status.trim().lowercase()

        return if (
            cleanStatus == "отклонение" ||
            cleanStatus.contains("отклон") ||
            cleanStatus.contains("выше") ||
            cleanStatus.contains("ниже")
        ) {
            "отклонение"
        } else {
            "норма"
        }
    }
}
