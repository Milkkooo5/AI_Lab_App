package com.example.thebestapp2026.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.thebestapp2026.data.session.SessionManager
import com.example.thebestapp2026.ui.viewmodel.UploadViewModel

@Composable
fun UploadScreen(
    navController: NavController,
    userId: String,
    viewModel: UploadViewModel
) {
    val context = LocalContext.current

    val analysis by viewModel.analysis.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf("Файл не выбран") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            selectedUri = uri
            selectedFileName = uri.lastPathSegment ?: "analysis.pdf"
        }
    }

    LaunchedEffect(analysis) {
        if (analysis != null) {
            navController.navigate("result")
            viewModel.clearResult()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFF))
            .padding(horizontal = 24.dp)
            .padding(top = 52.dp)
    ) {
        Text(
            text = "Загрузка",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )

        Text(
            text = "Добавьте PDF анализов",
            color = Color(0xFF6B7280),
            fontSize = 15.sp,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(26.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .padding(18.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = Color(0xFFBFD8FF),
                            cornerRadius = CornerRadius(28.dp.toPx(), 28.dp.toPx()),
                            style = Stroke(
                                width = 2.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 12f))
                            )
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Выберите файл анализа",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )

                    Text(
                        text = "PDF до 20 MB",
                        color = Color(0xFF6B7280),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    TextButton(
                        onClick = { launcher.launch(arrayOf("application/pdf")) },
                        modifier = Modifier.padding(top = 14.dp)
                    ) {
                        Text(
                            text = "Выбрать файл",
                            color = Color(0xFF2F7DFF),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (selectedUri != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = selectedFileName,
                        color = Color(0xFF111827),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Text(
                        text = "Файл готов к загрузке",
                        color = Color(0xFF6B7280),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
            }
        }

        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                color = Color(0xFF2F7DFF)
            )

            Text(
                text = "Анализируем PDF...",
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        if (error.isNotBlank()) {
            Text(
                text = error,
                color = Color(0xFFFF4D3A),
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Button(
            onClick = {
                val uri = selectedUri
                if (uri != null) {
                    val currentUserId = userId.ifBlank { SessionManager.currentUser?.userId ?: "" }
                    println("upload userId = $currentUserId")
                    if (currentUserId.isBlank()) return@Button

                    viewModel.uploadAnalysis(
                        context = context,
                        userId = currentUserId,
                        fileUri = uri
                    )
                }
            },
            enabled = selectedUri != null && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2F7DFF)
            )
        ) {
            Text(
                text = if (isLoading) "Загрузка..." else "Загрузить файл",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
