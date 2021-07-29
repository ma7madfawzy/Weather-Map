package com.app.weather.data.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

/**
 * Created by Fawzy
 */

@Parcelize
@Entity(tableName = "Locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "lat")
    var lat: Double?,
    @ColumnInfo(name = "lng")
    var lng: Double?,
) : Parcelable {
    constructor(coordinateEntity: CoordEntity) : this(
        0,
        coordinateEntity.lat ?: 0.0,
        coordinateEntity.lon ?: 0.0
    )
    fun latLang()=LatLng(lat?: 0.0,lng?: 0.0)
}
