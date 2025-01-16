package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StagesActivity : AppCompatActivity() {

    private lateinit var stagesContainer: LinearLayout
    private lateinit var backToPartsButton: Button
    private lateinit var scoreTextView: TextView
    private val totalStages = 10
    private var currentPart: Int = 1
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stages)

        sharedPreferences = getSharedPreferences("game_prefs", MODE_PRIVATE)
        scoreTextView = findViewById(R.id.scoreTextView)
        stagesContainer = findViewById(R.id.stagesContainer)
        backToPartsButton = findViewById(R.id.backToPartsButton)

        // نمایش امتیاز کاربر
        updateScoreDisplay()

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

    private fun updateScoreDisplay() {
        val score = sharedPreferences.getInt("user_score", 0)
        scoreTextView.text = getString(R.string.score_text, score)
    }
}