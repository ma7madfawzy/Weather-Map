package com.app.weather.data.datasource.forecast

import com.app.weather.data.api.WeatherAppAPI
import com.app.weather.domain.model.ForecastResponse
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Fawzy
 */

class ForecastRemoteDataSource @Inject constructor(private val api: WeatherAppAPI) {

    fun getForecastByGeoCords(lat: Double, lon: Double, units: String): Single<ForecastResponse> = api.getForecastByGeoCords(
        lat,
        lon,
        units
    )
}
