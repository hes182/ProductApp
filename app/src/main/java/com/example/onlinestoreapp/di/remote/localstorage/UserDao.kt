package com.example.onlinestoreapp.di.remote.localstorage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getUser(): List<User>

    @Query("SELECT EXISTS (SELECT 1 FROM User where id = :id)")
    fun getUserCheck(id: Int): Boolean

    @Insert
    fun insertUser(vararg users: User)

    @Delete
    fun deleteUser(user: User)
}