package com.weather.map.domain.use_cases

import com.weather.map.domain.common.Result
import com.weather.map.domain.model.TrackDM
import com.weather.map.domain.repositories.TracksRepository

class GetTracksUCImpl(private val tokenUC: GetTokenUC, private val repo: TracksRepository) :
    GetTracksUC {
    private var tokenResult: Result<String>? = null
    override suspend fun invoke(query: String, page: Int): Result<List<TrackDM>> {
        //fetch the token only first time and use it's value afterward
        return when (val result = tokenResult ?: tokenUC()) {
            is Result.Success<String> -> {
                tokenResult = result
                repo.fetchTracks(query, result.data, page)
            }
            is Result.Error -> result
        }
    }
}