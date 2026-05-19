package com.example.thebestapp2026.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "analysis")
data class AnalysisEntity (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val fileName: String,
    val aiText: String,
    val createdAt: String,
    val status: String
)