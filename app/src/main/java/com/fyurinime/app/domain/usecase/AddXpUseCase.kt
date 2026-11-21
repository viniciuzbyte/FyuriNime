package com.fyurinime.app.domain.usecase

import com.fyurinime.app.domain.model.Rank
import com.fyurinime.app.domain.model.UserProgress
import kotlin.math.floor
import kotlin.math.sqrt
import javax.inject.Inject

/**
 * Handles XP gain and level-up curve.
 * 1 minute watched = 1 XP. Level requires level*100 cumulative XP (simple curve).
 */
class AddXpUseCase @Inject constructor() {

    operator fun invoke(current: UserProgress, minutesWatched: Int): UserProgress {
        val newXp = current.xp + minutesWatched
        val newLevel = levelForXp(newXp)
        val newRank = Rank.fromLevel(newLevel)
        return current.copy(
            xp = newXp,
            level = newLevel,
            rank = newRank,
            totalWatchMinutes = current.totalWatchMinutes + minutesWatched
        )
    }

    private fun levelForXp(xp: Int): Int {
        // inverse of triangular level curve: xp = 50 * level * (level-1)
        val level = floor((1 + sqrt(1.0 + xp / 12.5)) / 2).toInt()
        return level.coerceAtLeast(1)
    }
}
