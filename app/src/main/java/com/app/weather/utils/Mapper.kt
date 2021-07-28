package com.app.weather.utils

/**
 * Created by Fawzy
 */

interface Mapper<R, D> {
    fun mapFrom(type: R): D
}
