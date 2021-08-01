package com.app.weather.data.db.dao

import androidx.lifecycle.LiveData
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
    fun insert(entity: LocationEntity):Long

    @Transaction
    fun deleteAndInsert(entity: LocationEntity) {
        delete(entity)
        insert(entity)
    }

    @Delete
    fun delete(entity: LocationEntity)

    @Query("Select count(*) from Locations")
    fun getCount(): Int
}
