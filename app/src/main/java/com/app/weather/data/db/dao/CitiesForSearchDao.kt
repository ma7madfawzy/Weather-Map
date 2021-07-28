package com.app.weather.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.weather.data.db.entity.CitiesForSearchEntity

/**
 * Created by Fawzy
 */

@Dao
interface CitiesForSearchDao {

    @Query("SELECT * FROM CitiesForSearch")
    fun getCities(): LiveData<List<CitiesForSearchEntity>>

    @Query(
        "SELECT * FROM CitiesForSearch WHERE fullName like '%' || :city || '%'|| '%' ORDER BY fullName DESC"
    )
    fun getCityByName(city: String? = ""): LiveData<List<CitiesForSearchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(citiesForSearchEntity: CitiesForSearchEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCities(cities: List<CitiesForSearchEntity>)

    @Query("DELETE FROM CitiesForSearch")
    fun deleteCities()

    @Query("Select count(*) from CitiesForSearch")
    fun getCount(): Int
}
