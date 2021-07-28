package com.app.weather.domain.usecase

import com.app.weather.domain.repositories.CurrentLatLngRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.merge
import javax.inject.Inject


class GetCurrentLatLngUseCase @Inject internal constructor(private val repo: CurrentLatLngRepository) {
    @ExperimentalCoroutinesApi
    suspend operator fun invoke() =repo.getLatLng()
}