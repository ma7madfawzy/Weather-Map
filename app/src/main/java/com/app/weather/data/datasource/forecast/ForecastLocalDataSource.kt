package com.app.weather.data.datasource.forecast

import com.app.weather.data.db.dao.ForecastDao
import com.app.weather.data.db.entity.ForecastEntity
import com.app.weather.domain.model.ForecastResponse
import javax.inject.Inject

/**
 * Created by Fawzy
 */

class ForecastLocalDataSource @Inject constructor(private val forecastDao: ForecastDao) {

    fun getForecast() = forecastDao.getForecast()

    fun insertForecast(forecast: ForecastResponse) = forecastDao.deleteAndInsert(
        ForecastEntity(forecast)
    )
}
