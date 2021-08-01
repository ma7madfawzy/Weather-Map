package com.app.weather.domain.usecase

import com.app.weather.data.db.entity.ForecastEntity
import com.app.weather.domain.common.Resource
import com.app.weather.domain.repositories.ForecastRepository
import com.app.weather.presentation.core.BaseViewState
import com.app.weather.presentation.core.Constants
import com.app.weather.presentation.dashboard.ForecastMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Fawzy
 */

class ForecastUseCase @Inject internal constructor(private val repository: ForecastRepository) {

    operator fun invoke(
        params: CurrentWeatherUseCase.CurrentWeatherParams?)
    : Flow<BaseViewState<ForecastEntity>> {
        return repository.loadForecast(
            params?.lat ?: 0.0,
            params?.lon ?: 0.0,
            params?.fetchRequired
                ?: false,
            units = params?.units ?: Constants.Coords.METRIC
        ).map {
            BaseViewState(it)
        }
    }
}
