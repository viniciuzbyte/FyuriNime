package com.fyurinime.app.domain.model

data class UserProgress(
    val xp: Int = 0,
    val level: Int = 1,
    val rank: Rank = Rank.ROOKIE,
    val totalWatchMinutes: Int = 0
)

data class Profile(
    val username: String,
    val email: String,
    val source: String?,
    val progress: UserProgress
)
