package com.app.weather.data.datasource.forecast

import com.app.weather.data.api.WeatherAppAPI
import com.app.weather.domain.model.ForecastResponse
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Fawzy
 */

class ForecastRemoteDataSource @Inject constructor(private val api: WeatherAppAPI) {

    fun getForecast(lat: Double, lon: Double, units: String)= api.getForecastByGeoCords(
        lat,
        lon,
        units
    )
}
