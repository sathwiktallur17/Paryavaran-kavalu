package com.example.paryavarankavalu.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Long = 1, // Single user for simplicity
    val totalPoints: Int = 0
)