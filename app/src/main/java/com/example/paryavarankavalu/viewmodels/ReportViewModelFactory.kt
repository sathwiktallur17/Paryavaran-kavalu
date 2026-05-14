package com.example.paryavarankavalu.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.paryavarankavalu.AppDatabase
import com.example.paryavarankavalu.repositories.ReportRepository

class ReportViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportViewModel::class.java)) {
            val database = Room.databaseBuilder(
                application,
                AppDatabase::class.java,
                "paryavaran_kavalu_db"
            ).build()
            val repository = ReportRepository(database.reportDao(), database.userDao())
            @Suppress("UNCHECKED_CAST")
            return ReportViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}