package com.example.thebestapp2026.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

@Composable
fun UploadScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFF))
            .padding(horizontal = 24.dp)
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Загрузка",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )

        Text(
            text = "Добавьте PDF или фото анализов",
            fontSize = 15.sp,
            color = Color(0xFF6B7280),
            modifier = Modifier.padding(top = 4.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .padding(top = 28.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Перетащите файл сюда",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "PDF, JPG, PNG до 20 MB",
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }
        }

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(58.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2F7DFF)
            )
        ) {

            Text(
                text = "Загрузить файл",
                fontSize = 17.sp
            )
        }
    }
}