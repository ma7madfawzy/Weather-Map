package com.app.weather.data.repositories

import androidx.lifecycle.LiveData
import com.app.weather.data.datasource.forecast.ForecastLocalDataSource
import com.app.weather.data.datasource.forecast.ForecastRemoteDataSource
import com.app.weather.data.db.entity.ForecastEntity
import com.app.weather.domain.model.ForecastResponse
import com.app.weather.domain.repositories.ForecastRepository
import com.app.weather.domain.repositories.NetworkBoundResource
import com.app.weather.presentation.core.Constants.NetworkService.RATE_LIMITER_TYPE
import com.app.weather.utils.domain.RateLimiter
import com.app.weather.utils.domain.Resource
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Fawzy
 */

class ForecastRepositoryImpl @Inject constructor(
    private val forecastRemoteDataSource: ForecastRemoteDataSource,
    private val forecastLocalDataSource: ForecastLocalDataSource
) : ForecastRepository {

    private val forecastListRateLimit = RateLimiter<String>(30, TimeUnit.SECONDS)

    override fun loadForecastByCoord(lat: Double, lon: Double, fetchRequired: Boolean, units: String)
            : LiveData<Resource<ForecastEntity>> {
        return object :
            NetworkBoundResource<ForecastEntity, ForecastResponse>(
                shouldFetch = { fetchRequired },
                loadFromDb = { forecastLocalDataSource.getForecast() },
                fetchFromNetwork = { forecastRemoteDataSource.getForecastByGeoCords(lat, lon, units) },
                saveCallResult = { forecastLocalDataSource.insertForecast(it) },
                onFetchFailed = { forecastListRateLimit.reset(RATE_LIMITER_TYPE) }
            ) {}.asLiveData
    }
}
