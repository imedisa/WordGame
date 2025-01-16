package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Looper
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Locale

class GameActivity : AppCompatActivity() {
    private lateinit var wordToGuess: String
    private lateinit var revealedLetters: BooleanArray
    private lateinit var stageTitle: TextView
    private lateinit var wordTextView: TextView
    private lateinit var lettersGrid: GridLayout
    private lateinit var checkButton: Button
    private lateinit var timerTextView: TextView
    private lateinit var backButton: Button
    private lateinit var livesLayout: LinearLayout
    private lateinit var scoreTextView: TextView
    private lateinit var buyHintButton: Button
    private lateinit var database: DatabaseReference
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var sharedPreferences: SharedPreferences
    private var score = 0
    private var currentPart: Int = 1
    private var currentStage: Int = 1
    private var currentWord: String = ""
    private var selectedLetters: StringBuilder = StringBuilder()
    private var timeLeftInMillis: Long = 60000
    private var lives = 5
    private val heartDrawable = R.drawable.ic_heart_red
    private val hintCost = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        sharedPreferences = getSharedPreferences("game_prefs", MODE_PRIVATE)
        score = sharedPreferences.getInt("user_score", 0)

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
        timerTextView = findViewById(R.id.timerTextView)
        backButton = findViewById(R.id.backButton)
        livesLayout = findViewById(R.id.livesLayout)
        scoreTextView = findViewById(R.id.scoreTextView)
        buyHintButton = findViewById(R.id.buyHintButton)
        lettersGrid.columnCount = 4
        lettersGrid.rowCount = 2
        lettersGrid.useDefaultMargins = true
    }

    private fun setupGame() {
        currentPart = intent.getIntExtra("part", 1)
        currentStage = intent.getIntExtra("stage", 1)
        stageTitle.text = getString(R.string.stage_title, currentPart, currentStage)
        database = FirebaseDatabase.getInstance().reference
        setupStage(currentPart, currentStage)
        updateScore()
    }

    private fun updateScore() {
        scoreTextView.text = "امتیاز: $score"
    }

    private fun calculateScore() {
        score += lives * 10
        updateScore()
    }

    private fun setupButtons() {
        checkButton.setOnClickListener { checkAndCompleteStage() }
        backButton.setOnClickListener { goBackToStages() }
        buyHintButton.setOnClickListener { buyHint() }
    }

    private fun buyHint() {
        if (score >= hintCost) {
            score -= hintCost
            sharedPreferences.edit().putInt("user_score", score).apply()
            updateScore()
            applyHint()
            Toast.makeText(this, "سرنخ اعمال شد! امتیاز شما: $score", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "امتیاز کافی ندارید!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun applyHint() {
        val hiddenIndices = revealedLetters.withIndex()
            .filter { !it.value }
            .map { it.index }

        if (hiddenIndices.isNotEmpty()) {
            val randomIndex = hiddenIndices.random()
            revealedLetters[randomIndex] = true
            updateWordDisplay()
        } else {
            Toast.makeText(this, "همه حروف باز شده‌اند!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateWordDisplay() {
        val displayedWord = StringBuilder()
        for (i in wordToGuess.indices) {
            if (revealedLetters[i]) {
                displayedWord.append(wordToGuess[i])
            } else {
                displayedWord.append("_")
            }
            displayedWord.append(" ")
        }
        wordTextView.text = displayedWord.toString().trim()
    }

    private fun setupStage(part: Int, stage: Int) {
        currentWord = getWordForStage(part, stage)
        wordToGuess = currentWord
        revealedLetters = BooleanArray(wordToGuess.length) { false }
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

    private fun getWordForStage(part: Int, stage: Int): String {
        return when (part) {
            1 -> when (stage) { // پارت ۱: طبیعت و حیوانات
                1 -> "درخت"
                2 -> "اسمان"
                3 -> "آسمان"
                4 -> "دریا"
                5 -> "اشغال"
                6 -> "ستاره"
                7 -> "حیوان"
                8 -> "پرنده"
                9 -> "گلستان"
                10 -> "جنگل"
                else -> "کلمه پیش‌فرض"
            }
            2 -> when (stage) { // پارت ۲: فناوری و کامپیوتر
                1 -> "موبایل"
                2 -> "روزنامه"
                3 -> "برنامه"
                4 -> "اینترنت"
                5 -> "دیتابیس"
                6 -> "سرور"
                7 -> "شبکه"
                8 -> "الگوریتم"
                9 -> "هوش مصنوعی"
                10 -> "ربات"
                else -> "کلمه پیش‌فرض"
            }
            3 -> when (stage) { // پارت ۳: غذاها و نوشیدنی‌ها
                1 -> "پیتزا"
                2 -> "همبرگر"
                3 -> "سالاد"
                4 -> "آبمیوه"
                5 -> "خوراکی"
                6 -> "بادمجان"
                7 -> "قهوه"
                8 -> "ساندویچ"
                9 -> "بستنی"
                10 -> "کباب"
                else -> "کلمه پیش‌فرض"
            }
            else -> "کلمه پیش‌فرض"
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

    private fun checkAndCompleteStage() {
        if (selectedLetters.toString() == currentWord) {
            calculateScore()
            showCorrectAnimation()
            resetTextViewStyle()
            Toast.makeText(this, "کلمه درست است!", Toast.LENGTH_SHORT).show()
            saveProgress(currentPart, currentStage)

            val nextStage = currentStage + 1
            if (nextStage <= 10) {
                StageManager.unlockNextStage(currentPart, currentStage)
                Toast.makeText(this, "مرحله $currentStage تکمیل شد! مرحله $nextStage باز شد.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, StagesActivity::class.java).apply {
                    putExtra("part", currentPart)
                })
            } else if (currentPart < 3) {
                StageManager.unlockNextStage(currentPart, currentStage)
                Toast.makeText(this, "پارت $currentPart تکمیل شد! پارت ${currentPart + 1} باز شد.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, StagesActivity::class.java).apply {
                    putExtra("part", currentPart + 1)
                })
            } else {
                Toast.makeText(this, "تبریک! شما همه مراحل را کامل کرده‌اید.", Toast.LENGTH_SHORT).show()
            }
            finish()
        } else {
            showIncorrectAnimation()
            resetTextViewStyle()
            loseLife()
            Toast.makeText(this, "کلمه نادرست است!", Toast.LENGTH_SHORT).show()
            selectedLetters.clear()
            wordTextView.text = "_".repeat(currentWord.length)
        }
    }

    private fun showCorrectAnimation() {
        wordTextView.setTextColor(Color.parseColor("#4CAF50"))

        android.os.Handler(Looper.getMainLooper()).postDelayed({
            val scaleAnimation = ScaleAnimation(
                1f, 1.2f,
                1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = 300
                repeatCount = 1
                repeatMode = Animation.REVERSE
            }

            wordTextView.startAnimation(scaleAnimation)
        }, 100)
    }

    private fun showIncorrectAnimation() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.incorrect_animation)
        wordTextView.startAnimation(animation)
        wordTextView.setBackgroundColor(Color.parseColor("#FF5252"))
        wordTextView.setTextColor(Color.WHITE)
    }

    private fun resetTextViewStyle() {
        wordTextView.setBackgroundColor(Color.TRANSPARENT)
        wordTextView.setTextColor(Color.BLACK)
    }

    private fun loseLife() {
        if (lives > 0) {
            lives--
            setupLives()
            if (lives == 0) {
                Toast.makeText(this, "جان‌های شما تمام شد!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun saveProgress(part: Int, stage: Int) {
        val userId = "user1"
        database.child("users").child(userId).child("unlockedStages").setValue("$part-$stage")
        database.child("users").child(userId).child("score").setValue(score)
        sharedPreferences.edit().putInt("user_score", score).apply()
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
        val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        timerTextView.text = timeFormatted
    }

    private fun goBackToStages() {
        startActivity(Intent(this, StagesActivity::class.java).apply {
            putExtra("part", currentPart)
        })
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }
}