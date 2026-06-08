package com.example.thebestapp2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.thebestapp2026.data.session.SessionManager
import com.example.thebestapp2026.ui.navigation.MainNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_TheBestApp2026)
        super.onCreate(savedInstanceState)
        SessionManager.init(this)
        enableEdgeToEdge()
        setContent {
            MainNavigation()
        }
    }
}
