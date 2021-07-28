package com.app.weather.domain.usecase

import com.app.weather.domain.repositories.CurrentLatLngRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi


class SetCurrentLatLngUseCase(private val repo: CurrentLatLngRepository) {
    @ExperimentalCoroutinesApi
    suspend operator fun invoke(latLng: LatLng) {
        repo.setLatLng("${latLng.latitude}:${latLng.longitude}")
    }
}