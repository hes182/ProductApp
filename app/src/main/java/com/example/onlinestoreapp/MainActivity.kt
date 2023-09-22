package com.example.onlinestoreapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.onlinestoreapp.common.utils.DbHelper
import com.example.onlinestoreapp.databinding.ActivityMainBinding
import com.example.onlinestoreapp.di.remote.localstorage.SQLKontrol
import com.example.onlinestoreapp.di.remote.localstorage.User
import com.example.onlinestoreapp.di.remote.localstorage.UserDao
import com.example.onlinestoreapp.view.ListProductActivity
import com.example.onlinestoreapp.view.LoginActivity
import com.example.onlinestoreapp.view.RegisterActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var sqlKontrol: SQLKontrol

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val inten = Intent(this, RegisterActivity::class.java)
            startActivity(inten)
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        sqlKontrol = SQLKontrol(this)

        if (sqlKontrol.checkUser()) {
            val inten = Intent(this, ListProductActivity::class.java)
            startActivity(inten)
            finish()
        }

    }

    private fun dbCekUser() {
    }
}