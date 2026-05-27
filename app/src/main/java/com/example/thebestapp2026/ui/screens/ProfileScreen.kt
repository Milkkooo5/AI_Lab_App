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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.thebestapp2026.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel,
    onLogout: () -> Unit = {}
) {
    val user by viewModel.user.collectAsState()
    val analysisCount by viewModel.analysisCount.collectAsState()
    val lastAnalysisDate by viewModel.lastAnalysisDate.collectAsState()
    val lastAnalysisStatus by viewModel.lastAnalysisStatus.collectAsState()
    var isEditing by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var genderMenuOpen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    LaunchedEffect(user) {
        val currentUser = user
        if (currentUser != null && !isEditing) {
            name = currentUser.name
            surname = currentUser.surname
            birthDate = currentUser.birthDate
            city = currentUser.city
            gender = currentUser.gender
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFF))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 52.dp)
    ) {
        Text(
            text = "Профиль",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD7E8FF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user?.name?.firstOrNull()?.uppercase() ?: "M",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F7DFF)
                )
            }

            Text(
                text = "${user?.name ?: "Milana"} ${user?.surname ?: "A."}",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827),
                modifier = Modifier.padding(top = 18.dp)
            )

            Text(
                text = user?.email ?: "milana@example.com",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileStat(
                    number = analysisCount.toString(),
                    label = "анализов",
                    modifier = Modifier.weight(1f)
                )
                ProfileStat(
                    number = lastAnalysisDate.ifBlank { "—" },
                    label = "последний",
                    modifier = Modifier.weight(1f)
                )
                ProfileStatusStat(
                    status = lastAnalysisStatus.ifBlank { "Нет данных" },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                if (isEditing) {
                    ProfileEditField("Имя", name) { name = it }
                    ProfileEditField("Фамилия", surname) { surname = it }
                    ProfileEditField("Дата рождения", birthDate) {
                        birthDate = formatBirthDate(it)
                    }
                    ProfileEditField("Город", city) { city = it }

                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = gender,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Пол") },
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier.fillMaxWidth()
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

                    Button(
                        onClick = {
                            viewModel.updateProfile(
                                name = name,
                                surname = surname,
                                birthDate = birthDate,
                                city = city,
                                gender = gender
                            )
                            isEditing = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .height(52.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F7DFF))
                    ) {
                        Text("Сохранить", fontWeight = FontWeight.Bold)
                    }
                } else {
                    ProfileInfoRow(
                        title = "Дата рождения",
                        value = user?.birthDate?.ifBlank { "не указано" } ?: "не указано"
                    )

                    ProfileInfoRow(
                        title = "Пол",
                        value = user?.gender?.ifBlank { "не указано" } ?: "не указано"
                    )

                    ProfileInfoRow(
                        title = "Город",
                        value = user?.city?.ifBlank { "не указано" } ?: "не указано"
                    )

                    Button(
                        onClick = { isEditing = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp)
                            .height(52.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F7DFF))
                    ) {
                        Text("Редактировать профиль", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Button(
            onClick = { navController.navigate("settings") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp)
                .height(54.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F7DFF))
        ) {
            Text(
                text = "Настройки",
                fontWeight = FontWeight.Bold
            )
        }

        TextButton(
            onClick = {
                viewModel.logout {
                    onLogout()
                    navController.navigate("login") {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Выйти",
                color = Color(0xFFFF4D3A)
            )
        }

        Spacer(modifier = Modifier.height(120.dp))
    }
}

@Composable
private fun ProfileEditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    )
}

private fun formatBirthDate(text: String): String {
    val digits = text.filter { it.isDigit() }.take(8)

    return when {
        digits.length <= 2 -> digits
        digits.length <= 4 -> digits.substring(0, 2) + "." + digits.substring(2)
        else -> digits.substring(0, 2) + "." + digits.substring(2, 4) + "." + digits.substring(4)
    }
}

@Composable
fun ProfileStat(
    number: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(92.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = number,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2F7DFF),
                textAlign = TextAlign.Center
            )

            Text(
                text = label,
                fontSize = 12.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(top = 6.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ProfileStatusStat(
    status: String,
    modifier: Modifier = Modifier
) {
    val isNormal = status.contains("Норма", ignoreCase = true)
    val hasDeviation = status.contains("Отклон", ignoreCase = true)
    val chipBackground = when {
        isNormal -> Color(0xFFEAF8EE)
        hasDeviation -> Color(0xFFFFF0E5)
        else -> Color(0xFFEFF6FF)
    }
    val chipColor = when {
        isNormal -> Color(0xFF22C55E)
        hasDeviation -> Color(0xFFFF8A00)
        else -> Color(0xFF2F7DFF)
    }

    Card(
        modifier = modifier.height(92.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(chipBackground)
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = status,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = chipColor,
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = "статус",
                fontSize = 12.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ProfileInfoRow(
    title: String,
    value: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFBFF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 13.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 13.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827),
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            )
        }
    }
}
