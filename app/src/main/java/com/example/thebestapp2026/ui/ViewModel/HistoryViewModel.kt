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

    private var isHistoryLoaded = false
    private var loadedUserId = ""

    fun loadHistory(userId: String) {
        if (userId.isBlank()) {
            _error.value = "Пользователь не найден"
            _isLoading.value = false
            return
        }

        if (isHistoryLoaded && loadedUserId == userId) {
            return
        }

        if (_isLoading.value) {
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = ""

            try {
                val localHistory = repository.getLocalHistory(userId)
                if (localHistory.isNotEmpty()) {
                    _history.value = localHistory
                }

                val updatedHistory = repository.getHistory(userId)
                _history.value = updatedHistory
                isHistoryLoaded = true
                loadedUserId = userId
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка загрузки истории"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshHistory(userId: String) {
        isHistoryLoaded = false
        loadedUserId = ""
        loadHistory(userId)
    }

    fun clearHistory() {
        _history.value = emptyList()
        _error.value = ""
        _isLoading.value = false
        isHistoryLoaded = false
        loadedUserId = ""
    }
}
