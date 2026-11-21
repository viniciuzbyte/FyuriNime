package com.fyurinime.app.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JikanAnimeListResponse(
    @Json(name = "data") val data: List<JikanAnimeDto>
)

@JsonClass(generateAdapter = true)
data class JikanAnimeDetailResponse(
    @Json(name = "data") val data: JikanAnimeDto
)

@JsonClass(generateAdapter = true)
data class JikanAnimeDto(
    @Json(name = "mal_id") val malId: Int,
    @Json(name = "title") val title: String,
    @Json(name = "synopsis") val synopsis: String?,
    @Json(name = "score") val score: Double?,
    @Json(name = "year") val year: Int?,
    @Json(name = "status") val status: String?,
    @Json(name = "images") val images: JikanImagesDto?,
    @Json(name = "genres") val genres: List<JikanGenreDto>?
)

@JsonClass(generateAdapter = true)
data class JikanImagesDto(
    @Json(name = "jpg") val jpg: JikanImageUrlDto?
)

@JsonClass(generateAdapter = true)
data class JikanImageUrlDto(
    @Json(name = "large_image_url") val largeImageUrl: String?,
    @Json(name = "image_url") val imageUrl: String?
)

@JsonClass(generateAdapter = true)
data class JikanGenreDto(
    @Json(name = "name") val name: String
)
