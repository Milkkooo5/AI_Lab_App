package com.example.thebestapp2026.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.thebestapp2026.ui.viewmodel.ResultViewModel

@Composable
fun ResultScreen(
    navController: NavController,
    viewModel: ResultViewModel
) {
    val analysis by viewModel.analysis.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadLastAnalysis()
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFF)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF2F7DFF))
        }
        return
    }

    val ui = analysis?.toUiState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFF))
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Результат",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Text(
                text = "Расшифровка простым языком",
                color = Color(0xFF6B7280),
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (ui == null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 120.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = "У вас пока нет анализов",
                        color = Color(0xFF6B7280),
                        fontSize = 15.sp,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }
        } else {
            item {
                val riskColor = ui.riskColor

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(22.dp)) {
                        Text(
                            text = ui.riskLabel,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = riskColor
                        )

                        Text(
                            text = ui.summary,
                            color = Color(0xFF6B7280),
                            fontSize = 15.sp,
                            modifier = Modifier.padding(top = 12.dp)
                        )

                        Card(
                            modifier = Modifier.padding(top = 16.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = riskColor.copy(alpha = 0.12f)
                            )
                        ) {
                            Text(
                                text = if (riskColor == Color(0xFF22C55E)) "Норма" else "Есть отклонения",
                                color = riskColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Показатели",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            ui.indicators.forEach { indicator ->
                item {
                    ResultIndicatorCard(
                        title = indicator.title,
                        value = indicator.value,
                        reference = indicator.status
                    )
                }
            }

            item {
                Text(
                    text = "Рекомендация ИИ",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                    modifier = Modifier.padding(top = 6.dp)
                )

                RecommendationBlock(
                    recommendation = ui.recommendation,
                    indicators = ui.indicators,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            if (ui.comparison.isNotBlank()) {
                item {
                    Text(
                        text = "Сравнение с прошлыми анализами",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827),
                        modifier = Modifier.padding(top = 6.dp)
                    )

                    ComparisonBlock(
                        comparison = ui.comparison,
                        modifier = Modifier.padding(top = 12.dp, bottom = 120.dp)
                    )
                }
            } else {
                item {
                    Spacer(modifier = Modifier.height(120.dp))
                }
            }
        }
    }
}

@Composable
private fun RecommendationBlock(
    recommendation: String,
    indicators: List<IndicatorUi>,
    modifier: Modifier = Modifier
) {
    val sections = buildRecommendationSections(recommendation, indicators)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        sections.forEach { section ->
            RecommendationMiniCard(section)
        }
    }
}

@Composable
private fun RecommendationMiniCard(section: RecommendationSection) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = section.backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = section.title,
                color = section.titleColor,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = section.text,
                color = Color(0xFF374151),
                fontSize = 15.sp,
                lineHeight = 21.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun ComparisonBlock(
    comparison: String,
    modifier: Modifier = Modifier
) {
    val items = buildComparisonItems(comparison)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items.forEach { item ->
            ComparisonMiniCard(item)
        }
    }
}

@Composable
private fun ComparisonMiniCard(item: ComparisonItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                Text(
                    text = item.title,
                    color = Color(0xFF111827),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    text = item.text,
                    color = Color(0xFF6B7280),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = item.badgeBackground)
            ) {
                Text(
                    text = item.badge,
                    color = item.badgeColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
                )
            }
        }
    }
}

@Composable
fun ResultIndicatorCard(
    title: String,
    value: String,
    reference: String
) {
    val statusColor = if (reference.contains("норм", true)) {
        Color(0xFF22C55E)
    } else {
        Color(0xFFFF8A00)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 14.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF111827)
                )

                Text(
                    text = reference,
                    color = statusColor,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Color(0xFF111827),
                textAlign = TextAlign.End,
                modifier = Modifier.weight(0.55f)
            )
        }
    }
}

private data class RecommendationSection(
    val title: String,
    val text: String,
    val titleColor: Color,
    val backgroundColor: Color
)

private data class ComparisonItem(
    val title: String,
    val text: String,
    val badge: String,
    val badgeColor: Color,
    val badgeBackground: Color
)

