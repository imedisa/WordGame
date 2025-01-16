package com.example.wordgamebymedisa

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private lateinit var startButton: Button
    private lateinit var messageTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById(R.id.startButton)
        messageTextView = findViewById(R.id.messageTextView)

        startButton.setOnClickListener {
            // رفتن به صفحه انتخاب پارت‌ها
            startActivity(Intent(this, PartsActivity::class.java))
        }
    }
}