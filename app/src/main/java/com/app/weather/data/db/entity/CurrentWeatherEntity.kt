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
    //returns color based on temp
    fun getColor(): Int {
        return when (main?.temp?.toInt()) {
            in -10..0-> Color.parseColor("#28E0AE")
            in 1..8-> Color.parseColor("#FF0090")
            in -9..15 -> Color.parseColor("#0090FF")
            in -16..20 -> Color.parseColor("#0051FF")
            in -21..29 -> Color.parseColor("#3D28E0")
            in -30..35 -> Color.parseColor("#FFAE00")
            in -36..42 -> Color.parseColor("#DC0000")
            else -> Color.parseColor("#28E0AE")
        }
    }
}
