package com.example.paryavarankavalu.repositories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.paryavarankavalu.models.Report
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Insert
    suspend fun insertReport(report: Report): Long

    @Query("SELECT * FROM reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<Report>>

    @Update
    suspend fun updateReport(report: Report)

    @Query("SELECT * FROM reports WHERE id = :id")
    suspend fun getReportById(id: Long): Report?
}