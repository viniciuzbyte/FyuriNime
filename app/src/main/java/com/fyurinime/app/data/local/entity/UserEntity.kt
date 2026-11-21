package com.fyurinime.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserEntity(
    @PrimaryKey val id: Int = 0, // single-row table for current local user progress
    val username: String,
    val email: String,
    val source: String?,
    val xp: Int = 0,
    val level: Int = 1,
    val rankLabel: String = "Rookie",
    val totalWatchMinutes: Int = 0
)
