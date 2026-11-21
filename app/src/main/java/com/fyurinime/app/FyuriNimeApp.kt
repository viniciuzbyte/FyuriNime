package com.fyurinime.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Fyuri Nime - Application entry point.
 * Metadata is sourced from Jikan API (public, legal MyAnimeList wrapper).
 * No scraping or unauthorized streaming endpoints are used in this app.
 */
@HiltAndroidApp
class FyuriNimeApp : Application()
