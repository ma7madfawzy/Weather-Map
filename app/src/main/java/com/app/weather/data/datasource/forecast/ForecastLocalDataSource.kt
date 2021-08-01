package com.app.weather.data.datasource.forecast

import com.app.weather.data.db.dao.ForecastDao
import com.app.weather.data.db.entity.ForecastEntity
import com.app.weather.domain.model.ForecastResponse
import javax.inject.Inject

/**
 * Created by Fawzy
 */

class ForecastLocalDataSource @Inject constructor(private val forecastDao: ForecastDao) {

    fun getForecast(lat: Double, lng: Double) = forecastDao.getForecast(lat,lng)

    fun insertForecast(forecast: ForecastResponse, lat: Double, lng: Double) =
        forecastDao.insertForecast(ForecastEntity(forecast, lat, lng))
}
