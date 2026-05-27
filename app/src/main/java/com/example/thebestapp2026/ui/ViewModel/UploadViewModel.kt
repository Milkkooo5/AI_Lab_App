package com.example.thebestapp2026.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thebestapp2026.App
import com.example.thebestapp2026.data.repository.AnalysisRepositoryImpl
import com.example.thebestapp2026.domain.Analysis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UploadViewModel : ViewModel() {

    private val repository = AnalysisRepositoryImpl(App.instance.applicationContext)

    private val _analysis = MutableStateFlow<Analysis?>(null)
    val analysis: StateFlow<Analysis?> = _analysis

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    fun uploadAnalysis(context: Context, userId: String, fileUri: Uri) {
        if (userId.isBlank()) {
            _error.value = "Пользователь не найден. Войдите заново."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = ""

            repository.uploadAnalysis(context, userId, fileUri)
                .onSuccess { _analysis.value = it }
                .onFailure { _error.value = it.message ?: "Ошибка загрузки файла" }

            _isLoading.value = false
        }
    }

    fun clearResult() {
        _analysis.value = null
    }
}
