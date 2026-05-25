package com.example.thebestapp2026.ui.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thebestapp2026.data.repository.AuthRepository
import com.example.thebestapp2026.domain.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: AuthRepository
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun loadUser() {
        viewModelScope.launch {
            _user.value = repository.getCurrentUser()
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _user.value = null
        }
    }
}