package com.example.thebestapp2026.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun RegisterScreen(
    onRegisterClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.Center)
        ) {
            Text(
                text = "Регистрация",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    AuthField(
                        label = "Имя",
                        value = "Milana"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AuthField(
                        label = "Email",
                        value = "milana@example.com"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AuthField(
                        label = "Пароль",
                        value = "••••••••"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AuthField(
                        label = "Повторите пароль",
                        value = "••••••••"
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onRegisterClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2F7DF6)
                        )
                    ) {
                        Text(
                            text = "Создать аккаунт",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = onLoginClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Уже есть аккаунт? Войти",
                    color = Color(0xFF111827),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFEFFAFF)
                )
            ) {
                Text(
                    text = "Ваши медицинские данные будут защищены\nи доступны только в личном кабинете.",
                    modifier = Modifier.padding(18.dp),
                    color = Color(0xFF667085),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun AuthField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit = {}
) {

    Column {

        Text(
            text = label,
            color = Color(0xFF6B7280),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = value,
            onValueChange = onValueChange,

            modifier = Modifier.fillMaxWidth(),

            shape = RoundedCornerShape(14.dp),

            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF3F6FC),
                unfocusedContainerColor = Color(0xFFF3F6FC),

                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}