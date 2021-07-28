package com.app.weather.presentation.core

/**
 * Created by Fawzy
 */

object Constants {


    object NetworkService {
        const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
        const val API_KEY_VALUE = "751d80f6c314139192ffcb62c107e654"
        const val RATE_LIMITER_TYPE = "data"
        const val API_KEY_QUERY = "appid"
    }

    object DataStore {
        const val USER_PREFERENCES_NAME = "DataStore"
        const val DEFAULT_LAT: Double = 51.5074//London lat
        const val DEFAULT_LONG: Double = 0.1278//London long
    }

    object AlgoliaKeys {
        const val APPLICATION_ID = "plNW8IW0YOIN"
        const val SEARCH_API_KEY = "029766644cb160efa51f2a32284310eb"
    }

    object Coords {
        const val LAT = "lat"
        const val LON = "lon"
        const val METRIC = "metric"
    }
}
