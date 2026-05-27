package com.example.thebestapp2026.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thebestapp2026.App
import com.example.thebestapp2026.data.repository.AuthRepositoryImpl
import com.example.thebestapp2026.domain.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val repository = AuthRepositoryImpl(App.instance.applicationContext)

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    fun register(
        name: String,
        surname: String,
        email: String,
        password: String,
        birthDate: String = "",
        city: String = "",
        gender: String = ""
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = ""

            repository.register(
                name = name,
                surname = surname,
                email = email,
                password = password,
                birthDate = birthDate,
                city = city,
                gender = gender
            ).onSuccess {
                _user.value = it
            }.onFailure {
                _error.value = it.message ?: "Ошибка регистрации"
            }

            _isLoading.value = false
        }
    }

    fun clearUser() {
        _user.value = null
        _error.value = ""
        _isLoading.value = false
    }
}
