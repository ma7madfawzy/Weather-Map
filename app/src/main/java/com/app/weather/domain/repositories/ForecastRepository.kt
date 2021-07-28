package com.app.weather.domain.repositories

import androidx.lifecycle.LiveData
import com.app.weather.data.db.entity.ForecastEntity
import com.app.weather.utils.domain.Resource

/**
 * Created by Fawzy
 */

interface ForecastRepository {

    fun loadForecastByCoord(
        lat: Double,
        lon: Double,
        fetchRequired: Boolean,
        units: String
    ): LiveData<Resource<ForecastEntity>>
}
