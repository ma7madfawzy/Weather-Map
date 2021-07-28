package com.app.weather.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.app.weather.data.db.entity.CitiesForSearchEntity
import com.app.weather.domain.repositories.SearchCitiesRepository
import com.app.weather.presentation.search.SearchViewState
import com.app.weather.utils.domain.Resource
import javax.inject.Inject

/**
 * Created by Fawzy
 */

class SearchCitiesUseCase @Inject internal constructor(private val repository: SearchCitiesRepository) {

    operator fun invoke(cityName: String?): LiveData<SearchViewState> {
        return repository.loadCitiesByCityName(cityName ?: "")
            .map { onSearchResultReady(it) }
    }

    private fun onSearchResultReady(resource: Resource<List<CitiesForSearchEntity>>): SearchViewState {
        return SearchViewState(
            status = resource.status,
            error = resource.message,
            data = resource.data
        )
    }

}
