package com.fyurinime.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fyurinime.app.data.local.dao.AchievementDao
import com.fyurinime.app.data.local.dao.UserDao
import com.fyurinime.app.data.local.entity.AchievementEntity
import com.fyurinime.app.data.local.entity.UserEntity
import com.fyurinime.app.data.local.entity.WatchHistoryEntity

@Database(
    entities = [UserEntity::class, AchievementEntity::class, WatchHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun achievementDao(): AchievementDao

    companion object {
        const val DATABASE_NAME = "fyurinime.db"
    }
}
