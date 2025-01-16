import android.content.SharedPreferences

object StageManager {
    private val unlockedStages = mutableSetOf<Pair<Int, Int>>() // (پارت, مرحله)
    private lateinit var sharedPreferences: SharedPreferences

    // تابع برای تنظیم SharedPreferences
    fun setSharedPreferences(prefs: SharedPreferences) {
        sharedPreferences = prefs
        loadUnlockedStages() // بارگذاری مراحل باز شده از SharedPreferences
    }

    // تابع برای ذخیره مراحل باز شده در SharedPreferences
    private fun saveUnlockedStages() {
        val editor = sharedPreferences.edit()
        val stagesString = unlockedStages.joinToString(",") { "${it.first}-${it.second}" }
        editor.putString("unlocked_stages", stagesString)
        editor.apply()
    }

    // تابع برای بارگذاری مراحل باز شده از SharedPreferences
    private fun loadUnlockedStages() {
        val stagesString = sharedPreferences.getString("unlocked_stages", "")
        if (!stagesString.isNullOrEmpty()) {
            val stages = stagesString.split(",")
            stages.forEach {
                val parts = it.split("-")
                if (parts.size == 2) {
                    unlockedStages.add(parts[0].toInt() to parts[1].toInt())
                }
            }
        } else {
            // اگر داده‌ای وجود نداشت، مرحله اول پارت اول رو باز کن
            unlockedStages.add(1 to 1)
        }
    }

    fun isStageUnlocked(part: Int, stage: Int): Boolean {
        return unlockedStages.contains(part to stage)
    }

    fun unlockNextStage(part: Int, currentStage: Int) {
        if (currentStage < 10) {
            unlockedStages.add(part to (currentStage + 1))
        } else if (part < 3) {
            unlockedStages.add((part + 1) to 1)
        }
        saveUnlockedStages() // ذخیره تغییرات در SharedPreferences
    }

    init {
        // مرحله اول پارت اول به طور پیش‌فرض باز است
        unlockedStages.add(1 to 1)
    }
}