package com.app.weather.domain.repositories

import androidx.lifecycle.LiveData
import com.app.weather.data.db.entity.LocationEntity


interface PinnedLocationsRepository {

    fun getAll(): LiveData<List<LocationEntity>>

    fun insert(entity: LocationEntity)

    fun deleteAndInsert(entity: LocationEntity)

    fun delete(entity: LocationEntity)

    fun getCount(): Int
}