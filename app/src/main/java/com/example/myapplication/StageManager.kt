package com.example.myapplication

object StageManager {
    private val unlockedStages = mutableSetOf(1) // مرحله ۱ ابتدا باز است

    // بررسی آیا مرحله باز است یا خیر
    fun isStageUnlocked(stage: Int): Boolean {
        return unlockedStages.contains(stage)
    }

    // باز کردن مرحله بعدی
    fun unlockNextStage(currentStage: Int) {
        unlockedStages.add(currentStage + 1)
    }

    // دریافت لیست مراحل باز شده
    fun getUnlockedStages(): List<Int> {
        return unlockedStages.toList()
    }
}