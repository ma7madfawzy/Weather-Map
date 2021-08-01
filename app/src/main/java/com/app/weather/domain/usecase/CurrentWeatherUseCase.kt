package com.app.weather.domain.usecase

import com.app.weather.data.db.entity.CurrentWeatherEntity
import com.app.weather.domain.repositories.CurrentWeatherRepository
import com.app.weather.presentation.core.BaseViewState
import com.app.weather.presentation.core.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Fawzy
 */

class CurrentWeatherUseCase @Inject internal constructor(
    private val repository: CurrentWeatherRepository
) {

    operator fun invoke(params: CurrentWeatherParams?): Flow<BaseViewState<CurrentWeatherEntity>> {
        return repository.loadCurrentWeatherByGeoCords(
            params?.lat ?: 0.0,
            params?.lon ?: 0.0,
            params?.fetchRequired
                ?: false,
            units = params?.units ?: Constants.Coords.METRIC
        ).map {
            BaseViewState(it)
        }
    }


    class CurrentWeatherParams(
        val lat: Double = 0.0,
        val lon: Double=0.0,
        val fetchRequired: Boolean,
        val units: String
    )
}
