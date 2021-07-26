package com.weather.map.data.data_sources.remote

import com.weather.map.data.api.NetworkHandler
import com.weather.map.domain.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL


/**
 * @SongsDataSource is a class that works with remote data sources.
 */
class UserDataSource : BaseRemoteDataSource() {
    companion object{
        private const val TOKEN_URL = BASE_URL + "v0/api/gateway/token/client"
        private const val TOKEN_KEY =  "accessToken"
        private const val TOKEN_TYPE =  "tokenType"

    }

    suspend fun getToken(): Result<String> {
        return requestToken()
    }

    private suspend fun requestToken()
            : Result<String> {

        return when (val result = NetworkHandler.request(getURL(), POST_METHOD, API_KEY_VALUE_DM)) {
            is Result.Success<String> -> {
                Result.Success(parseJson(result.data))
            }
            is Result.Error -> {
                Result.Error(result.exception)
            }
        }

    }

    private fun getURL() = URL(TOKEN_URL)

    private suspend fun parseJson(responseString: String) = withContext(Dispatchers.Default) {
       val response=JSONObject(responseString)
        response.getString(TOKEN_TYPE)+" "+response.getString(TOKEN_KEY)
    }

}