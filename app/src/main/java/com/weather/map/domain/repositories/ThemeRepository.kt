package com.weather.map.domain.repositories

import kotlinx.coroutines.flow.Flow


interface ThemeRepository {

    suspend fun getWhichTheme(): Flow<Int>
    suspend fun setWhichTheme(which: Int)
}