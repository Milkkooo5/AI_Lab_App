package com.example.thebestapp2026.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.thebestapp2026.App
import com.example.thebestapp2026.data.local.AppDatabase
import com.example.thebestapp2026.data.local.TokenStorage
import com.example.thebestapp2026.data.local.UserEntity
import com.example.thebestapp2026.data.repository.AnalysisRepositoryImpl
import com.example.thebestapp2026.data.repository.AuthRepositoryImpl
import com.example.thebestapp2026.data.session.SessionManager
import com.example.thebestapp2026.domain.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val context = App.instance.applicationContext
    private val repository = AuthRepositoryImpl(context)
    private val analysisRepository = AnalysisRepositoryImpl(context)
    private val tokenStorage = TokenStorage(context)

    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_database"
    ).build()

    private val userDao = db.userDao()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _analysisCount = MutableStateFlow(0)
    val analysisCount: StateFlow<Int> = _analysisCount

    private val _lastAnalysisDate = MutableStateFlow("—")
    val lastAnalysisDate: StateFlow<String> = _lastAnalysisDate

    private val _lastAnalysisStatus = MutableStateFlow("Нет данных")
    val lastAnalysisStatus: StateFlow<String> = _lastAnalysisStatus

    fun loadUser() {
        viewModelScope.launch {
            val currentUser = repository.getCurrentUser()
            _user.value = currentUser
            loadAnalysisStats(currentUser?.userId ?: "")
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _user.value = null
            clearStats()
        }
    }

    fun logout(onFinished: () -> Unit) {
        viewModelScope.launch {
            repository.logout()
            _user.value = null
            clearStats()
            onFinished()
        }
    }

    fun updateProfile(
        name: String,
        surname: String,
        birthDate: String,
        city: String,
        gender: String
    ) {
        viewModelScope.launch {
            val oldUser = _user.value ?: repository.getCurrentUser() ?: return@launch

            val updatedUser = oldUser.copy(
                name = name,
                surname = surname,
                birthDate = birthDate,
                city = city,
                gender = gender
            )

            userDao.saveUser(
                UserEntity(
                    userId = updatedUser.userId,
                    name = updatedUser.name,
                    surname = updatedUser.surname,
                    email = updatedUser.email,
                    birthDate = updatedUser.birthDate,
                    city = updatedUser.city,
                    gender = updatedUser.gender
                )
            )

            SessionManager.save(context, updatedUser, tokenStorage.getToken())
            _user.value = updatedUser
        }
    }

    private suspend fun loadAnalysisStats(userId: String) {
        if (userId.isBlank()) {
            clearStats()
            return
        }

        val history = analysisRepository.getHistory(userId)
        val lastAnalysis = history.firstOrNull()

        _analysisCount.value = history.size
        _lastAnalysisDate.value = lastAnalysis?.createdAt ?: "—"
        _lastAnalysisStatus.value = if (lastAnalysis == null) {
            "Нет данных"
        } else if (lastAnalysis.aiText.contains("отклонение", ignoreCase = true)) {
            "Отклонение"
        } else {
            "Норма"
        }
    }

    private fun clearStats() {
        _analysisCount.value = 0
        _lastAnalysisDate.value = "—"
        _lastAnalysisStatus.value = "Нет данных"
    }
}
