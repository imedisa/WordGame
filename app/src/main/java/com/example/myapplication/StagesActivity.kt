package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StagesActivity : AppCompatActivity() {

    private lateinit var stagesContainer: LinearLayout
    private lateinit var backToPartsButton: Button
    private val totalStages = 10
    private var currentPart: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stages)

        stagesContainer = findViewById(R.id.stagesContainer)
        backToPartsButton = findViewById(R.id.backToPartsButton)

        currentPart = intent.getIntExtra("part", 1)

        for (stage in 1..totalStages) {
            val button = Button(this).apply {
                text = "مرحله $stage"
                textSize = 18f
                setOnClickListener {
                    if (StageManager.isStageUnlocked(currentPart, stage)) {
                        startActivity(Intent(this@StagesActivity, GameActivity::class.java).apply {
                            putExtra("part", currentPart)
                            putExtra("stage", stage)
                        })
                    } else {
                        Toast.makeText(
                            this@StagesActivity,
                            "مرحله $stage قفل است! ابتدا مرحله قبلی را کامل کنید.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                isEnabled = StageManager.isStageUnlocked(currentPart, stage)
                alpha = if (isEnabled) 1f else 0.5f
            }
            stagesContainer.addView(button)
        }

        backToPartsButton.setOnClickListener {
            startActivity(Intent(this, PartsActivity::class.java))
            finish()
        }
    }
}