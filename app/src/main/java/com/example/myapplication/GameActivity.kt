
// 3. GameActivity.kt
package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.view.ViewGroup
import android.graphics.Color

class GameActivity : AppCompatActivity() {

    private lateinit var stageTitle: TextView
    private lateinit var wordTextView: TextView
    private lateinit var lettersGrid: GridLayout
    private lateinit var checkButton: Button
    private lateinit var clearButton: Button
    private lateinit var timerTextView: TextView
    private lateinit var backButton: Button
    private lateinit var livesLayout: LinearLayout
    private lateinit var database: DatabaseReference
    private lateinit var countDownTimer: CountDownTimer

    private var currentStage: Int = 1
    private var currentWord: String = ""
    private var selectedLetters: StringBuilder = StringBuilder()
    private var timeLeftInMillis: Long = 60000
    private var lives = 5
    private val heartDrawable = R.drawable.ic_heart_red

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        initializeViews()
        setupGame()
        setupButtons()
        startTimer()
        setupLives()
    }

    private fun initializeViews() {
        stageTitle = findViewById(R.id.stageTitle)
        wordTextView = findViewById(R.id.wordTextView)
        lettersGrid = findViewById(R.id.lettersGrid)
        checkButton = findViewById(R.id.checkButton)
        clearButton = findViewById(R.id.clearButton)
        timerTextView = findViewById(R.id.timerTextView)
        backButton = findViewById(R.id.backButton)
        livesLayout = findViewById(R.id.livesLayout)

        lettersGrid.columnCount = 4
        lettersGrid.rowCount = 2
        lettersGrid.useDefaultMargins = true
    }

    private fun setupGame() {
        currentStage = intent.getIntExtra("stage", 1)
        stageTitle.text = getString(R.string.stage_title, currentStage)
        database = FirebaseDatabase.getInstance().reference
        setupStage(currentStage)
    }

    private fun setupButtons() {
        checkButton.setOnClickListener { checkAndCompleteStage() }
        clearButton.setOnClickListener { clearLastLetter() }
        backButton.setOnClickListener { goBackToStages() }
    }

    private fun setupStage(stage: Int) {
        currentWord = when (stage) {
            1 -> "سلام"
            2 -> "استاد"
            3 -> "مهربان"
            4 -> "نمره"
            5 -> "خوب"
            6 -> "بخشیدن"
            7 -> "ممنون"
            8 -> "خواهشمندم"
            9 -> "متشکرم"
            10 -> "اندروید"
            else -> "ممنون"
        }

        wordTextView.text = "_".repeat(currentWord.length)
        lettersGrid.removeAllViews()

        val shuffledLetters = currentWord.toList().shuffled()

        for (letter in shuffledLetters) {
            val button = Button(this).apply {
                text = letter.toString()
                textSize = 18f

                val params = GridLayout.LayoutParams().apply {
                    width = ViewGroup.LayoutParams.WRAP_CONTENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    setMargins(8, 8, 8, 8)
                }
                layoutParams = params

                setBackgroundColor(Color.parseColor("#2196F3"))
                setTextColor(Color.WHITE)
                setPadding(16, 16, 16, 16)

                setOnClickListener { addLetter(letter.toString()) }
            }
            lettersGrid.addView(button)
        }
    }

    private fun setupLives() {
        livesLayout.removeAllViews()

        for (i in 1..lives) {
            val heartImageView = ImageView(this).apply {
                setImageResource(heartDrawable)
                layoutParams = LinearLayout.LayoutParams(
                    dpToPx(30),
                    dpToPx(30)
                ).apply {
                    setMargins(dpToPx(4), 0, dpToPx(4), 0)
                }
            }
            livesLayout.addView(heartImageView)
        }
    }

    private fun dpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    private fun addLetter(letter: String) {
        if (selectedLetters.length < currentWord.length) {
            selectedLetters.append(letter)
            wordTextView.text = selectedLetters.toString()
        } else {
            Toast.makeText(this, "تعداد حروف به حداکثر رسیده است", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearLastLetter() {
        if (selectedLetters.isNotEmpty()) {
            selectedLetters.deleteCharAt(selectedLetters.length - 1)
            wordTextView.text = if (selectedLetters.isEmpty()) {
                "_".repeat(currentWord.length)
            } else {
                selectedLetters.toString()
            }
        }
    }

    private fun checkAndCompleteStage() {
        if (selectedLetters.toString() == currentWord) {
            Toast.makeText(this, "کلمه درست است!", Toast.LENGTH_SHORT).show()
            saveProgress(currentStage)

            val nextStage = currentStage + 1
            if (nextStage <= 10) {
                StageManager.unlockNextStage(currentStage)
                Toast.makeText(this, "مرحله $currentStage تکمیل شد! مرحله $nextStage باز شد.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, StagesActivity::class.java))
            } else {
                Toast.makeText(this, "تبریک! شما همه مراحل را کامل کرده‌اید.", Toast.LENGTH_SHORT).show()
            }
            finish()
        } else {
            loseLife()
            Toast.makeText(this, "کلمه نادرست است!", Toast.LENGTH_SHORT).show()
            selectedLetters.clear()
            wordTextView.text = "_".repeat(currentWord.length)
        }
    }

    private fun loseLife() {
        if (lives > 0) {
            lives--
            setupLives()  // بازسازی نمایش قلب‌ها
            if (lives == 0) {
                Toast.makeText(this, "جان‌های شما تمام شد!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun saveProgress(stage: Int) {
        val userId = "user1"
        database.child("users").child(userId).child("unlockedStages").setValue(stage)
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
                timeLeftInMillis = 0
                updateTimer()
                Toast.makeText(this@GameActivity, "زمان شما به پایان رسید!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.start()
    }

    private fun updateTimer() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val timeFormatted = String.format("%02d:%02d", minutes, seconds)
        timerTextView.text = timeFormatted
    }

    private fun goBackToStages() {
        startActivity(Intent(this, StagesActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }
}