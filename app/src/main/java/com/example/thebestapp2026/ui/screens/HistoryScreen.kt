package com.example.thebestapp2026.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.thebestapp2026.data.session.SessionManager
import com.example.thebestapp2026.ui.viewmodel.HistoryViewModel

private enum class HistoryFilter(val title: String) {
    All("Все"),
    Deviation("С отклонениями"),
    Normal("Норма")
}

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = viewModel()
) {
    val history by viewModel.history.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var filter by remember { mutableStateOf(HistoryFilter.All) }

    LaunchedEffect(Unit) {
        viewModel.loadHistory()
    }

    val filteredHistory = history.filter { analysis ->
        val hasDeviation = analysis.toUiState().indicators.any { it.hasDeviation() } || analysis.toUiState().riskLabel == "Есть отклонения"
        when (filter) {
            HistoryFilter.All -> true
            HistoryFilter.Deviation -> hasDeviation
            HistoryFilter.Normal -> !hasDeviation
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFF))
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(52.dp))

            Text(
                text = "История",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Все сохранённые анализы",
                fontSize = 14.sp,
                color = Color(0xFF6B7280)
            )

            Row(
                modifier = Modifier.padding(top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HistoryFilter.entries.forEach { item ->
                    FilterChip(
                        selected = filter == item,
                        onClick = { filter = item },
                        label = { Text(item.title) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
        }

        if (isLoading) {
            item { CircularProgressIndicator(color = Color(0xFF2F7DFF)) }
        } else if (error.isNotBlank()) {
            item { EmptyState(text = error) }
        } else if (filteredHistory.isEmpty()) {
            item { EmptyState(text = "Данных пока нет") }
        } else {
            items(filteredHistory) { item ->
                val ui = item.toUiState()
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            SessionManager.lastAnalysis = item
                            SessionManager.save()
                            navController.navigate("result")
                        },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = item.displayFileName(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = item.createdAt.ifBlank { "Дата не указана" },
                            fontSize = 14.sp,
                            color = Color(0xFF6B7280)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = ui.riskLabel,
                                fontWeight = FontWeight.Medium,
                                color = ui.riskColor
                            )

                            TextButton(onClick = { viewModel.deleteAnalysis(item) }) {
                                Text("Удалить", color = Color(0xFFB42318))
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}
