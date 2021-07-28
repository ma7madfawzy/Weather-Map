package com.app.weather.data.db.entity

import android.os.Parcelable
import androidx.room.*
import com.app.weather.domain.model.ForecastResponse
import com.app.weather.domain.model.ListItem
import kotlinx.parcelize.Parcelize

/**
 * Created by Fawzy
 */

@Parcelize
@Entity(tableName = "Forecast")
data class ForecastEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @Embedded
    var city: CityEntity?,

    @ColumnInfo(name = "list")
    var list: List<ListItem>?
) : Parcelable {

    @Ignore
    constructor(forecastResponse: ForecastResponse) : this(
        id = 0,
        city = forecastResponse.city?.let { CityEntity(it) },
        list = forecastResponse.list
    )
}
