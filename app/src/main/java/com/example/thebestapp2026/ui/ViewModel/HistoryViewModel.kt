package com.example.thebestapp2026.ui.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thebestapp2026.data.repository.AnalysisRepository
import com.example.thebestapp2026.domain.Analysis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: AnalysisRepository
) : ViewModel() {
    private val _history = MutableStateFlow<List<Analysis>>(emptyList())
    val history: StateFlow<List<Analysis>> = _history
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadHistory(
        userId: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _history.value = repository.getHistory(userId)
            _isLoading.value = false
        }
    }
}