package com.example.thebestapp2026.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.thebestapp2026.data.local.AnalysisEntity

@Dao
interface AnalysisDao {

    @Insert
    suspend fun saveAnalysis(
        analysis: AnalysisEntity
    )

    @Query("SELECT * FROM analysis WHERE userId = :userId ORDER BY id DESC")
    suspend fun getHistory(
        userId: String
    ): List<AnalysisEntity>

    @Query("SELECT * FROM analysis WHERE userId = :userId ORDER BY id DESC LIMIT 1")
    suspend fun getLastAnalysis(userId: String): AnalysisEntity?
}
