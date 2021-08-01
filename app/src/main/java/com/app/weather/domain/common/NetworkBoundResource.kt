package com.app.weather.domain.common

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.weather.utils.extensions.logE
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class NetworkBoundResource<OfflineResponseType, NetworkResponseType>
@MainThread internal constructor(
    shouldFetch: () -> Boolean= { true },
    private val loadFromDbFlow: () -> Flow<OfflineResponseType>,
    private val fetchFromNetworkSingle: () -> Single<NetworkResponseType>,
    private val saveCallResult: (networkResponseType: NetworkResponseType) -> Unit,
    private val onFetchFailed: () -> Unit
) {
    private val result = MutableStateFlow<Resource<OfflineResponseType>>(Resource.loading())
    private var mDisposable: Disposable? = null

    internal val asFlow: MutableStateFlow<Resource<OfflineResponseType>>
        get() = result

    init {
        if (shouldFetch()) fetchFromNetwork()
        else fetchOffline()
    }

    private fun fetchOffline() {
        CoroutineScope(Dispatchers.IO).launch {
            loadFromDbFlow().collect { result.value=Resource.success(it) }
        }
    }

    private fun fetchFromNetwork() {
        fetchFromNetworkSingle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : SingleObserver<NetworkResponseType> {
                    override fun onSubscribe(d: Disposable) {
                        if (!d.isDisposed) mDisposable = d
                    }

                    override fun onSuccess(networkResponseType: NetworkResponseType) {
                        saveResultAndReInit(networkResponseType)
                    }

                    override fun onError(e: Throwable) {
                        onFetchFailed()
                        result.value = Resource.error(e.message.toString())
                        fetchOffline()
                        mDisposable!!.dispose()
                    }
                }
            )
    }

    @MainThread
    private fun saveResultAndReInit(networkResponseType: NetworkResponseType) {
        Completable
            .fromCallable { saveCallResult(networkResponseType) }
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
                        logE("network fetched data was saved to database successfully")
                        fetchOffline()
                        mDisposable!!.dispose()
                    }

                    override fun onError(e: Throwable) {
                        logE("Error saving network fetched data to database")
                        mDisposable!!.dispose()
                    }
                }
            )
    }
}
