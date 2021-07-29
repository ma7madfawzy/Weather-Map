package com.app.weather.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.weather.data.db.entity.LocationEntity

/**
 * Created by Fawzy
 */

@Dao
interface LocationDao {

    @Query("SELECT * FROM Locations")
    fun getAll(): LiveData<List<LocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: LocationEntity)

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
