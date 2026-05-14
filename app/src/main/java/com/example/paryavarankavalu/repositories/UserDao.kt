package com.example.paryavarankavalu.repositories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.paryavarankavalu.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE id = 1")
    fun getUser(): Flow<User>

    @Update
    suspend fun updateUser(user: User)
}