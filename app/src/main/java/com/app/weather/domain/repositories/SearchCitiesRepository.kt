package com.app.weather.domain.repositories

import androidx.lifecycle.LiveData
import com.app.weather.data.db.entity.CitiesForSearchEntity
import com.app.weather.utils.domain.Resource

/**
 * Created by Fawzy
 */

interface SearchCitiesRepository {

    fun loadCitiesByCityName(cityName: String?): LiveData<Resource<List<CitiesForSearchEntity>>>
}
