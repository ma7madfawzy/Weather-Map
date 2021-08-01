package com.app.weather.data.repositories

import androidx.lifecycle.LiveData
import com.app.weather.data.datasource.searchCities.SearchCitiesLocalDataSource
import com.app.weather.data.datasource.searchCities.SearchCitiesRemoteDataSource
import com.app.weather.data.db.entity.CitiesForSearchEntity
import com.app.weather.domain.model.SearchResponse
import com.app.weather.domain.common.NetworkBoundResource
import com.app.weather.domain.repositories.SearchCitiesRepository
import com.app.weather.presentation.core.Constants.NetworkService.RATE_LIMITER_TYPE
import com.app.weather.domain.common.RateLimiter
import com.app.weather.domain.common.Resource
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Fawzy
 */

class SearchCitiesRepositoryImpl @Inject constructor(
    private val searchCitiesRemoteDataSource: SearchCitiesRemoteDataSource,
    private val searchCitiesLocalDataSource: SearchCitiesLocalDataSource,
) : SearchCitiesRepository {

    private val rateLimiter = RateLimiter<String>(1, TimeUnit.SECONDS)
    
    override fun loadCitiesByCityName(cityName: String?): StateFlow<Resource<List<CitiesForSearchEntity>>> {
        return object :
            NetworkBoundResource<List<CitiesForSearchEntity>, SearchResponse>(
                loadFromDbFlow = { searchCitiesLocalDataSource.getCityByName(cityName) },
                fetchFromNetworkSingle = { searchCitiesRemoteDataSource.getCityWithQuery(cityName ?: "") },
                saveCallResult = { searchCitiesLocalDataSource.insertCities(it) },
                onFetchFailed = { rateLimiter.reset(RATE_LIMITER_TYPE) }
            ) {}.asFlow
    }
}
