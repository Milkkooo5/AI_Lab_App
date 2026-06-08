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

    private var isAnalysisLoaded = false

    fun loadLastAnalysis() {
        if (isAnalysisLoaded) {
            return
        }

        if (_isLoading.value) {
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                _analysis.value = repository.getLastAnalysis()
                isAnalysisLoaded = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearAnalysis() {
        _analysis.value = null
        isAnalysisLoaded = false
    }
}
