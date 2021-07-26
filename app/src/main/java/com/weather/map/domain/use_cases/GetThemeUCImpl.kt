package com.weather.map.domain.use_cases

import com.weather.map.domain.repositories.ThemeRepository

class GetThemeUCImpl(private val repo: ThemeRepository) : GetThemeUC {
    override suspend operator fun invoke() = repo.getWhichTheme()

}