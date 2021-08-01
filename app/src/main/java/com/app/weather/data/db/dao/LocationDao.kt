package com.app.weather.data.db.dao

import androidx.room.*
import com.app.weather.data.db.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Fawzy
 */

@Dao
interface LocationDao {

    @Query("SELECT * FROM Locations")
    fun getAll(): Flow<List<LocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: LocationEntity): Long

    @Query("DELETE FROM Locations WHERE locationLat like :lat and locationLon LIKE:lng")
    fun delete(lat: Double, lng: Double)

    @Query("Select count(*) from Locations")
    fun getCount(): Int
}
