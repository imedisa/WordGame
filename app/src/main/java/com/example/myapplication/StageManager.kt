object StageManager {
    private val unlockedStages = mutableSetOf<Pair<Int, Int>>() // (پارت, مرحله)
    private var userScore = 0 // متغیر برای ذخیره امتیاز کاربر

    fun isStageUnlocked(part: Int, stage: Int): Boolean {
        return unlockedStages.contains(part to stage)
    }

    fun unlockNextStage(part: Int, currentStage: Int) {
        if (currentStage < 10) {
            unlockedStages.add(part to (currentStage + 1))
        } else if (part < 3) {
            unlockedStages.add((part + 1) to 1)
        }
    }

    fun getScore(): Int {
        return userScore
    }

    fun setScore(score: Int) {
        userScore = score
    }

    init {
        unlockedStages.add(1 to 1) // مرحله اول پارت اول به طور پیش‌فرض باز است
    }
}