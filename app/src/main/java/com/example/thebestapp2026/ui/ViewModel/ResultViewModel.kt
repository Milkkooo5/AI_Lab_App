package com.example.thebestapp2026.ui.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thebestapp2026.data.repository.AnalysisRepository
import com.example.thebestapp2026.domain.Analysis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ResultViewModel(
    private val repository: AnalysisRepository
) : ViewModel() {
    private val _analysis = MutableStateFlow<Analysis?>(null)
    val analysis: StateFlow<Analysis?> = _analysis
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadLastAnalysis() {

        viewModelScope.launch {
            _isLoading.value = true
            _analysis.value = repository.getLastAnalysis()
            _isLoading.value = false
        }
    }
}