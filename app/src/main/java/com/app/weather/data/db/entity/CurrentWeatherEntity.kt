package com.app.weather.data.db.entity

import android.graphics.Color
import android.os.Parcelable
import androidx.room.*
import com.app.weather.domain.model.CurrentWeatherResponse
import com.app.weather.domain.model.WeatherItem
import kotlinx.parcelize.Parcelize
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Fawzy
 */

@Parcelize
@Entity(tableName = "CurrentWeather")
data class CurrentWeatherEntity(
    @ColumnInfo(name = "visibility")
    var visibility: Int?,
    @ColumnInfo(name = "timezone")
    var timezone: Int?,
    @Embedded
    var main: MainEntity?,
    @Embedded
    var clouds: CloudsEntity?,
    @ColumnInfo(name = "dt")
    var dt: Long?,
    @ColumnInfo(name = "weather")
    val weather: List<WeatherItem?>? = null,
    @ColumnInfo(name = "name")
    val name: String?,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "base")
    val base: String?,
    @Embedded
    val wind: WindEntity?,
    @ColumnInfo(name = "weatherLat")
    val lat: Double?,
    @ColumnInfo(name = "weatherLon")
    val lng: Double?
) : Parcelable {
    @Ignore
    constructor(currentWeather: CurrentWeatherResponse, lat: Double, lng: Double) : this(
        visibility = currentWeather.visibility,
        timezone = currentWeather.timezone,
        main = MainEntity(currentWeather.main),
        clouds = CloudsEntity(currentWeather.clouds),
        dt = currentWeather.dt?.toLong(),
        weather = currentWeather.weather,
        name = currentWeather.name,
        id = 0,
        base = currentWeather.base,
        wind = WindEntity(currentWeather.wind),
        lat = lat,
        lng = lng
    )

    fun getCurrentWeather(): WeatherItem? {
        return weather?.first()
    }

    private fun getDateTime(s: Long): DayOfWeek? {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val netDate = Date(s * 1000)
            val formattedDate = sdf.format(netDate)

            LocalDate.of(
                formattedDate.substringAfterLast("/").toInt(),
                formattedDate.substringAfter("/").take(2).toInt(),
                formattedDate.substringBefore("/").toInt()
            )
                .dayOfWeek
        } catch (e: Exception) {
            e.printStackTrace()
            DayOfWeek.MONDAY
        }
    }

    fun getColor(): Int {
        return when (dt?.let { getDateTime(it) }) {
            DayOfWeek.MONDAY -> Color.parseColor("#28E0AE")
            DayOfWeek.TUESDAY -> Color.parseColor("#FF0090")
            DayOfWeek.WEDNESDAY -> Color.parseColor("#FFAE00")
            DayOfWeek.THURSDAY -> Color.parseColor("#0090FF")
            DayOfWeek.FRIDAY -> Color.parseColor("#DC0000")
            DayOfWeek.SATURDAY -> Color.parseColor("#0051FF")
            DayOfWeek.SUNDAY -> Color.parseColor("#3D28E0")
            else -> Color.parseColor("#28E0AE")
        }
    }
}
