package com.example.paryavarankavalu.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class Report(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val wasteType: String,
    val photoPath: String, // Path to compressed photo
    val status: String = "Pending", // Pending or Cleaned
    val timestamp: Long = System.currentTimeMillis(),
    val pointsAwarded: Int = 10 // Points for reporting
)