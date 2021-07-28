package com.app.weather.domain.repositories

import androidx.lifecycle.LiveData
import com.app.weather.data.db.entity.CurrentWeatherEntity
import com.app.weather.utils.domain.Resource

/**
 * Created by Fawzy
 */

interface CurrentWeatherRepository {


    fun loadCurrentWeatherByGeoCords(
        lat: Double,
        lon: Double,
        fetchRequired: Boolean,
        units: String
    ): LiveData<Resource<CurrentWeatherEntity>>
}
