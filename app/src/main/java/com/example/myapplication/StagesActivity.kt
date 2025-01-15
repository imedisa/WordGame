package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StagesActivity : AppCompatActivity() {

    private lateinit var stagesContainer: LinearLayout
    private val totalStages = 10 // تعداد کل مراحل

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stages)

        stagesContainer = findViewById(R.id.stagesContainer)

        // ایجاد دکمه‌های مراحل
        for (stage in 1..totalStages) {
            val button = Button(this).apply {
                text = "مرحله $stage"
                textSize = 18f
                setOnClickListener {
                    if (StageManager.isStageUnlocked(stage)) {
                        // رفتن به صفحه بازی (GameActivity)
                        startActivity(Intent(this@StagesActivity, GameActivity::class.java).apply {
                            putExtra("stage", stage)
                        })
                    } else {
                        // نمایش پیام خطا (مرحله قفل است)
                        Toast.makeText(
                            this@StagesActivity,
                            "مرحله $stage قفل است! ابتدا مرحله قبلی را کامل کنید.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                // تنظیم وضعیت دکمه (باز یا قفل)
                isEnabled = StageManager.isStageUnlocked(stage)
                alpha = if (isEnabled) 1f else 0.5f // کاهش شفافیت برای مراحل قفل شده
            }
            stagesContainer.addView(button)
        }
    }
}