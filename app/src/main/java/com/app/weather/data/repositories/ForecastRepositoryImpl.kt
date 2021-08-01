package com.app.weather.data.repositories

import com.app.weather.data.datasource.forecast.ForecastLocalDataSource
import com.app.weather.data.datasource.forecast.ForecastRemoteDataSource
import com.app.weather.data.db.entity.ForecastEntity
import com.app.weather.domain.common.NetworkBoundResource
import com.app.weather.domain.common.RateLimiter
import com.app.weather.domain.common.Resource
import com.app.weather.domain.model.ForecastResponse
import com.app.weather.domain.repositories.ForecastRepository
import com.app.weather.presentation.core.Constants.NetworkService.RATE_LIMITER_TYPE
import kotlinx.coroutines.flow.StateFlow
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

    override fun loadForecast(lat: Double, lon: Double, fetchRequired: Boolean, units: String)
            : StateFlow<Resource<ForecastEntity>> {
        return object :
            NetworkBoundResource<ForecastEntity, ForecastResponse>(
                shouldFetch = { fetchRequired },
                loadFromDbFlow = { forecastLocalDataSource.getForecast(lat, lon) },
                fetchFromNetworkSingle = { forecastRemoteDataSource.getForecast(lat, lon, units) },
                saveCallResult = { forecastLocalDataSource.insertForecast(it, lat, lon) },
                onFetchFailed = { forecastListRateLimit.reset(RATE_LIMITER_TYPE) }
            ) {}.asFlow
    }
}
