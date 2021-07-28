package com.app.weather.utils.extensions

import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable


inline fun <T : Any> Single<T>.simpleSubscribe(
    crossinline onSubscribe: (d: Disposable) -> Unit,
    crossinline onSuccess: (requestType: T) -> Unit,
    crossinline onError: (e: Throwable) -> Unit
): SingleObserver<T> {
    return object : SingleObserver<T> {
        override fun onSubscribe(d: Disposable) {
            onSubscribe(d)
        }

        override fun onSuccess(requestType: T) {
            onSuccess(requestType)
        }

        override fun onError(e: Throwable) {
            onError(e)
        }
    }
}
