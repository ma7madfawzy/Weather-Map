package com.app.weather.domain.usecase

import com.app.weather.domain.repositories.PinnedLocationsRepository
import com.algolia.search.saas.AbstractQuery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


class DeletePinnedLocationUseCase@Inject constructor(private val repo: PinnedLocationsRepository) {
    @ExperimentalCoroutinesApi
    operator fun invoke(latLng: AbstractQuery.LatLng) {
        repo.delete(latLng)
    }
}