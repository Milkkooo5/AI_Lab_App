package com.example.thebestapp2026.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel : ViewModel() {

    private val _notifications = MutableStateFlow(true)
    val notifications: StateFlow<Boolean> = _notifications

    private val _darkTheme = MutableStateFlow(false)
    val darkTheme: StateFlow<Boolean> = _darkTheme

    private val _saveInCloud = MutableStateFlow(true)
    val saveInCloud: StateFlow<Boolean> = _saveInCloud

    fun setNotifications(value: Boolean) {
        _notifications.value = value
    }

    fun setDarkTheme(value: Boolean) {
        _darkTheme.value = value
    }

    fun setSaveInCloud(value: Boolean) {
        _saveInCloud.value = value
    }
}