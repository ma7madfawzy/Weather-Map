package com.app.weather.data.datasource.currentWeather

import com.app.weather.data.db.dao.CurrentWeatherDao
import com.app.weather.data.db.entity.CurrentWeatherEntity
import com.app.weather.domain.model.CurrentWeatherResponse
import javax.inject.Inject

/**
 * Created by Fawzy
 */

class CurrentWeatherLocalDataSource @Inject constructor(
    private val currentWeatherDao: CurrentWeatherDao
) {

    fun getCurrentWeather(lat: Double, lng: Double) = currentWeatherDao.getCurrentWeather(lat, lng)

    fun insertCurrentWeather(currentWeather: CurrentWeatherResponse, lat: Double, lon: Double) =
        currentWeatherDao.insertCurrentWeather(CurrentWeatherEntity(currentWeather,lat,lon))
}
