package com.example.paryavarankavalu.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paryavarankavalu.models.Report
import com.example.paryavarankavalu.models.User
import com.example.paryavarankavalu.repositories.ReportRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ReportViewModel(private val repository: ReportRepository) : ViewModel() {

    val allReports: Flow<List<Report>> = repository.allReports
    val user: Flow<User> = repository.user

    fun insertReport(report: Report) {
        viewModelScope.launch {
            repository.insertReport(report)
        }
    }

    fun updateReport(report: Report) {
        viewModelScope.launch {
            repository.updateReport(report)
        }
    }
}