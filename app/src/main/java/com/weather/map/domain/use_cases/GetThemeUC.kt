package com.weather.map.domain.use_cases

import kotlinx.coroutines.flow.Flow

interface GetThemeUC {
    suspend operator fun invoke(): Flow<Int>
}