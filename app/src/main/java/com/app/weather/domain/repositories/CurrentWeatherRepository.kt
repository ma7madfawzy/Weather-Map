package com.app.weather.domain.repositories

import androidx.lifecycle.LiveData
import com.app.weather.data.db.entity.CurrentWeatherEntity
import com.app.weather.domain.common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Fawzy
 */

interface CurrentWeatherRepository {
    fun loadCurrentWeatherByGeoCords(
        lat: Double,
        lon: Double,
        fetchRequired: Boolean,
        units: String
    ): Flow<Resource<CurrentWeatherEntity>>
}
