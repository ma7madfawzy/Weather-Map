package com.app.weather.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.weather.data.db.dao.CitiesForSearchDao
import com.app.weather.data.db.dao.CurrentWeatherDao
import com.app.weather.data.db.dao.ForecastDao
import com.app.weather.data.db.entity.CitiesForSearchEntity
import com.app.weather.data.db.entity.CurrentWeatherEntity
import com.app.weather.data.db.entity.ForecastEntity
import com.app.weather.data.db.typeconverters.DataConverter

@Database(
    entities = [
        ForecastEntity::class,
        CurrentWeatherEntity::class,
        CitiesForSearchEntity::class
    ],
    version = 2
)
@TypeConverters(DataConverter::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun forecastDao(): ForecastDao

    abstract fun currentWeatherDao(): CurrentWeatherDao

    abstract fun citiesForSearchDao(): CitiesForSearchDao
}
