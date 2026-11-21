package com.fyurinime.app.data.repository

import com.fyurinime.app.data.remote.JikanApi
import com.fyurinime.app.data.remote.dto.JikanAnimeDto
import com.fyurinime.app.domain.model.Anime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeRepository @Inject constructor(
    private val api: JikanApi
) {
    suspend fun getTrending(): List<Anime> =
        api.getTopAnime().data.map { it.toDomain() }

    suspend fun getSeasonNow(): List<Anime> =
        api.getSeasonNow().data.map { it.toDomain() }

    suspend fun search(query: String): List<Anime> =
        api.searchAnime(query).data.map { it.toDomain() }

    suspend fun getDetail(malId: Int): Anime =
        api.getAnimeDetail(malId).data.toDomain()

    private fun JikanAnimeDto.toDomain() = Anime(
        malId = malId,
        title = title,
        synopsis = synopsis,
        imageUrl = images?.jpg?.largeImageUrl ?: images?.jpg?.imageUrl,
        score = score,
        year = year,
        status = status,
        genres = genres?.map { it.name } ?: emptyList()
    )
}
