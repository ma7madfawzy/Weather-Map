package com.weather.map.domain.use_cases

import com.weather.map.domain.repositories.ThemeRepository

class SetThemeUCImpl(private val repo: ThemeRepository) : SetThemeUC {
    override suspend operator fun invoke(which: Int) = repo.setWhichTheme(which)

}