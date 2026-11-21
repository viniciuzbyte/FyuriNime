package com.fyurinime.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fyurinime.app.data.local.entity.AchievementEntity
import com.fyurinime.app.data.local.entity.WatchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievements")
    fun observeAll(): Flow<List<AchievementEntity>>

    @Query("SELECT achievementId FROM achievements")
    suspend fun getUnlockedIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(achievement: AchievementEntity)

    @Insert
    suspend fun insertWatchHistory(entry: WatchHistoryEntity)

    @Query("SELECT COALESCE(SUM(minutesWatched), 0) FROM watch_history")
    suspend fun getTotalWatchedMinutes(): Int
}
