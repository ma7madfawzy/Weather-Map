package com.app.weather.domain.repositories

import com.algolia.search.saas.AbstractQuery
import com.app.weather.data.db.entity.LocationEntity
import kotlinx.coroutines.flow.Flow


interface PinnedLocationsRepository {

    fun getAll(): Flow<List<LocationEntity>>

    fun insert(entity: LocationEntity):Long

    fun delete(latLng: AbstractQuery.LatLng)

    fun getCount(): Int
}