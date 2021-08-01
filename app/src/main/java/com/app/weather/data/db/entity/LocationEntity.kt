package com.app.weather.data.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.algolia.search.saas.AbstractQuery
import kotlinx.parcelize.Parcelize

/**
 * Created by Fawzy
 */

@Parcelize
@Entity(tableName = "Locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long,
    @ColumnInfo(name = "locationLat")
    var lat: Double?,
    @ColumnInfo(name = "locationLon")
    var lng: Double?,
) : Parcelable {

    constructor(latLng: AbstractQuery.LatLng) : this(0,latLng.lat,latLng.lng)

    fun latLang()= AbstractQuery.LatLng(lat ?: 0.0, lng ?: 0.0)
}