private fun buildRecommendationSections(
    recommendation: String,
    indicators: List<IndicatorUi>
): List<RecommendationSection> {
    val sentences = splitToSentences(recommendation)
    val normalIndicators = indicators
        .filter { it.status.equals("норма", ignoreCase = true) }
        .map { it.title }
    val attentionIndicators = indicators
        .filter { !it.status.equals("норма", ignoreCase = true) }
        .map { it.title }

    val summary = sentences.firstOrNull()
        ?: "Анализ обработан. Ниже собраны основные выводы."

    val normalText = if (normalIndicators.isNotEmpty()) {
        "В пределах нормы: ${normalIndicators.take(4).joinToString(", ")}."
    } else {
        sentences.firstOrNull { it.contains("норм", ignoreCase = true) }
            ?: "Показатели нормы будут отображены после распознавания анализа."
    }

    val attentionText = if (attentionIndicators.isNotEmpty()) {
        "Требуют внимания: ${attentionIndicators.joinToString(", ")}."
    } else {
        sentences.firstOrNull {
            it.contains("вним", ignoreCase = true) ||
                    it.contains("отклон", ignoreCase = true) ||
                    it.contains("повыш", ignoreCase = true) ||
                    it.contains("пониж", ignoreCase = true)
        } ?: "Явных отклонений по распознанным показателям не найдено."
    }

    val tipsText = sentences
        .filter {
            it.contains("рекомен", ignoreCase = true) ||
                    it.contains("след", ignoreCase = true) ||
                    it.contains("обрат", ignoreCase = true) ||
                    it.contains("врач", ignoreCase = true) ||
                    it.contains("повтор", ignoreCase = true)
        }
        .takeIf { it.isNotEmpty() }
        ?.joinToString(" ")
        ?: "Сохраняйте результаты и обсудите их со специалистом при жалобах или сомнениях."

    return listOf(
        RecommendationSection(
            title = "Общий вывод",
            text = summary,
            titleColor = Color(0xFF2F7DFF),
            backgroundColor = Color(0xFFEFF6FF)
        ),
        RecommendationSection(
            title = "Что в норме",
            text = normalText,
            titleColor = Color(0xFF22C55E),
            backgroundColor = Color(0xFFEAF8EE)
        ),
        RecommendationSection(
            title = "Что требует внимания",
            text = attentionText,
            titleColor = Color(0xFFFF8A00),
            backgroundColor = Color(0xFFFFF4E8)
        ),
        RecommendationSection(
            title = "Рекомендации",
            text = tipsText,
            titleColor = Color(0xFF7C3AED),
            backgroundColor = Color(0xFFF4F0FF)
        )
    )
}

private fun buildComparisonItems(comparison: String): List<ComparisonItem> {
    val parts = splitToSentences(comparison)

    if (parts.isEmpty()) {
        return emptyList()
    }

    return parts.map { sentence ->
        val title = sentence.substringBefore(":").trim()
        val text = sentence.substringAfter(":", sentence).trim()
        val status = comparisonStatus(sentence)

        ComparisonItem(
            title = title.ifBlank { "Изменение" },
            text = text,
            badge = status.first,
            badgeColor = status.second,
            badgeBackground = status.third
        )
    }
}

private fun comparisonStatus(text: String): Triple<String, Color, Color> {
    return when {
        text.contains("улучш", ignoreCase = true) -> Triple(
            "Улучшение",
            Color(0xFF22C55E),
            Color(0xFFEAF8EE)
        )
        text.contains("вним", ignoreCase = true) ||
                text.contains("ухуд", ignoreCase = true) ||
                text.contains("отклон", ignoreCase = true) -> Triple(
            "Требует внимания",
            Color(0xFFFF8A00),
            Color(0xFFFFF4E8)
        )
        text.contains("отсутств", ignoreCase = true) -> Triple(
            "Нет данных",
            Color(0xFF6B7280),
            Color(0xFFF3F4F6)
        )
        else -> Triple(
            "Без изменений",
            Color(0xFF2F7DFF),
            Color(0xFFEFF6FF)
        )
    }
}

private fun splitToSentences(text: String): List<String> {
    return text
        .replace("\n", ". ")
        .split(".")
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .map { sentence ->
            if (sentence.endsWith("!") || sentence.endsWith("?")) sentence else "$sentence."
        }
}
