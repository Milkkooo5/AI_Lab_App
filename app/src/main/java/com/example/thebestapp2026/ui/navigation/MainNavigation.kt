package com.example.thebestapp2026.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudUpload
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.thebestapp2026.data.session.SessionManager
import com.example.thebestapp2026.ui.ViewModel.HomeViewModel
import com.example.thebestapp2026.ui.screens.HistoryScreen
import com.example.thebestapp2026.ui.screens.HomeScreen
import com.example.thebestapp2026.ui.screens.LoginScreen
import com.example.thebestapp2026.ui.screens.ProfileScreen
import com.example.thebestapp2026.ui.screens.RegisterScreen
import com.example.thebestapp2026.ui.screens.ResultScreen
import com.example.thebestapp2026.ui.screens.SettingsScreen
import com.example.thebestapp2026.ui.screens.UploadScreen
import com.example.thebestapp2026.ui.viewmodel.HistoryViewModel
import com.example.thebestapp2026.ui.viewmodel.LoginViewModel
import com.example.thebestapp2026.ui.viewmodel.ProfileViewModel
import com.example.thebestapp2026.ui.viewmodel.RegisterViewModel
import com.example.thebestapp2026.ui.viewmodel.ResultViewModel
import com.example.thebestapp2026.ui.viewmodel.SettingsViewModel
import com.example.thebestapp2026.ui.viewmodel.UploadViewModel

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    val loginViewModel: LoginViewModel = viewModel()
    val registerViewModel: RegisterViewModel = viewModel()
    val uploadViewModel: UploadViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val resultViewModel: ResultViewModel = viewModel()
    val historyViewModel: HistoryViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()

    val loginUser by loginViewModel.user.collectAsState()
    val registerUser by registerViewModel.user.collectAsState()
    val profileUser by profileViewModel.user.collectAsState()

    val userId = loginUser?.userId
        ?: registerUser?.userId
        ?: profileUser?.userId
        ?: SessionManager.currentUser?.userId
        ?: ""

    val startDestination = if (
        SessionManager.token.isNotBlank() &&
        SessionManager.currentUser != null
    ) {
        "home"
    } else {
        "login"
    }

    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        containerColor = Color(0xFFF8FAFF),
        bottomBar = {
            if (
                currentRoute != null &&
                currentRoute != "login" &&
                currentRoute != "register"
            ) {
                BottomBar(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(padding)
        ) {
            composable("login") {
                LoginScreen(
                    navController = navController,
                    viewModel = loginViewModel
                )
            }

            composable("register") {
                RegisterScreen(
                    navController = navController,
                    viewModel = registerViewModel
                )
            }

            composable("home") {
                HomeScreen(
                    navController = navController,
                    viewModel = homeViewModel
                )
            }

            composable("upload") {
                UploadScreen(
                    navController = navController,
                    userId = userId,
                    viewModel = uploadViewModel
                )
            }

            composable("history") {
                HistoryScreen(
                    navController = navController,
                    userId = userId,
                    viewModel = historyViewModel
                )
            }

            composable("profile") {
                ProfileScreen(
                    navController = navController,
                    viewModel = profileViewModel,
                    onLogout = {
                        loginViewModel.clearUser()
                        registerViewModel.clearUser()
                        homeViewModel.clearAnalysis()
                        resultViewModel.clearAnalysis()
                        historyViewModel.clearHistory()
                    }
                )
            }

            composable("settings") {
                SettingsScreen(
                    navController = navController,
                    viewModel = settingsViewModel
                )
            }

            composable("result") {
                ResultScreen(
                    navController = navController,
                    viewModel = resultViewModel
                )
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavController,
    currentRoute: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 16.dp),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 13.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomBarItem(
                icon = Icons.Rounded.Home,
                title = "Главная",
                isSelected = currentRoute == "home"
            ) {
                navController.navigate("home") {
                    launchSingleTop = true
                    popUpTo("home") { saveState = true }
                }
            }

            BottomBarItem(
                icon = Icons.Rounded.CloudUpload,
                title = "Загрузка",
                isSelected = currentRoute == "upload"
            ) {
                navController.navigate("upload") {
                    launchSingleTop = true
                }
            }

            BottomBarItem(
                icon = Icons.Rounded.History,
                title = "История",
                isSelected = currentRoute == "history"
            ) {
                navController.navigate("history") {
                    launchSingleTop = true
                }
            }

            BottomBarItem(
                icon = Icons.Rounded.Person,
                title = "Профиль",
                isSelected = currentRoute == "profile"
            ) {
                navController.navigate("profile") {
                    launchSingleTop = true
                }
            }
        }
    }
}

@Composable
fun BottomBarItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) Color(0xFF2F7DFF) else Color(0xFFE5ECF6),
            modifier = Modifier.size(22.dp)
        )

        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color(0xFF2F7DFF) else Color(0xFF8A94A6),
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}
