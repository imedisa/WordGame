package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
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

    private lateinit var stageTitle: TextView
    private lateinit var wordTextView: TextView
    private lateinit var lettersGrid: GridLayout
    private lateinit var checkButton: Button
    private lateinit var timerTextView: TextView
    private lateinit var backButton: Button
    private lateinit var livesLayout: LinearLayout
    private lateinit var database: DatabaseReference
    private lateinit var countDownTimer: CountDownTimer

    private var currentPart: Int = 1
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
        timerTextView = findViewById(R.id.timerTextView)
        backButton = findViewById(R.id.backButton)
        livesLayout = findViewById(R.id.livesLayout)

        lettersGrid.columnCount = 4
        lettersGrid.rowCount = 2
        lettersGrid.useDefaultMargins = true
    }

    private fun setupGame() {
        currentPart = intent.getIntExtra("part", 1)
        currentStage = intent.getIntExtra("stage", 1)
        stageTitle.text = getString(R.string.stage_title, currentPart, currentStage) // رفع خطای format string
        database = FirebaseDatabase.getInstance().reference
        setupStage(currentPart, currentStage)
    }

    private fun setupButtons() {
        checkButton.setOnClickListener { checkAndCompleteStage() }
        backButton.setOnClickListener { goBackToStages() }
    }

    private fun setupStage(part: Int, stage: Int) {
        currentWord = getWordForStage(part, stage)
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
            1 -> when (stage) {
                1 -> "سلام"
                2 -> "کتاب"
                3 -> "مدرسه"
                4 -> "دانشگاه"
                5 -> "استاد"
                6 -> "دانشجو"
                7 -> "کلاس"
                8 -> "درس"
                9 -> "نمره"
                10 -> "پروژه"
                else -> "کلمه پیش‌فرض"
            }
            2 -> when (stage) {
                1 -> "برنامه"
                2 -> "کامپیوتر"
                3 -> "اندروید"
                4 -> "کدنویسی"
                5 -> "الگوریتم"
                6 -> "دیتابیس"
                7 -> "سرور"
                8 -> "شبکه"
                9 -> "اینترنت"
                10 -> "هوش مصنوعی"
                else -> "کلمه پیش‌فرض"
            }
            3 -> when (stage) {
                1 -> "فروشگاه"
                2 -> "خرید"
                3 -> "فروش"
                4 -> "محصول"
                5 -> "مشتری"
                6 -> "پرداخت"
                7 -> "تخفیف"
                8 -> "تحویل"
                9 -> "فروشنده"
                10 -> "خریدار"
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
            showCorrectAnimation() // نمایش انیمیشن پاسخ درست
            resetTextViewStyle() // بازنشانی استایل TextView
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
        // انیمیشن بزرگ‌شدن متن
        val scaleAnimation = android.view.animation.ScaleAnimation(
            1f, 1.2f, // مقیاس X از ۱ به ۱.۲
            1f, 1.2f, // مقیاس Y از ۱ به ۱.۲
            Animation.RELATIVE_TO_SELF, 0.5f, // نقطه محور X (وسط)
            Animation.RELATIVE_TO_SELF, 0.5f  // نقطه محور Y (وسط)
        ).apply {
            duration = 300 // مدت زمان انیمیشن (میلی‌ثانیه)
            repeatCount = 1 // تعداد تکرار
            repeatMode = Animation.REVERSE // بازگشت به حالت اولیه

            // اضافه کردن Listener برای تغییر رنگ متن پس از اتمام انیمیشن
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // قبل از شروع انیمیشن کاری انجام ندهید
                }

                override fun onAnimationEnd(animation: Animation?) {
                    // پس از اتمام انیمیشن، رنگ متن را به سبز تغییر دهید
                    wordTextView.setTextColor(Color.parseColor("#4CAF50")) // سبز
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // در صورت تکرار انیمیشن کاری انجام ندهید
                }
            })
        }

        // اعمال انیمیشن به TextView
        wordTextView.startAnimation(scaleAnimation)
    }
    private fun showIncorrectAnimation() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.incorrect_animation)
        wordTextView.startAnimation(animation)
        wordTextView.setBackgroundColor(Color.parseColor("#FF5252")) // قرمز
        wordTextView.setTextColor(Color.WHITE) // متن سفید
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