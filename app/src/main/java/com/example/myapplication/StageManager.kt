object StageManager {
    private val unlockedStages = mutableSetOf<Pair<Int, Int>>() // (پارت, مرحله)
    private const val STAGES_PER_PART = 10 // تعداد مراحل در هر پارت

    // بررسی آیا مرحله باز است یا خیر
    fun isStageUnlocked(part: Int, stage: Int): Boolean {
        return unlockedStages.contains(part to stage)
    }

    // باز کردن مرحله بعدی در یک پارت
    fun unlockNextStage(part: Int, currentStage: Int) {
        if (currentStage < STAGES_PER_PART) {
            unlockedStages.add(part to (currentStage + 1))
        } else if (part < 3) {
            // اگر آخرین مرحله یک پارت کامل شد، پارت بعدی باز شود
            unlockedStages.add((part + 1) to 1)
        }
    }

    // دریافت لیست مراحل باز شده در یک پارت
    fun getUnlockedStages(part: Int): List<Int> {
        return unlockedStages.filter { it.first == part }.map { it.second }
    }

    // باز کردن مرحله اول پارت اول به طور پیش‌فرض
    init {
        unlockedStages.add(1 to 1)
    }
}