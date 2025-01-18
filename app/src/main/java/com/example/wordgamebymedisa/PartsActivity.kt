package com.example.wordgamebymedisa

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
    private val totalParts = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parts)

        partsContainer = findViewById(R.id.partsContainer)

        for (part in 1..totalParts) {
            val cardView = CardView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 16, 16, 16)
                }
                radius = 8f
                cardElevation = 4f
                setCardBackgroundColor(ContextCompat.getColor(this@PartsActivity, android.R.color.white))
            }

            val button = Button(this).apply {
                text = "بخش $part"
                textSize = 18f
                setBackgroundResource(R.drawable.button_background)
                setTextColor(Color.WHITE)
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 16, 16, 16)
                }
                setOnClickListener {
                    startActivity(Intent(this@PartsActivity, StagesActivity::class.java).apply {
                        putExtra("part", part)
                    })
                }
            }

            cardView.addView(button)
            partsContainer.addView(cardView)
        }
    }
}