package com.app.weather.widget

import com.algolia.search.saas.AbstractQuery
import com.app.weather.data.db.entity.CurrentWeatherEntity
import com.app.weather.domain.usecase.CurrentWeatherUseCase
import com.app.weather.domain.usecase.LoadCurrentLocationUseCase
import com.app.weather.presentation.core.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherWidgetViewModel @Inject constructor(
    private val loadCurrentLocationUseCase: LoadCurrentLocationUseCase,
    private val currentWeatherUseCase: CurrentWeatherUseCase
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun requestLocationData(onResult: (weatherEntity: CurrentWeatherEntity) -> Unit) {
        coroutineScope.launch {
            loadCurrentLocationUseCase()
                .collect { requestCurrentWeather(it as AbstractQuery.LatLng, onResult) }
        }

    }

    private suspend fun requestCurrentWeather(
        latLng: AbstractQuery.LatLng,
        onResult: (entity: CurrentWeatherEntity) -> Unit
    ) {
        currentWeatherUseCase(CurrentWeatherUseCase.CurrentWeatherParams(latLng.lat, latLng.lng))
            .collect { weatherEntity ->
                weatherEntity.data?.let { nonNullEntity -> onResult(nonNullEntity) }
            }
    }
}
