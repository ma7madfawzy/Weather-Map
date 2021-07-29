package com.app.weather.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.app.weather.data.db.entity.CitiesForSearchEntity
import com.app.weather.domain.repositories.SearchCitiesRepository
import com.app.weather.presentation.core.BaseViewState
import com.app.weather.domain.common.Resource
import javax.inject.Inject

/**
 * Created by Fawzy
 */

class SearchCitiesUseCase @Inject internal constructor(private val repository: SearchCitiesRepository) {

    operator fun invoke(cityName: String?): LiveData<BaseViewState<List<CitiesForSearchEntity>>> {
        return repository.loadCitiesByCityName(cityName ?: "")
            .map { onSearchResultReady(it) }
    }

    private fun onSearchResultReady(resource: Resource<List<CitiesForSearchEntity>>) =
        BaseViewState(
            resource.status,
            resource.message,
            resource.data
        )

}
