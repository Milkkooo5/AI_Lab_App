package com.example.thebestapp2026.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "analysis")
data class AnalysisEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val fileName: String,
    val resultJson: String,
    val createdAt: String
)