package com.example.thebestapp2026.ui.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thebestapp2026.App
import com.example.thebestapp2026.data.repository.AnalysisRepositoryImpl
import com.example.thebestapp2026.domain.Analysis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = AnalysisRepositoryImpl(
        App.instance.applicationContext
    )

    private val _analysis = MutableStateFlow<Analysis?>(null)
    val analysis: StateFlow<Analysis?> = _analysis

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadLastAnalysis() {
        viewModelScope.launch {
            _isLoading.value = true
            _analysis.value = null
            _analysis.value = repository.getLastAnalysis()
            _isLoading.value = false
        }
    }

    fun clearAnalysis() {
        _analysis.value = null
    }
}
