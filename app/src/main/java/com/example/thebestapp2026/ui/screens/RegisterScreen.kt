package com.example.thebestapp2026.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.thebestapp2026.ui.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel
) {
    var name by rememberSaveable { mutableStateOf("") }
    var surname by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var birthDate by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf("") }
    var genderMenuOpen by rememberSaveable { mutableStateOf(false) }

    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(user) {
        if (user != null) {
            navController.navigate("home") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFF))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(42.dp))

        Text(
            text = "Регистрация",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )

        Text(
            text = "Создайте личный кабинет для анализов",
            color = Color(0xFF6B7280),
            fontSize = 15.sp,
            modifier = Modifier.padding(top = 6.dp)
        )

        Spacer(modifier = Modifier.height(26.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                RegisterField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Имя"
                )

                RegisterField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = "Фамилия"
                )

                RegisterField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email"
                )

                RegisterField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Пароль",
                    isPassword = true
                )

                RegisterField(
                    value = birthDate,
                    onValueChange = { birthDate = formatRegisterBirthDate(it) },
                    label = "Дата рождения"
                )

                RegisterField(
                    value = city,
                    onValueChange = { city = it },
                    label = "Город"
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = gender,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Пол") },
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    )

                    TextButton(
                        onClick = { genderMenuOpen = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                    ) {
                        Text("")
                    }

                    DropdownMenu(
                        expanded = genderMenuOpen,
                        onDismissRequest = { genderMenuOpen = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Мужской") },
                            onClick = {
                                gender = "Мужской"
                                genderMenuOpen = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Женский") },
                            onClick = {
                                gender = "Женский"
                                genderMenuOpen = false
                            }
                        )
                    }
                }

                if (error.isNotBlank()) {
                    Text(
                        text = error,
                        color = Color(0xFFFF4D3A),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }

                Button(
                    onClick = {
                        viewModel.register(
                            name = name,
                            surname = surname,
                            email = email,
                            password = password,
                            birthDate = birthDate,
                            city = city,
                            gender = gender
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 22.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2F7DFF)
                    ),
                    enabled = !isLoading
                ) {
                    Text(
                        text = if (isLoading) "Создание..." else "Создать аккаунт",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                TextButton(
                    onClick = { navController.navigate("login") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Уже есть аккаунт? Войти",
                        color = Color(0xFF2F7DFF)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(42.dp))
    }
}

@Composable
private fun RegisterField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (isPassword) {
            PasswordVisualTransformation()
        } else {
            androidx.compose.ui.text.input.VisualTransformation.None
        },
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    )
}

private fun formatRegisterBirthDate(text: String): String {
    val digits = text.filter { it.isDigit() }.take(8)

    return when {
        digits.length <= 2 -> digits
        digits.length <= 4 -> digits.substring(0, 2) + "." + digits.substring(2)
        else -> digits.substring(0, 2) + "." + digits.substring(2, 4) + "." + digits.substring(4)
    }
}
