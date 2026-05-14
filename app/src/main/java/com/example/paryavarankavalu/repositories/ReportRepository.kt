package com.example.paryavarankavalu.repositories

import com.example.paryavarankavalu.models.Report
import com.example.paryavarankavalu.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ReportRepository(private val reportDao: ReportDao, private val userDao: UserDao) {

    val allReports: Flow<List<Report>> = reportDao.getAllReports()
    val user: Flow<User> = userDao.getUser()

    suspend fun insertReport(report: Report): Long {
        val id = reportDao.insertReport(report)
        // Award points
        val currentUser = userDao.getUser().first()
        val updatedUser = currentUser.copy(totalPoints = currentUser.totalPoints + report.pointsAwarded)
        userDao.insertUser(updatedUser)
        return id
    }

    suspend fun updateReport(report: Report) {
        reportDao.updateReport(report)
    }

    suspend fun getReportById(id: Long): Report? {
        return reportDao.getReportById(id)
    }
}