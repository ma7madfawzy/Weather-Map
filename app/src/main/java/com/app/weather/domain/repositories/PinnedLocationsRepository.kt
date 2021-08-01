package com.app.weather.domain.repositories

import androidx.lifecycle.LiveData
import com.app.weather.data.db.entity.LocationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


interface PinnedLocationsRepository {

    fun getAll(): Flow<List<LocationEntity>>

    fun insert(entity: LocationEntity):Long

    fun deleteAndInsert(entity: LocationEntity)

    fun delete(entity: LocationEntity)

    fun getCount(): Int
}