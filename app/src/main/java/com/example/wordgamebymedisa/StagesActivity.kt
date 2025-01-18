package com.example.wordgamebymedisa

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

class StagesActivity : AppCompatActivity() {

    private lateinit var stagesContainer: GridLayout
    private lateinit var backToPartsButton: Button
    private lateinit var scoreTextView: TextView
    private val totalStages = 10
    private var currentPart: Int = 1
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stages)

        sharedPreferences = getSharedPreferences("game_prefs", MODE_PRIVATE)
        StageManager.setSharedPreferences(sharedPreferences)
        scoreTextView = findViewById(R.id.scoreTextView)
        stagesContainer = findViewById(R.id.stagesContainer)
        backToPartsButton = findViewById(R.id.backToPartsButton)

        updateScoreDisplay()

        currentPart = intent.getIntExtra("part", 1)

        for (stage in 1..totalStages) {
            val cardView = CardView(this).apply {
                val params = GridLayout.LayoutParams().apply {
                    width = 200
                    height = 200
                    setMargins(16, 16, 16, 16)
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setGravity(Gravity.CENTER)
                }
                layoutParams = params
                radius = 100f
                cardElevation = 4f
                setCardBackgroundColor(ContextCompat.getColor(this@StagesActivity, android.R.color.white))
            }

            val button = Button(this).apply {
                text = "$stage"
                textSize = 24f
                setBackgroundResource(R.drawable.circle_button_background)
                setTextColor(Color.WHITE)
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setOnClickListener {
                    if (StageManager.isStageUnlocked(currentPart, stage)) {
                        startActivity(Intent(this@StagesActivity, GameActivity::class.java).apply {
                            putExtra("part", currentPart)
                            putExtra("stage", stage)
                        })
                    } else {
                        Toast.makeText(
                            this@StagesActivity,
                            getString(R.string.stage_locked, stage),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                isEnabled = StageManager.isStageUnlocked(currentPart, stage)
                alpha = if (isEnabled) 1f else 0.5f
            }

            cardView.addView(button)
            stagesContainer.addView(cardView)
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