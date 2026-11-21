package com.fyurinime.app.data.remote

import com.fyurinime.app.data.remote.dto.JikanAnimeDetailResponse
import com.fyurinime.app.data.remote.dto.JikanAnimeListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Jikan v4 — public, free, unofficial-but-legal REST wrapper around
 * MyAnimeList's public data. https://docs.api.jikan.moe/
 * No auth/scraping/anti-bot bypass required.
 */
interface JikanApi {

    @GET("v4/top/anime")
    suspend fun getTopAnime(
        @Query("page") page: Int = 1,
        @Query("filter") filter: String = "airing"
    ): JikanAnimeListResponse

    @GET("v4/seasons/now")
    suspend fun getSeasonNow(
        @Query("page") page: Int = 1
    ): JikanAnimeListResponse

    @GET("v4/anime")
    suspend fun searchAnime(
        @Query("q") query: String,
        @Query("page") page: Int = 1
    ): JikanAnimeListResponse

    @GET("v4/anime/{id}/full")
    suspend fun getAnimeDetail(@Path("id") id: Int): JikanAnimeDetailResponse
}
