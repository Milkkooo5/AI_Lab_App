package com.example.thebestapp2026.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFF))
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

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2F7DFF)
            )
        ) {

            Column(
                modifier = Modifier.padding(22.dp)
            ) {

                Text(
                    text = "Последний анализ",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp
                )

                Text(
                    text = "Общий анализ крови",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp)
                )

                Text(
                    text = "Есть 2 показателя выше нормы",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }

        Text(
            text = "Показатели",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827),
            modifier = Modifier.padding(top = 30.dp)
        )

        AnalysisCard(
            title = "Гемоглобин",
            value = "128 г/л",
            status = "норма",
            statusColor = Color(0xFF22C55E)
        )

        AnalysisCard(
            title = "Лейкоциты",
            value = "10.8",
            status = "выше",
            statusColor = Color(0xFFFF9500)
        )

        AnalysisCard(
            title = "Глюкоза",
            value = "5.1",
            status = "норма",
            statusColor = Color(0xFF22C55E)
        )

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
                .padding(top = 18.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF4E8)
            )
        ) {

            Text(
                text = "Повторить анализ через 7–10 дней и обратиться к терапевту при симптомах.",
                modifier = Modifier.padding(20.dp),
                color = Color(0xFF9A5B16),
                lineHeight = 22.sp
            )
        }
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
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Text(
                    text = value,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
            }

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