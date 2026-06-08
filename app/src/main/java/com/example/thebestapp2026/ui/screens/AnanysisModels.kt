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
            val title = it.substringBefore(":")
                .trim()
                .trimStart('-', '•', '*')
                .trim()
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
        .extractSection("Рекомендация")
        ?: "Покажите результат врачу при наличии жалоб."

    val comparison = lines
        .extractSection("Сравнение")
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

    return if (
        cleanStatus == "отклонение" ||
        cleanStatus.contains("отклон") ||
        cleanStatus.contains("выше") ||
        cleanStatus.contains("ниже") ||
        cleanStatus.contains("повыш") ||
        cleanStatus.contains("пониж")
    ) {
        "отклонение"
    } else {
        "норма"
    }
}

private fun List<String>.extractSection(title: String): String? {
    val startIndex = indexOfFirst { line ->
        line.startsWith(title, ignoreCase = true)
    }

    if (startIndex == -1) return null

    val firstLine = this[startIndex].substringAfter(":").trim()
    val nextSectionIndex = drop(startIndex + 1).indexOfFirst { line ->
        line.startsWith("Рекомендация", ignoreCase = true) ||
                line.startsWith("Сравнение", ignoreCase = true)
    }

    val extraLines = if (nextSectionIndex == -1) {
        drop(startIndex + 1)
    } else {
        drop(startIndex + 1).take(nextSectionIndex)
    }

    return listOf(firstLine)
        .plus(extraLines)
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .joinToString("\n")
        .takeIf { it.isNotBlank() }
}
