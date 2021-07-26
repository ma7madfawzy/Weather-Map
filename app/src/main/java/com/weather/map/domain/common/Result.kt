package com.weather.map.domain.common

import androidx.annotation.StringRes

/**
 *  A wrapper for handling  requests
 * @param <T> represents response type
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: String? = null, @StringRes val validationMsg: Int? = null) :
        Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}