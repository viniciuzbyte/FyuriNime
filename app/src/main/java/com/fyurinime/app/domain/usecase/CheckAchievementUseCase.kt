package com.fyurinime.app.domain.usecase

import com.fyurinime.app.domain.model.Achievement
import com.fyurinime.app.domain.model.AchievementType
import javax.inject.Inject

/**
 * Determines which achievements should newly unlock given total watch minutes.
 */
class CheckAchievementUseCase @Inject constructor() {

    operator fun invoke(
        totalWatchMinutes: Int,
        alreadyUnlockedIds: Set<String>
    ): List<Achievement> {
        return AchievementType.entries
            .filter { totalWatchMinutes >= it.thresholdMinutes && it.id !in alreadyUnlockedIds }
            .map { Achievement(type = it, unlocked = true, unlockedAtMillis = System.currentTimeMillis()) }
    }
}
