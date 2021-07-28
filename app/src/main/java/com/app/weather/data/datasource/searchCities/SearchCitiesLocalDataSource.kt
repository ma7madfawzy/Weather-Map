package com.app.weather.data.datasource.searchCities

import androidx.lifecycle.LiveData
import com.app.weather.data.db.dao.CitiesForSearchDao
import com.app.weather.data.db.entity.CitiesForSearchEntity
import com.app.weather.domain.model.SearchResponse
import javax.inject.Inject

/**
 * Created by Fawzy
 */

class SearchCitiesLocalDataSource @Inject constructor(
    private val citiesForSearchDao: CitiesForSearchDao
) {

    fun getCityByName(cityName: String?): LiveData<List<CitiesForSearchEntity>> =
        citiesForSearchDao.getCityByName(
            cityName
        )

    fun insertCities(response: SearchResponse) {
        response.hits
            ?.map { CitiesForSearchEntity(it) }
            ?.let { citiesForSearchDao.insertCities(it) }
    }
}
