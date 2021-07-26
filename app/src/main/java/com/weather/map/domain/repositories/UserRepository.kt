package com.weather.map.domain.repositories

import com.weather.map.domain.common.Result


/**
 * Class that requests token on the remote data source.
 */

interface UserRepository {

    suspend fun getToken(): Result<String>
}