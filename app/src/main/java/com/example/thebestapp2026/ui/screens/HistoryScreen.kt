package com.example.thebestapp2026.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class HistoryItem(
    val title: String,
    val date: String,
    val status: String,
    val statusColor: Color
)

@Composable
fun HistoryScreen() {

    val historyList = listOf(

        HistoryItem(
            title = "Анализ крови #1042",
            date = "Загружено 12 мая 2026",
            status = "Отклонения",
            statusColor = Color(0xFFFF9500)
        ),

        HistoryItem(
            title = "Анализ крови #1043",
            date = "Загружено 13 мая 2026",
            status = "Норма",
            statusColor = Color(0xFF22C55E)
        ),

        HistoryItem(
            title = "Анализ крови #1044",
            date = "Загружено 15 мая 2026",
            status = "Отклонения",
            statusColor = Color(0xFFFF9500)
        ),

        HistoryItem(
            title = "Анализ крови #1045",
            date = "Загружено 18 мая 2026",
            status = "Норма",
            statusColor = Color(0xFF22C55E)
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFF)),

        contentPadding = PaddingValues(
            start = 24.dp,
            end = 24.dp,
            top = 60.dp,
            bottom = 120.dp
        ),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {

            Text(
                text = "История",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
        }

        item {

            Text(
                text = "Все сохраненные запросы",
                fontSize = 15.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        item {

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                FilterChip(
                    text = "Все",
                    background = Color(0xFFE8F0FF),
                    textColor = Color(0xFF2F7DFF)
                )

                FilterChip(
                    text = "Отклонения",
                    background = Color(0xFFFFF4E8),
                    textColor = Color(0xFFFF9500)
                )

                FilterChip(
                    text = "Норма",
                    background = Color(0xFFEFFAF2),
                    textColor = Color(0xFF22C55E)
                )
            }
        }

        items(historyList) { item ->

            HistoryCard(
                title = item.title,
                date = item.date,
                status = item.status,
                statusColor = item.statusColor
            )
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    background: Color,
    textColor: Color
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(background)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        Text(
            text = text,
            color = textColor,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp
        )
    }
}

@Composable
fun HistoryCard(
    title: String,
    date: String,
    status: String,
    statusColor: Color
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),

            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {

                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Text(
                    text = date,
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(statusColor.copy(alpha = 0.15f))
                    .padding(horizontal = 14.dp, vertical = 7.dp)
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