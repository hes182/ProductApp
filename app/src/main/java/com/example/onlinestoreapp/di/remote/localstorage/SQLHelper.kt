package com.example.onlinestoreapp.di.remote.localstorage

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class SQLHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABSE_VERSION) {

    val TABLE_NAME = "user"
    val COLM_IDUSER = "isuser"
    val COLM_TOKEN = "token"

    val CREATE_TABLE_USER = "CREATE TABLE $TABLE_NAME($COLM_TOKEN TEXT)"

    companion object {
        const val DATABASE_NAME = "appDB.db"
        const val DATABSE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            db?.execSQL(CREATE_TABLE_USER)
        }catch (e: SQLiteException) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        super.onDowngrade(db, oldVersion, newVersion)
    }
}