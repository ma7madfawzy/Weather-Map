package com.app.weather.data.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.app.weather.domain.model.Coord
import com.app.weather.domain.model.Geoloc
import kotlinx.parcelize.Parcelize

/**
 * Created by Fawzy
 */

@Parcelize
@Entity(tableName = "Coord")
data class CoordEntity(
    @ColumnInfo(name = "CoordLat")
    val lon: Double?,
    @ColumnInfo(name = "coordLon")
    val lat: Double?
) : Parcelable {
    @Ignore
    constructor(coord: Coord) : this(
        lon = coord.lon,
        lat = coord.lat
    )

    @Ignore
    constructor(geoloc: Geoloc?) : this(
        lon = geoloc?.lng,
        lat = geoloc?.lat
    )
}
