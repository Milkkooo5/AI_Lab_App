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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.thebestapp2026.ui.viewmodel.ResultViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: ResultViewModel = viewModel()
) {
    val analysis by viewModel.analysis.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadLastAnalysis()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFF))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Главная",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )

        Text(
            text = "Ваше здоровье сегодня",
            fontSize = 15.sp,
            color = Color(0xFF6B7280),
            modifier = Modifier.padding(top = 4.dp)
        )

        if (isLoading) {
            CircularProgressIndicator(
                color = Color(0xFF2F7DFF),
                modifier = Modifier.padding(top = 28.dp)
            )
            return@Column
        }

        val current = analysis
        if (current == null) {
            EmptyState(text = "Пока нет загруженных анализов")
            return@Column
        }

        val ui = current.toUiState()
        val deviations = ui.indicators.count { it.hasDeviation() }
        val normal = ui.indicators.size - deviations

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2F7DFF))
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                Text(
                    text = "Последний анализ",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp
                )

                Text(
                    text = current.displayFileName(),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp)
                )

                Text(
                    text = ui.riskLabel,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }

        HealthSummaryCard(
            total = ui.indicators.size,
            normal = normal,
            deviations = deviations
        )

        Text(
            text = "Показатели",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827),
            modifier = Modifier.padding(top = 30.dp)
        )

        if (ui.indicators.isEmpty()) {
            EmptyState(text = "Показатели не распознаны. Настройте ответ ИИ в структурированном формате.")
        } else {
            ui.indicators
                .groupBy { it.group() }
                .toSortedMap(compareBy { it.ordinal })
                .forEach { (group, indicators) ->
                    Text(
                        text = group.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827),
                        modifier = Modifier.padding(top = 20.dp)
                    )

                    indicators.forEach { indicator ->
                        val hasDeviation = indicator.hasDeviation()
                        AnalysisCard(
                            title = indicator.title,
                            value = indicator.value,
                            status = if (hasDeviation) "отклонение" else "норма",
                            statusColor = if (hasDeviation) Color(0xFFFF9500) else Color(0xFF22C55E)
                        )
                    }
                }
        }

        Text(
            text = "Рекомендация ИИ",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827),
            modifier = Modifier.padding(top = 30.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp, bottom = 120.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF4E8))
        ) {
            Text(
                text = ui.recommendation,
                modifier = Modifier.padding(20.dp),
                color = Color(0xFF9A5B16),
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun HealthSummaryCard(
    total: Int,
    normal: Int,
    deviations: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "Картина здоровья",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Row(
                modifier = Modifier.padding(top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryStat("Всего", total.toString(), Color(0xFF2F7DFF), Modifier.weight(1f))
                SummaryStat("Норма", normal.toString(), Color(0xFF22C55E), Modifier.weight(1f))
                SummaryStat("Откл.", deviations.toString(), Color(0xFFFF9500), Modifier.weight(1f))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFE5E7EB))
            ) {
                val normalWeight = normal.coerceAtLeast(0).toFloat()
                val deviationWeight = deviations.coerceAtLeast(0).toFloat()
                if (normalWeight > 0f) {
                    Box(
                        modifier = Modifier
                            .weight(normalWeight)
                            .height(12.dp)
                            .background(Color(0xFF22C55E))
                    )
                }
                if (deviationWeight > 0f) {
                    Box(
                        modifier = Modifier
                            .weight(deviationWeight)
                            .height(12.dp)
                            .background(Color(0xFFFF9500))
                    )
                }
                if (normalWeight == 0f && deviationWeight == 0f) {
                    Box(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
private fun SummaryStat(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Text(text = title, fontSize = 12.sp, color = Color(0xFF6B7280))
        Text(
            text = value,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun AnalysisCard(
    title: String,
    value: String,
    status: String,
    statusColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827),
                lineHeight = 22.sp
            )

            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827),
                lineHeight = 26.sp,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Start
            )

            Box(
                modifier = Modifier
                    .padding(top = 14.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(statusColor.copy(alpha = 0.15f))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = status,
                    color = statusColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}
