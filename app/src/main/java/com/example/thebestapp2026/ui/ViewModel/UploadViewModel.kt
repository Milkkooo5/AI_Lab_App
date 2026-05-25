package com.example.thebestapp2026.ui.ViewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thebestapp2026.data.repository.AnalysisRepository
import com.example.thebestapp2026.domain.Analysis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UploadViewModel(
    private val repository: AnalysisRepository
) : ViewModel() {
    private val _analysis = MutableStateFlow<Analysis?>(null)
    val analysis: StateFlow<Analysis?> = _analysis
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    fun uploadAnalysis(
        context: Context,
        userId: String,
        fileUri: Uri
    ) {
        viewModelScope.launch {

            _isLoading.value = true
            _error.value = ""

            repository.uploadAnalysis(
                context,
                userId,
                fileUri
            ).onSuccess {

                _analysis.value = it

            }.onFailure {

                _error.value = it.message ?: "Ошибка загрузки файла"
            }
            _isLoading.value = false
        }
    }
}