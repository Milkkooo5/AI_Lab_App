package com.example.thebestapp2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.thebestapp2026.ui.navigation.MainNavigation
import com.example.thebestapp2026.ui.screens.LoginScreen
import com.example.thebestapp2026.ui.screens.ProfileScreen
import com.example.thebestapp2026.ui.screens.RegisterScreen
import com.example.thebestapp2026.ui.theme.TheBestApp2026Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainNavigation()
        }
    }
}
