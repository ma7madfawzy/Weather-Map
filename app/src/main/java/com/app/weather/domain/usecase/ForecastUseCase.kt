package com.app.weather.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.app.weather.data.db.entity.ForecastEntity
import com.app.weather.domain.repositories.ForecastRepository
import com.app.weather.presentation.core.BaseViewState
import com.app.weather.presentation.core.Constants
import com.app.weather.presentation.dashboard.ForecastMapper
import com.app.weather.domain.common.Resource
import javax.inject.Inject

/**
 * Created by Fawzy
 */

class ForecastUseCase @Inject internal constructor(private val repository: ForecastRepository) {

    operator fun invoke(params: CurrentWeatherUseCase.CurrentWeatherParams?): LiveData<BaseViewState<ForecastEntity>> {
        return repository.loadForecastByCoord(
            params?.lat?.toDouble() ?: 0.0,
            params?.lon?.toDouble() ?: 0.0,
            params?.fetchRequired
                ?: false,
            units = params?.units ?: Constants.Coords.METRIC
        ).map {
            onForecastResultReady(it)
        }
    }

    private fun onForecastResultReady(resource: Resource<ForecastEntity>): BaseViewState<ForecastEntity> {
        val mappedList = resource.data?.list?.let { ForecastMapper().mapFrom(it) }
        resource.data?.list = mappedList

        return BaseViewState(resource)
    }
}
