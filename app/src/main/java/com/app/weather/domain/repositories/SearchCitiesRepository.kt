package com.app.weather.domain.repositories

import androidx.lifecycle.LiveData
import com.app.weather.data.db.entity.CitiesForSearchEntity
import com.app.weather.domain.common.Resource

/**
 * Created by Fawzy
 */

interface SearchCitiesRepository {

    fun loadCitiesByCityName(cityName: String?): LiveData<Resource<List<CitiesForSearchEntity>>>
}
