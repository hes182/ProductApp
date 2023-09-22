package com.example.onlinestoreapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.onlinestoreapp.R
import com.example.onlinestoreapp.databinding.ActivityAddProductBinding

class AddProductActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView2.setOnClickListener { finish() }
    }
}