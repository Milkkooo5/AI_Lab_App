package com.example.thebestapp2026.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thebestapp2026.App
import com.example.thebestapp2026.data.repository.AuthRepositoryImpl
import com.example.thebestapp2026.domain.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository = AuthRepositoryImpl(
        App.instance.applicationContext
    )

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = ""

            repository.login(email, password)
                .onSuccess { _user.value = it }
                .onFailure { _error.value = it.message ?: "Ошибка входа" }

            _isLoading.value = false
        }
    }

    fun clearUser() {
        _user.value = null
        _error.value = ""
        _isLoading.value = false
    }
}
