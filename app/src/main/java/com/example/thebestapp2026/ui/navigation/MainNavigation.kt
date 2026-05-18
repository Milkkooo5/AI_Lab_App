package com.example.thebestapp2026.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
}

@Composable
fun MainNavigation() {

    val navController = rememberNavController()

    Scaffold(
        containerColor = Color(0xFFF8FAFF),

        bottomBar = {
            BottomBar(navController)
        }

    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = BottomItem.Home.route,
            modifier = Modifier.padding(padding)
        ) {

            composable(BottomItem.Home.route) {
                HomeScreen()
            }

            composable(BottomItem.Upload.route) {
                UploadScreen()
            }

            composable(BottomItem.History.route) {
                HistoryScreen()
            }

            composable(BottomItem.Profile.route) {
                ProfileScreen()
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController
) {

    val items = listOf(
        BottomItem.Home,
        BottomItem.Upload,
        BottomItem.History,
        BottomItem.Profile
    )

    Card(
        modifier = Modifier.padding(
            start = 18.dp,
            end = 18.dp,
            bottom = 18.dp
        ),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {

        NavigationBar(
            containerColor = Color.Transparent
        ) {

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach { item ->

                NavigationBarItem(
                    selected = currentRoute == item.route,

                    onClick = {
                        navController.navigate(item.route)
                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF2F7DFF),
                        selectedTextColor = Color(0xFF2F7DFF),
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = Color.LightGray,
                        unselectedTextColor = Color.LightGray
                    ),

                    icon = {

                        when (item.route) {

                            "home" -> {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = null
                                )
                            }

                            "upload" -> {
                                Icon(
                                    imageVector = Icons.Default.Upload,
                                    contentDescription = null
                                )
                            }

                            "history" -> {
                                Icon(
                                    imageVector = Icons.Outlined.History,
                                    contentDescription = null
                                )
                            }

                            else -> {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null
                                )
                            }
                        }
                    },

                    label = {
                        Text(text = item.title)
                    }
                )
            }
        }
    }
}