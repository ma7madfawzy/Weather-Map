package com.app.weather.domain.common

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread internal constructor(private val shouldFetch:(data: ResultType?) ->Boolean,
                                 private val loadFromDb:()->LiveData<ResultType>,
                                 private val fetchFromNetwork:()->Single<RequestType>,
                                 private val saveCallResult:(item: RequestType)->Unit,
                                 private val onFetchFailed:()->Unit
                                 ) {
    private val result = MediatorLiveData<Resource<ResultType>>()
    private var mDisposable: Disposable? = null
    private var dbSource: LiveData<ResultType>

    internal val asLiveData: LiveData<Resource<ResultType>>
        get() = result

    init {
        result.value = Resource.loading()
        @Suppress("LeakingThis")
        dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            //remove the source when changed
            result.removeSource(dbSource)
            if (shouldFetch(data)) fetchFromNetwork(dbSource)
            else result.setValue(Resource.success(data))
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        result.addSource(dbSource) { newData -> result.setValue(Resource.loading(newData)) }
        fetchFromNetwork()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : SingleObserver<RequestType> {
                    override fun onSubscribe(d: Disposable) {
                        if (!d.isDisposed) mDisposable = d
                    }

                    override fun onSuccess(requestType: RequestType) {
                        result.removeSource(dbSource)
                        saveResultAndReInit(requestType)
                    }

                    override fun onError(e: Throwable) {
                        onFetchFailed()
                        result.removeSource(dbSource)
                        result.addSource(dbSource) { newData ->
                            result.setValue(Resource.error(e.message.toString(), newData))
                        }
                        mDisposable!!.dispose()
                    }
                }
            )
    }

    @MainThread
    private fun saveResultAndReInit(response: RequestType) {
        Completable
            .fromCallable { saveCallResult(response) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {
                        if (!d.isDisposed) {
                            mDisposable = d
                        }
                    }

                    override fun onComplete() {
                        result.addSource(loadFromDb()) { newData ->
                            result.setValue(
                                Resource.success(
                                    newData
                                )
                            )
                        }
                        mDisposable!!.dispose()
                    }

                    override fun onError(e: Throwable) {
                        mDisposable!!.dispose()
                    }
                }
            )
    }
}
