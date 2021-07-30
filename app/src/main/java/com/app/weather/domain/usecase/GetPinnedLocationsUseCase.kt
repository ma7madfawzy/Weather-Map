package com.app.weather.domain.usecase

import com.app.weather.domain.repositories.PinnedLocationsRepository
import javax.inject.Inject


class GetPinnedLocationsUseCase @Inject internal constructor(private val repo: PinnedLocationsRepository) {
    operator fun invoke() = repo.getAll()
}