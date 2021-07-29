package com.app.weather.domain.usecase

import androidx.lifecycle.map
import com.app.weather.domain.common.Resource
import com.app.weather.domain.repositories.PinnedLocationsRepository
import com.app.weather.presentation.core.BaseViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


class GetPinnedLocationsUseCase @Inject internal constructor(private val repo: PinnedLocationsRepository) {
    @ExperimentalCoroutinesApi
    operator fun invoke() = repo.getAll()
}