package com.fyurinime.app.domain.model

enum class AchievementType(
    val id: String,
    val title: String,
    val description: String,
    val thresholdMinutes: Int
) {
    FIRST_STEP("first_step", "First Step", "Tonton 40 menit pertamamu", 40),
    BINGE_STARTER("binge_starter", "Binge Starter", "Total 5 jam waktu tonton", 300),
    DEDICATED_FAN("dedicated_fan", "Dedicated Fan", "Total 20 jam waktu tonton", 1200),
    MARATHONER("marathoner", "Marathoner", "Total 50 jam waktu tonton", 3000)
}

data class Achievement(
    val type: AchievementType,
    val unlocked: Boolean,
    val unlockedAtMillis: Long? = null
)
