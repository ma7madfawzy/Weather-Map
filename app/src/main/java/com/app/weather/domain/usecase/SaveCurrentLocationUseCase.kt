package com.app.weather.domain.usecase

import com.algolia.search.saas.AbstractQuery
import com.app.weather.domain.repositories.CurrentLocationRepository

class SaveCurrentLocationUseCase(private val repo: CurrentLocationRepository) {
    suspend operator fun invoke(latLng: AbstractQuery.LatLng) = repo.saveLocationCoordinates(latLng)
}