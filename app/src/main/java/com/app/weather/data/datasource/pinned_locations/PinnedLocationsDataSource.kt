package com.app.weather.data.datasource.pinned_locations

import androidx.lifecycle.LiveData
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

    fun deleteAndInsert(entity: LocationEntity) = locationDao.deleteAndInsert(entity)

    fun delete(entity: LocationEntity) = locationDao.delete(entity)

    fun getCount() = locationDao.getCount()
}
