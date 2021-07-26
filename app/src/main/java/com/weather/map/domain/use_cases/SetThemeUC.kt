package com.weather.map.domain.use_cases

interface SetThemeUC {
    suspend operator fun invoke(which: Int)
}