package com.app.weather.data.repositories

import com.algolia.search.saas.AbstractQuery
import com.app.weather.data.datasource.currentLocation.CurrentLocationLocalDataSource
import com.app.weather.domain.repositories.CurrentLocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Fawzy
 */

class CurrentLocationRepositoryImpl @Inject constructor(
    private val dataSource: CurrentLocationLocalDataSource
) : CurrentLocationRepository {

    override suspend fun loadLocationName() = dataSource.loadLocationTitle()

    override suspend fun saveLocationName(location: String) = dataSource.saveLocationTitle(location)

    override suspend fun loadLocationCoordinates() = dataSource.loadLocationCoordinates()

    override suspend fun saveLocationCoordinates(location: AbstractQuery.LatLng) =
        dataSource.saveCoordinates(location)


}