package com.fyurinime.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watch_history")
data class WatchHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val animeMalId: Int,
    val animeTitle: String,
    val minutesWatched: Int,
    val watchedAtMillis: Long
)
