package com.example.onlinestoreapp.di.remote.localstorage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "iduser") val iduser: Int?,
    @ColumnInfo(name = "token") val token: String?
)