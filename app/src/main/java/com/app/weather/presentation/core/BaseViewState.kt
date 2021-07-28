package com.app.weather.presentation.core

import com.app.weather.data.db.entity.CurrentWeatherEntity
import com.app.weather.utils.domain.Resource
import com.app.weather.utils.domain.Status

/**
 * Created by Fawzy
 */

open class BaseViewState<T:Any>(private val baseStatus: Status, private val baseError: String?, val data:T?) {
    fun isLoading() = baseStatus == Status.LOADING
    fun getErrorMessage() = baseError
    fun shouldShowErrorMessage() = baseError != null
    constructor(resource:Resource<T>):this(resource.status, resource.message, resource.data)
}
