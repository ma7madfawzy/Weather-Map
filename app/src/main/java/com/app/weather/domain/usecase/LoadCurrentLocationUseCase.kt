package com.app.weather.domain.usecase

import com.app.weather.domain.repositories.CurrentLocationRepository

class LoadCurrentLocationUseCase(private val repo: CurrentLocationRepository) {
    suspend operator fun invoke() = repo.loadLocationCoordinates()
}