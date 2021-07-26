package com.weather.map.data.repositories

import com.weather.map.data.data_sources.local.ThemeLocalDataSource
import com.weather.map.domain.repositories.ThemeRepository


/**
 * Class that requests token on the remote data source.
 */

class ThemeRepositoryImpl(private val dataSource: ThemeLocalDataSource) : ThemeRepository {

    override suspend fun getWhichTheme() = dataSource.getWhichTheme()

    override suspend fun setWhichTheme(which: Int) = dataSource.setWhichTheme(which)

}