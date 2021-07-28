package com.app.weather.presentation.dashboard

import com.app.weather.presentation.core.BaseViewState
import com.app.weather.data.db.entity.ForecastEntity
import com.app.weather.utils.domain.Status

/**
 * Created by Fawzy
 */

class ForecastViewState(
    val status: Status,
    val error: String? = null,
    val data: ForecastEntity? = null
) : BaseViewState(status, error) {
    fun getForecast() = data
}
