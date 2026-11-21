package com.fyurinime.app.domain.model

enum class Rank(val minLevel: Int, val label: String) {
    ROOKIE(1, "Rookie"),
    OTAKU(5, "Otaku"),
    VETERAN(10, "Veteran"),
    SENSEI(20, "Sensei"),
    LEGEND(35, "Legend");

    companion object {
        fun fromLevel(level: Int): Rank =
            entries.sortedByDescending { it.minLevel }.first { level >= it.minLevel }
    }
}
