package com.example.onlinestoreapp.di.remote.localstorage

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class SQLKontrol(context: Context) {
    val sqlHelper = SQLHelper(context)
    lateinit var db: SQLiteDatabase

    fun setSaveUser(token: String?) {
        db = sqlHelper.writableDatabase
        val query = "SELECT * FROM " + sqlHelper.TABLE_NAME
        val cursor = db.rawQuery(query, null)
        if (cursor.count > 0) {
            db.delete(sqlHelper.TABLE_NAME, null, null)
        }
        val valueIns = ContentValues()
        valueIns.put(sqlHelper.COLM_TOKEN, token)
        db.insert(sqlHelper.TABLE_NAME, null, valueIns)
        db.close()
    }

    @SuppressLint("Range")
    fun getToken(): String {
        var token = ""
        val query = "SELECT * FROM ${sqlHelper.TABLE_NAME}"
        db = sqlHelper.readableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.count > 0) {
            cursor.moveToFirst()
            token = cursor.getString(cursor.getColumnIndex(sqlHelper.COLM_TOKEN))
        }
        cursor.close()
        db.close()
        return token
    }

    fun checkUser() : Boolean {
        db = sqlHelper.readableDatabase
        val query = " SELECT * FROM ${sqlHelper.TABLE_NAME}"
        val cursor = db.rawQuery(query, null)
        if (cursor.count <= 0) {
            cursor.close()
            db.close()
            return false
        }
        cursor.close()
        db.close()
        return true
    }
}