package com.app.weather.data.repositories

import androidx.lifecycle.LiveData
import com.app.weather.data.datasource.currentWeather.CurrentWeatherLocalDataSource
import com.app.weather.data.datasource.currentWeather.CurrentWeatherRemoteDataSource
import com.app.weather.data.db.entity.CurrentWeatherEntity
import com.app.weather.domain.model.CurrentWeatherResponse
import com.app.weather.domain.repositories.CurrentWeatherRepository
import com.app.weather.domain.commmon.NetworkBoundResource
import com.app.weather.presentation.core.Constants
import com.app.weather.domain.common.RateLimiter
import com.app.weather.domain.common.Resource
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
    ): LiveData<Resource<CurrentWeatherEntity>> {
        return object :
            NetworkBoundResource<CurrentWeatherEntity, CurrentWeatherResponse>(
                { fetchRequired },
                loadFromDb = { currentWeatherLocalDataSource.getCurrentWeather() },
                fetchFromNetwork = {
                    currentWeatherRemoteDataSource.getCurrentWeatherByGeoCords(lat, lon, units)
                },
                saveCallResult = { currentWeatherLocalDataSource.insertCurrentWeather(it) },
                onFetchFailed = { currentWeatherRateLimit.reset(Constants.NetworkService.RATE_LIMITER_TYPE) }
            ) {}.asLiveData
    }
}