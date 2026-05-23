package com.example.thebestapp2026.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.thebestapp2026.ui.screens.*

sealed class BottomItem(
    val route: String,
    val title: String
) {
    object Home : BottomItem("home", "Главная")
    object Upload : BottomItem("upload", "Загрузка")
    object History : BottomItem("history", "История")
    object Profile : BottomItem("profile", "Профиль")
    object Result : BottomItem("result", "Результат")

}

@Composable
fun MainNavigation() {

    val navController = rememberNavController()
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(

        containerColor = Color(0xFFF8FAFF),
        bottomBar = {
            if (
                currentRoute != "login" &&
                currentRoute != "register"
            ) {
                BottomBar(navController)
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(padding)
        ) {

            composable(BottomItem.Home.route) {
                HomeScreen()
            }

            composable(BottomItem.Upload.route) {
                UploadScreen()
            }

            composable(BottomItem.History.route) {
                HistoryScreen( navController = navController)
            }

            composable(BottomItem.Profile.route) {
                ProfileScreen()
            }
            composable(BottomItem.Result.route) {
                ResultScreen( navController = navController)
            }
            composable("login") {
                LoginScreen(navController = navController)
            }

            composable("register") {
                RegisterScreen(navController = navController)
            }

        }
    }
}

@Composable
fun BottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 18.dp
            ),

        shape = RoundedCornerShape(30.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(10.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),

            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            BottomBarItem(
                title = "Главная",
                isSelected = false
            ) {
                navController.navigate("home")
            }

            BottomBarItem(
                title = "Загрузка",
                isSelected = false
            ) {
                navController.navigate("upload")
            }

            BottomBarItem(
                title = "История",
                isSelected = false
            ) {
                navController.navigate("history")
            }

            BottomBarItem(
                title = "Профиль",
                isSelected = false
            ) {
                navController.navigate("profile")
            }
        }
    }
}

@Composable
fun BottomBarItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = Modifier.clickable {
            onClick()
        }
    ) {

        Box(
            modifier = Modifier
                .size(14.dp)
                .background(
                    if (isSelected)
                        Color(0xFF2F7DFF)
                    else
                        Color(0xFFE5E7EB),

                    CircleShape
                )
        )

        Text(
            text = title,

            fontSize = 11.sp,

            fontWeight = if (isSelected)
                FontWeight.Bold
            else
                FontWeight.Normal,

            color = if (isSelected)
                Color(0xFF2F7DFF)
            else
                Color(0xFF6B7280),

            modifier = Modifier.padding(top = 6.dp)
        )
    }
}