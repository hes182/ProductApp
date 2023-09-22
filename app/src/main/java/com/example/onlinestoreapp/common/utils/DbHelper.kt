package com.example.onlinestoreapp.common.utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.example.onlinestoreapp.di.remote.localstorage.AppDatabase

object DbHelper {
   fun setDb(context: Context): AppDatabase {
       return Room.databaseBuilder(
           context,
           AppDatabase::class.java,
           "app_database"
       ).build()
   }
}