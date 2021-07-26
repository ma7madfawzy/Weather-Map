package com.weather.map.data.data_sources.remote

import com.weather.map.domain.model.KeyValueDM

open class BaseRemoteDataSource {
    companion object {
        const val GET_METHOD = "GET"
        const val POST_METHOD = "POST"

        const val BASE_URL = "http://staging-gateway.mondiamedia.com/"

        private const val API_KEY = "X-MM-GATEWAY-KEY"
        private const val API_KEY_VALUE = "G2269608a-bf41-2dc7-cfea-856957fcab1e"
        val API_KEY_VALUE_DM = KeyValueDM(API_KEY, API_KEY_VALUE)
    }
}