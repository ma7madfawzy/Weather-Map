package com.app.weather.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.app.weather.presentation.core.Constants
import com.app.weather.data.db.entity.CurrentWeatherEntity
import com.app.weather.domain.repositories.CurrentWeatherRepository
import com.app.weather.presentation.dashboard.CurrentWeatherViewState
import com.app.weather.utils.UseCaseLiveData
import com.app.weather.utils.domain.Resource
import javax.inject.Inject

/**
 * Created by Fawzy
 */

class CurrentWeatherUseCase @Inject internal constructor(
    private val repository: CurrentWeatherRepository
)  {


    operator fun invoke(params: CurrentWeatherParams?): LiveData<CurrentWeatherViewState> {
        return repository.loadCurrentWeatherByGeoCords(
            params?.lat?.toDouble() ?: 0.0,
            params?.lon?.toDouble() ?: 0.0,
            params?.fetchRequired
                ?: false,
            units = params?.units ?: Constants.Coords.METRIC
        ).map {
            onCurrentWeatherResultReady(it)
        }
    }

    private fun onCurrentWeatherResultReady(resource: Resource<CurrentWeatherEntity>): CurrentWeatherViewState {
        return CurrentWeatherViewState(
            status = resource.status,
            error = resource.message,
            data = resource.data
        )
    }

    class CurrentWeatherParams(
        val lat: String = "",
        val lon: String = "",
        val fetchRequired: Boolean,
        val units: String
    )
}
