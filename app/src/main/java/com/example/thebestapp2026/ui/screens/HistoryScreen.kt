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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.thebestapp2026.data.session.SessionManager
import com.example.thebestapp2026.ui.viewmodel.HistoryViewModel

@Composable
fun HistoryScreen(
    navController: NavController,
    userId: String,
    viewModel: HistoryViewModel
) {
    val history by viewModel.history.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentUserId = userId.ifBlank { SessionManager.currentUser?.userId ?: "" }

    LaunchedEffect(currentUserId) {
        println("history userId = $currentUserId")
        if (currentUserId.isNotBlank()) {
            viewModel.loadHistory(currentUserId)
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
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "История",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Text(
                text = "Все сохраненные анализы",
                color = Color(0xFF6B7280),
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (isLoading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier.padding(top = 24.dp),
                    color = Color(0xFF2F7DFF)
                )
            }
        }

        if (error.isNotBlank()) {
            item {
                Text(
                    text = error,
                    color = Color(0xFFFF4D3A)
                )
            }
        }

        if (!isLoading && history.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = "История пока пустая",
                        color = Color(0xFF6B7280),
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }
        }

        items(history) { analysis ->
            val ui = analysis.toUiState()
            val isNormal = ui.riskColor == Color(0xFF22C55E)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        SessionManager.lastAnalysis = analysis
                        navController.navigate("result")
                    },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 14.dp)
                        ) {
                            Text(
                                text = analysis.fileName,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = analysis.createdAt,
                                color = Color(0xFF6B7280),
                                fontSize = 13.sp,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }

                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isNormal) Color(0xFFEAF8EE) else Color(0xFFFFF0E5)
                            )
                        ) {
                            Text(
                                text = if (isNormal) "Норма" else "Отклонение",
                                color = if (isNormal) Color(0xFF22C55E) else Color(0xFFFF8A00),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }

                    Text(
                        text = ui.summary,
                        color = Color(0xFF6B7280),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 14.dp)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}
