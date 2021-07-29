package com.app.weather.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.app.weather.data.db.entity.CurrentWeatherEntity
import com.app.weather.domain.repositories.CurrentWeatherRepository
import com.app.weather.presentation.core.BaseViewState
import com.app.weather.presentation.core.Constants
import com.app.weather.domain.common.Resource
import javax.inject.Inject

/**
 * Created by Fawzy
 */

class CurrentWeatherUseCase @Inject internal constructor(
    private val repository: CurrentWeatherRepository
) {

    operator fun invoke(params: CurrentWeatherParams?): LiveData<BaseViewState<CurrentWeatherEntity>> {
        return repository.loadCurrentWeatherByGeoCords(
            params?.lat?.toDouble() ?: 0.0,
            params?.lon?.toDouble() ?: 0.0,
            params?.fetchRequired
                ?: false,
            units = params?.units ?: Constants.Coords.METRIC
        ).map {
            BaseViewState(it)
        }
    }


    class CurrentWeatherParams(
        val lat: String = "",
        val lon: String = "",
        val fetchRequired: Boolean,
        val units: String
    )
}
