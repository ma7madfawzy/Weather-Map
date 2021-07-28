package com.app.weather.presentation.core

import com.app.weather.utils.domain.Status

/**
 * Created by Fawzy
 */

open class BaseViewState(val baseStatus: Status, val baseError: String?) {
    fun isLoading() = baseStatus == Status.LOADING
    fun getErrorMessage() = baseError
    fun shouldShowErrorMessage() = baseError != null
}
