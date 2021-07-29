package com.app.weather.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.weather.data.db.entity.CurrentWeatherEntity

/**
 * Created by Fawzy
 */

@Dao
interface CurrentWeatherDao {

    @Query("SELECT * FROM CurrentWeather")
    fun getCurrentWeather(): LiveData<CurrentWeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentWeather(currentWeatherEntity: CurrentWeatherEntity)

    @Transaction
    fun deleteAndInsert(currentWeatherEntity: CurrentWeatherEntity) {
//        deleteAll()
        insertCurrentWeather(currentWeatherEntity)
    }

    @Query("DELETE FROM CurrentWeather")
    fun deleteAll()

    @Query("Select count(*) from CurrentWeather")
    fun getCount(): Int
}
