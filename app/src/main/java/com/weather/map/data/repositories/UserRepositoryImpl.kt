package com.weather.map.data.repositories

import com.weather.map.data.data_sources.remote.UserDataSource
import com.weather.map.domain.repositories.UserRepository


/**
 * Class that requests token on the remote data source.
 */

class UserRepositoryImpl(private val dataSource: UserDataSource) : UserRepository {
    override suspend  fun getToken() = dataSource.getToken()

}