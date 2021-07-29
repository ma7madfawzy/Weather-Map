package com.app.weather.domain.usecase

import com.app.weather.data.db.entity.CoordEntity
import com.app.weather.data.db.entity.LocationEntity
import com.app.weather.domain.repositories.PinnedLocationsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi


class DeletePinnedLocationUseCase(private val repo: PinnedLocationsRepository) {
    @ExperimentalCoroutinesApi
    operator fun invoke(coordEntity: CoordEntity) {
        repo.delete(LocationEntity(coordEntity))
    }
}