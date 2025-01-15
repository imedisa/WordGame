package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PartsActivity : AppCompatActivity() {

    private lateinit var partsContainer: LinearLayout
    private val totalParts = 3 // تعداد کل پارت‌ها

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parts)

        partsContainer = findViewById(R.id.partsContainer)

        // ایجاد دکمه‌های پارت‌ها
        for (part in 1..totalParts) {
            val button = Button(this).apply {
                text = "پارت $part"
                textSize = 18f
                setOnClickListener {
                    // رفتن به صفحه مراحل مربوط به این پارت
                    startActivity(Intent(this@PartsActivity, StagesActivity::class.java).apply {
                        putExtra("part", part)
                    })
                }
            }
            partsContainer.addView(button)
        }
    }
}