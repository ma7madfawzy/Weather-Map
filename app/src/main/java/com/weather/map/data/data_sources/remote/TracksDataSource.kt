package com.weather.map.data.data_sources.remote

import com.weather.map.data.api.NetworkHandler
import com.weather.map.domain.model.KeyValueDM
import com.weather.map.domain.common.Result
import com.weather.map.domain.model.TrackDM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL


/**
 * @SongsDataSource is a class that works with remote data sources.
 */
class TracksDataSource : BaseRemoteDataSource() {
    companion object {
        private const val PAGE_SIZE = 5
        private const val TRACKS_URL = BASE_URL + "v2/api/sayt/flat"
    }

    suspend fun fetchTracks(query: String, token: String, page: Int): Result<List<TrackDM>> {
        return requestData(query,token, page)
    }

    private suspend fun requestData(query: String, token: String,page: Int): Result<List<TrackDM>> {
        val AUTH_KEY_VALUE_DM = KeyValueDM("Authorization", token)
        return when (val result =
            NetworkHandler.request(getURL(query, page), GET_METHOD, API_KEY_VALUE_DM,AUTH_KEY_VALUE_DM)) {
            is Result.Success<String> -> {
                Result.Success(parseJson(result.data))
            }
            is Result.Error -> {
                Result.Error(result.exception)
            }
        }

    }

    private fun getURL(query: String, page: Int) =
        URL("$TRACKS_URL?query=$query&page=$page&limit=$PAGE_SIZE")

    private suspend fun parseJson(responseString: String): List<TrackDM> {
        return withContext(Dispatchers.Default) {
            val tracks: ArrayList<TrackDM> = ArrayList()
            val jsonArray = JSONArray(responseString)
            for (i in 0 until jsonArray.length()) {
                tracks.add(TrackDM(jsonArray.getJSONObject(i)))
            }
            tracks
        }
    }

}