package com.example.thebestapp2026.ui.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thebestapp2026.data.repository.AuthRepository
import com.example.thebestapp2026.domain.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository
): ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = ""

            repository.login(email, password)
                .onSuccess {
                    _user.value = it
                }
                .onFailure {
                    _error.value = it.message ?: "Ошибка входа"
                }

            _isLoading.value = false
        }
    }
}