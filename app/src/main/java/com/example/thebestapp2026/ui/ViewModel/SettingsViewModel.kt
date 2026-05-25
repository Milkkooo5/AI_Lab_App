package com.example.thebestapp2026.ui.ViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel : ViewModel() {
    private val _notifications = MutableStateFlow(true)
    val notifications: StateFlow<Boolean> = _notifications

    private val _saveHistory = MutableStateFlow(true)
    val saveHistory: StateFlow<Boolean> = _saveHistory
    private val _darkTheme = MutableStateFlow(false)
    val darkTheme: StateFlow<Boolean> = _darkTheme

    fun setNotifications(value: Boolean) {
        _notifications.value = value
    }
    fun setSaveHistory(value: Boolean) {
        _saveHistory.value = value
    }
    fun setDarkTheme(value: Boolean) {
        _darkTheme.value = value
    }
}