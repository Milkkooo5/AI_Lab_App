package com.example.thebestapp2026.ui.screens

import androidx.compose.ui.graphics.Color
import com.example.thebestapp2026.domain.Analysis

data class AnalysisUiState(
    val riskLabel: String,
    val riskColor: Color,
    val summary: String,
    val recommendation: String,
    val comparison: String,
    val indicators: List<IndicatorUi>
)

data class IndicatorUi(
    val title: String,
    val value: String,
    val status: String
)

fun Analysis.toUiState(): AnalysisUiState {
    val lines = aiText.lines().filter { it.isNotBlank() }

    val indicators = lines
        .filter {
            it.contains(":") &&
                    !it.startsWith("Рекомендация", ignoreCase = true) &&
                    !it.startsWith("Сравнение", ignoreCase = true)
        }
        .map {
            val title = it.substringBefore(":").trim()
            val rest = it.substringAfter(":").trim()
            val value = rest.substringBefore(",").trim()
            val status = normalizeStatus(rest.substringAfter(",", "норма"))

            IndicatorUi(
                title = title,
                value = value,
                status = status
            )
        }

    val recommendation = lines
        .firstOrNull { it.startsWith("Рекомендация", ignoreCase = true) }
        ?.substringAfter(":")
        ?.trim()
        ?: "Покажите результат врачу при наличии жалоб."

    val comparison = lines
        .firstOrNull { it.startsWith("Сравнение", ignoreCase = true) }
        ?.substringAfter(":")
        ?.trim()
        ?: ""

    val hasRisk = indicators.any {
        it.status.equals("отклонение", ignoreCase = true)
    }

    return AnalysisUiState(
        riskLabel = if (hasRisk) "Обнаружены отклонения" else "Без явных отклонений",
        riskColor = if (hasRisk) Color(0xFFFF4D3A) else Color(0xFF22C55E),
        summary = if (hasRisk) {
            "ИИ обнаружил возможные отклонения в анализе."
        } else {
            "Явных отклонений по анализу не найдено."
        },
        recommendation = recommendation,
        comparison = comparison,
        indicators = indicators
    )
}

private fun normalizeStatus(status: String): String {
    val cleanStatus = status.trim().lowercase()

    return if (cleanStatus == "отклонение") {
        "отклонение"
    } else {
        "норма"
    }
}
