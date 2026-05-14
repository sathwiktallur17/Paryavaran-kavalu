package com.example.paryavarankavalu

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.paryavarankavalu.models.Report
import com.example.paryavarankavalu.models.User
import com.example.paryavarankavalu.repositories.ReportDao
import com.example.paryavarankavalu.repositories.UserDao

@Database(entities = [Report::class, User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reportDao(): ReportDao
    abstract fun userDao(): UserDao
}