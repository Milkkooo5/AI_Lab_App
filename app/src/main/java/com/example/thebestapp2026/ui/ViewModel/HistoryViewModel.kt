package com.example.thebestapp2026.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thebestapp2026.App
import com.example.thebestapp2026.data.repository.AnalysisRepositoryImpl
import com.example.thebestapp2026.domain.Analysis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    private val repository = AnalysisRepositoryImpl(App.instance.applicationContext)

    private val _history = MutableStateFlow<List<Analysis>>(emptyList())
    val history: StateFlow<List<Analysis>> = _history

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    fun loadHistory(userId: String) {
        if (userId.isBlank()) {
            _history.value = emptyList()
            _error.value = "Пользователь не найден"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = ""

            try {
                _history.value = repository.getHistory(userId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка загрузки истории"
            }

            _isLoading.value = false
        }
    }

    fun clearHistory() {
        _history.value = emptyList()
        _error.value = ""
        _isLoading.value = false
    }
}
