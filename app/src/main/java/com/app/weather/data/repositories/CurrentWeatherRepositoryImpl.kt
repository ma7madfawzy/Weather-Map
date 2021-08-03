package com.app.weather.data.repositories

import com.app.weather.data.datasource.currentWeather.CurrentWeatherLocalDataSource
import com.app.weather.data.datasource.currentWeather.CurrentWeatherRemoteDataSource
import com.app.weather.data.db.entity.CurrentWeatherEntity
import com.app.weather.domain.common.NetworkBoundResource
import com.app.weather.domain.common.RateLimiter
import com.app.weather.domain.common.Resource
import com.app.weather.domain.model.CurrentWeatherResponse
import com.app.weather.domain.repositories.CurrentWeatherRepository
import com.app.weather.presentation.core.Constants
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Fawzy
 */

class CurrentWeatherRepositoryImpl @Inject constructor(
    private val currentWeatherRemoteDataSource: CurrentWeatherRemoteDataSource,
    private val currentWeatherLocalDataSource: CurrentWeatherLocalDataSource
) : CurrentWeatherRepository {

    private val currentWeatherRateLimit = RateLimiter<String>(30, TimeUnit.SECONDS)

    override fun loadCurrentWeatherByGeoCords(
        lat: Double, lon: Double, fetchRequired: Boolean, units: String
    ): StateFlow<Resource<CurrentWeatherEntity>> {
        return object :
            NetworkBoundResource<CurrentWeatherEntity, CurrentWeatherResponse>(
                { fetchRequired },
                loadFromDbFlow = { currentWeatherLocalDataSource.getCurrentWeather(lat,lon) },
                fetchFromNetworkSingle = {
                    currentWeatherRemoteDataSource.getCurrentWeatherByGeoCords(lat, lon, units)
                },
                saveCallResult = { currentWeatherLocalDataSource.insertCurrentWeather(it,lat,lon) },
                onFetchFailed = { currentWeatherRateLimit.reset(Constants.NetworkService.RATE_LIMITER_TYPE) }
            ) {}.asFlow
    }
}