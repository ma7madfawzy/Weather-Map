package com.app.weather.data.datasource.pinned_locations

import com.algolia.search.saas.AbstractQuery
import com.app.weather.data.db.dao.LocationDao
import com.app.weather.data.db.entity.LocationEntity
import javax.inject.Inject

/**
 * Created by Fawzy
 */

class PinnedLocationsDataSource @Inject constructor(
    private val locationDao: LocationDao
) {
    fun getAll()=locationDao.getAll()
    fun insert(entity: LocationEntity) = locationDao.insert(entity)

    fun delete(latLng: AbstractQuery.LatLng) = locationDao.delete(latLng.lat,latLng.lng)

    fun getCount() = locationDao.getCount()
}
