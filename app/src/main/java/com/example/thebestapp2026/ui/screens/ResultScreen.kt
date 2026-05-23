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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.thebestapp2026.ui.navigation.BottomBar

@Composable
fun ResultScreen(
    navController: NavController
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFF))
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            item {

                Spacer(modifier = Modifier.height(52.dp))

                Text(
                    text = "Результат",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Расшифровка простым языком",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
            }

            item {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = "Обнаружены отклонения",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF4D3A)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "ИИ обнаружил повышенный уровень лейкоцитов и СОЭ. Это может быть связано с воспалительным процессом.",
                            fontSize = 15.sp,
                            color = Color(0xFF6B7280)
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Box(
                            modifier = Modifier
                                .background(
                                    Color(0xFFFFF1E6),
                                    RoundedCornerShape(50.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {

                            Text(
                                text = "Риск: средний",
                                fontSize = 12.sp,
                                color = Color(0xFFFF8A1F)
                            )
                        }
                    }
                }
            }

            item {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFF1EE)
                    )
                ) {

                    Text(
                        text = "Это не диагноз. Покажите результат врачу.",
                        modifier = Modifier.padding(20.dp),
                        color = Color(0xFFB42318),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            item {

                Text(
                    text = "Показатели",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
            }

            item {

                ResultIndicatorCard(
                    title = "Лейкоциты",
                    value = "10.8 ×10⁹/л",
                    reference = "Реф.: 4.0–9.0"
                )
            }

            item {

                ResultIndicatorCard(
                    title = "СОЭ",
                    value = "18 мм/ч",
                    reference = "Реф.: 2–15"
                )
            }

            item {

                ResultIndicatorCard(
                    title = "Гемоглобин",
                    value = "128 г/л",
                    reference = "Реф.: 120–150"
                )
            }

            item {
                Spacer(modifier = Modifier.height(120.dp))
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

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = reference,
                    color = Color(0xFF9CA3AF),
                    fontSize = 13.sp
                )
            }

            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color(0xFF111827)
            )
        }
    }
}