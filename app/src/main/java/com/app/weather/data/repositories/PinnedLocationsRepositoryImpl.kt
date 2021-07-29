package com.app.weather.data.repositories

import androidx.lifecycle.LiveData
import com.app.weather.data.datasource.pinned_locations.PinnedLocationsDataSource
import com.app.weather.data.db.entity.LocationEntity
import com.app.weather.domain.common.Resource
import com.app.weather.domain.repositories.PinnedLocationsRepository
import javax.inject.Inject


/**
 * Class that requests token on the remote data source.
 */

class PinnedLocationsRepositoryImpl @Inject constructor(private val dataSource: PinnedLocationsDataSource) :
    PinnedLocationsRepository {
    override fun getAll() = dataSource.getAll()

    override fun insert(entity: LocationEntity) = dataSource.insert(entity)

    override fun deleteAndInsert(entity: LocationEntity) = dataSource.deleteAndInsert(entity)

    override fun delete(entity: LocationEntity) = dataSource.delete(entity)

    override fun getCount() = dataSource.getCount()

}