package com.weather.map.domain.use_cases

import com.weather.map.domain.common.Result
import com.weather.map.domain.model.TrackDM

interface GetTracksUC {
    suspend operator fun invoke(query: String = "", page: Int): Result<List<TrackDM>>
}