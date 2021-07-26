package com.weather.map.domain.repositories

import com.weather.map.domain.common.Result
import com.weather.map.domain.model.TrackDM


/**
 * Class that requests fetchSongs on the remote data source.
 */

interface TracksRepository {
    suspend fun fetchTracks(query: String = "", token: String, page: Int): Result<List<TrackDM>>
}