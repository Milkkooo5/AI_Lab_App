package com.example.thebestapp2026.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.thebestapp2026.ui.ViewModel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    val analysis by viewModel.analysis.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadLastAnalysis()
    }

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
                text = "Главная",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Text(
                text = "Ваше здоровье сегодня",
                color = Color(0xFF6B7280),
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (isLoading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier.padding(top = 20.dp),
                    color = Color(0xFF2F7DFF)
                )
            }
        } else if (analysis == null) {
            item {
                EmptyWhiteCard(text = "У вас пока нет анализов")
            }
        } else {
            val currentAnalysis = analysis!!
            val ui = currentAnalysis.toUiState()

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2F7DFF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(22.dp)) {
                        Text(
                            text = "Последний анализ",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 14.sp
                        )

                        Text(
                            text = currentAnalysis.fileName,
                            color = Color.White,
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Text(
                            text = ui.riskLabel,
                            color = Color.White,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Показатели",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            ui.indicators.take(4).forEach { indicator ->
                item {
                    SmallIndicatorCard(
                        title = indicator.title,
                        value = indicator.value,
                        status = indicator.status
                    )
                }
            }

            item {
                Text(
                    text = "Рекомендация ИИ",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                    modifier = Modifier.padding(top = 6.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 120.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF4E8)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = ui.recommendation,
                        modifier = Modifier.padding(20.dp),
                        color = Color(0xFF9A5B16),
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SmallIndicatorCard(
    title: String,
    value: String,
    status: String
) {
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
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Text(
                    text = status,
                    color = if (status.contains("норм", true)) Color(0xFF22C55E) else Color(0xFFFF8A00),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 5.dp)
                )
            }

            Text(
                text = value,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(0.45f)
            )
        }
    }
}

@Composable
private fun EmptyWhiteCard(text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = text,
            color = Color(0xFF6B7280),
            fontSize = 15.sp,
            modifier = Modifier.padding(20.dp)
        )
    }
}
