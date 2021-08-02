package com.app.weather.domain.repositories

import com.algolia.search.saas.AbstractQuery
import kotlinx.coroutines.flow.Flow


interface CurrentLocationRepository {

    suspend fun loadLocationName(): Flow<Any>
    suspend fun saveLocationName(location:String)

    suspend fun loadLocationCoordinates(): Flow<Any>
    suspend fun saveLocationCoordinates(location:AbstractQuery.LatLng)
}