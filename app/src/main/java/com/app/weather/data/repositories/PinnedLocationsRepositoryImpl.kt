package com.app.weather.data.repositories

import com.algolia.search.saas.AbstractQuery
import com.app.weather.data.datasource.pinned_locations.PinnedLocationsDataSource
import com.app.weather.data.db.entity.LocationEntity
import com.app.weather.domain.repositories.PinnedLocationsRepository
import javax.inject.Inject


/**
 * Class that requests token on the remote data source.
 */

class PinnedLocationsRepositoryImpl @Inject constructor(private val dataSource: PinnedLocationsDataSource) :
    PinnedLocationsRepository {
    override fun getAll() = dataSource.getAll()

    override fun insert(entity: LocationEntity) = dataSource.insert(entity)

    override fun delete(latLng: AbstractQuery.LatLng) = dataSource.delete(latLng)

    override fun getCount() = dataSource.getCount()

}