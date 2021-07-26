package com.weather.map.data.repositories

import com.weather.map.data.data_sources.remote.TracksDataSource
import com.weather.map.domain.common.Result
import com.weather.map.domain.model.TrackDM
import com.weather.map.domain.repositories.TracksRepository


/**
 * Class that requests fetchSongs on the remote data source.
 */

class TracksRepositoryImpl(private val dataSource: TracksDataSource) : TracksRepository {
    override suspend fun fetchTracks(query: String, token: String, page: Int)
            : Result<List<TrackDM>> = dataSource.fetchTracks(query, token, page)

}