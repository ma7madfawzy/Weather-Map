package com.app.weather.presentation.dashboard

import com.app.weather.presentation.core.BaseViewState
import com.app.weather.data.db.entity.CurrentWeatherEntity
import com.app.weather.utils.domain.Status

/**
 * Created by Fawzy
 */

class CurrentWeatherViewState(
    val status: Status,
    val error: String? = null,
    val data: CurrentWeatherEntity? = null
) : BaseViewState(status, error) {
    fun getForecast() = data
}
