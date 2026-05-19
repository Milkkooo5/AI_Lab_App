package com.example.thebestapp2026.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity (

    @PrimaryKey
    val userId: String,
    val name: String,
    val surname: String,
    val email: String,
    val birthDate: String,
    val city: String,
    val gender: String

)