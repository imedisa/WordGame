package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

class PartsActivity : AppCompatActivity() {

    private lateinit var partsContainer: LinearLayout
    private val totalParts = 3 // تعداد کل پارت‌ها

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parts)

        partsContainer = findViewById(R.id.partsContainer)

        // ایجاد دکمه‌های پارت‌ها
        for (part in 1..totalParts) {
            // ایجاد CardView برای هر پارت
            val cardView = CardView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 16, 16, 16) // فاصله بین کارت‌ها
                }
                radius = 8f // گوشه‌های گرد
                cardElevation = 4f // سایه
                setCardBackgroundColor(ContextCompat.getColor(this@PartsActivity, android.R.color.white)) // رنگ پس‌زمینه
            }

            // ایجاد دکمه داخل CardView
            val button = Button(this).apply {
                text = "بخش $part"
                textSize = 18f
                setBackgroundResource(R.drawable.button_background) // پس‌زمینه دکمه
                setTextColor(Color.WHITE) // رنگ متن سفید
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 16, 16, 16) // فاصله داخلی دکمه
                }
                setOnClickListener {
                    // رفتن به صفحه مراحل مربوط به این پارت
                    startActivity(Intent(this@PartsActivity, StagesActivity::class.java).apply {
                        putExtra("part", part)
                    })
                }
            }

            // اضافه کردن دکمه به CardView
            cardView.addView(button)

            // اضافه کردن CardView به لیست پارت‌ها
            partsContainer.addView(cardView)
        }
    }
}