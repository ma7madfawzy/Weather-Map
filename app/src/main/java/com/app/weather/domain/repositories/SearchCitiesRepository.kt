package com.app.weather.domain.repositories

import androidx.lifecycle.LiveData
import com.app.weather.data.db.entity.CitiesForSearchEntity
import com.app.weather.domain.common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Fawzy
 */

interface SearchCitiesRepository {

    fun loadCitiesByCityName(cityName: String?): Flow<Resource<List<CitiesForSearchEntity>>>
}
