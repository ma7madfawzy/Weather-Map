package com.app.weather.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.weather.data.db.entity.CurrentWeatherEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Fawzy
 */

@Dao
interface CurrentWeatherDao {

    @Query("SELECT * FROM CurrentWeather WHERE weatherLat like :lat and :lng LIKE weatherLon")
    fun getCurrentWeather(lat: Double, lng: Double): Flow<CurrentWeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentWeather(currentWeatherEntity: CurrentWeatherEntity)

    @Query("DELETE FROM CurrentWeather")
    fun deleteAll()

    @Query("Select count(*) from CurrentWeather")
    fun getCount(): Int
}
