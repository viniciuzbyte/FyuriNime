package com.fyurinime.app.domain.model

data class Anime(
    val malId: Int,
    val title: String,
    val synopsis: String?,
    val imageUrl: String?,
    val score: Double?,
    val year: Int?,
    val status: String?,
    val genres: List<String>
)
