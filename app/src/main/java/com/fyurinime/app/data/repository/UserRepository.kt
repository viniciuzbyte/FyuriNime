package com.fyurinime.app.data.repository

import com.fyurinime.app.data.local.dao.AchievementDao
import com.fyurinime.app.data.local.dao.UserDao
import com.fyurinime.app.data.local.entity.AchievementEntity
import com.fyurinime.app.data.local.entity.UserEntity
import com.fyurinime.app.data.local.entity.WatchHistoryEntity
import com.fyurinime.app.domain.model.Achievement
import com.fyurinime.app.domain.model.AchievementType
import com.fyurinime.app.domain.model.Rank
import com.fyurinime.app.domain.model.UserProgress
import com.fyurinime.app.domain.usecase.AddXpUseCase
import com.fyurinime.app.domain.usecase.CheckAchievementUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val achievementDao: AchievementDao,
    private val addXpUseCase: AddXpUseCase,
    private val checkAchievementUseCase: CheckAchievementUseCase
) {
    fun observeProgress(): Flow<UserProgress> =
        userDao.observeUser().map { entity ->
            UserProgress(
                xp = entity?.xp ?: 0,
                level = entity?.level ?: 1,
                rank = rankFromLabel(entity?.rankLabel ?: Rank.ROOKIE.label),
                totalWatchMinutes = entity?.totalWatchMinutes ?: 0
            )
        }

    fun observeAchievements(): Flow<List<Achievement>> =
        achievementDao.observeAll().map { unlocked ->
            val unlockedById = unlocked.associateBy { it.achievementId }
            AchievementType.entries.map { type ->
                val entry = unlockedById[type.id]
                Achievement(type = type, unlocked = entry != null, unlockedAtMillis = entry?.unlockedAtMillis)
            }
        }

    suspend fun createLocalAccount(username: String, email: String) {
        userDao.upsert(
            UserEntity(id = 0, username = username, email = email, source = null)
        )
    }

    suspend fun setSource(source: String) {
        val current = userDao.getUser() ?: return
        userDao.update(current.copy(source = source))
    }

    suspend fun updateUsername(username: String) {
        val current = userDao.getUser() ?: return
        userDao.update(current.copy(username = username))
    }

    /** Call when a watch session ends. Grants XP, persists history, unlocks achievements. */
    suspend fun recordWatchSession(animeMalId: Int, animeTitle: String, minutesWatched: Int) {
        if (minutesWatched <= 0) return

        achievementDao.insertWatchHistory(
            WatchHistoryEntity(
                animeMalId = animeMalId,
                animeTitle = animeTitle,
                minutesWatched = minutesWatched,
                watchedAtMillis = System.currentTimeMillis()
            )
        )

        val current = userDao.getUser() ?: return
        val currentProgress = UserProgress(
            xp = current.xp,
            level = current.level,
            rank = rankFromLabel(current.rankLabel),
            totalWatchMinutes = current.totalWatchMinutes
        )
        val updated = addXpUseCase(currentProgress, minutesWatched)
        userDao.update(
            current.copy(
                xp = updated.xp,
                level = updated.level,
                rankLabel = updated.rank.label,
                totalWatchMinutes = updated.totalWatchMinutes
            )
        )

        val unlockedIds = achievementDao.getUnlockedIds().toSet()
        val newlyUnlocked = checkAchievementUseCase(updated.totalWatchMinutes, unlockedIds)
        newlyUnlocked.forEach { achievement ->
            achievementDao.insert(
                AchievementEntity(
                    achievementId = achievement.type.id,
                    unlockedAtMillis = achievement.unlockedAtMillis ?: System.currentTimeMillis()
                )
            )
        }
    }

    private fun rankFromLabel(label: String): Rank =
        Rank.entries.firstOrNull { it.label == label } ?: Rank.ROOKIE
}
