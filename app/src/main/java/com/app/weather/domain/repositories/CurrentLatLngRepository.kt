package com.app.weather.domain.repositories

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow


interface CurrentLatLngRepository {

    suspend fun getLatLng(): Flow<LatLng>

    suspend fun setLatLng(latLng:String)
}