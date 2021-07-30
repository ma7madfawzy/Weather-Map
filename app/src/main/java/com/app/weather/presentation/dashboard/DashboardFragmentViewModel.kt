package com.app.weather.presentation.dashboard

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.app.weather.data.db.entity.CurrentWeatherEntity
import com.app.weather.data.db.entity.ForecastEntity
import com.app.weather.domain.usecase.CurrentWeatherUseCase
import com.app.weather.domain.usecase.DeletePinnedLocationUseCase
import com.app.weather.domain.usecase.ForecastUseCase
import com.app.weather.domain.usecase.InsertPinnedLocationUseCase
import com.app.weather.presentation.core.BaseViewModel
import com.app.weather.presentation.core.BaseViewState
import com.app.weather.presentation.core.BaseVmFragment
import com.app.weather.presentation.core.Constants
import com.algolia.search.saas.AbstractQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Fawzy
 */

@HiltViewModel
class DashboardFragmentViewModel @Inject internal constructor(
    private val forecastUseCase: ForecastUseCase,
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    private val insertPinnedLocationUseCase: InsertPinnedLocationUseCase,
    private val deletePinnedLocationUseCase: DeletePinnedLocationUseCase,
    private val networkAvailableCallback: BaseVmFragment.NetworkAvailableCallback
) : BaseViewModel() {

    val isPinned: ObservableBoolean = ObservableBoolean(false)
    private val forecastParams: MutableLiveData<CurrentWeatherUseCase.CurrentWeatherParams> =
        MutableLiveData()
    private val currentWeatherParams: MutableLiveData<CurrentWeatherUseCase.CurrentWeatherParams> =
        MutableLiveData()

    val forecastViewState: LiveData<BaseViewState<ForecastEntity>> =
        forecastParams.switchMap { forecastUseCase(it) }

    val currentWeatherViewState: LiveData<BaseViewState<CurrentWeatherEntity>> =
        currentWeatherParams.switchMap { currentWeatherUseCase(it) }

    @ExperimentalCoroutinesApi
    fun onPinLocationClicked() {
        if (isPinned.get())
            unPinLocation()
        else
            pinLocation()
    }

    private fun pinLocation() {
        viewModelScope.launch (Dispatchers.IO){
            insertPinnedLocationUseCase(
                AbstractQuery.LatLng(
                    currentWeatherParams.value!!.lat,
                    currentWeatherParams.value!!.lon
                )
            )
            isPinned.set(true)
        }
    }

    private fun unPinLocation() {
        CoroutineScope(Dispatchers.IO).launch {
            deletePinnedLocationUseCase(
                AbstractQuery.LatLng(
                    currentWeatherParams.value!!.lat,
                    currentWeatherParams.value!!.lon
                )
            )
            isPinned.set(false)
        }
    }

    fun updateParams(lat: Double, lng: Double): DashboardFragmentViewModel {
        val latLng = AbstractQuery.LatLng(lat, lng)
        updateWeatherParams(latLng)
        updateForecastParams(latLng)
        return this
    }

    private fun updateForecastParams(latLng: AbstractQuery.LatLng) {
        setForecastParams(
            CurrentWeatherUseCase.CurrentWeatherParams(
                latLng.lat, latLng.lng,
                networkAvailableCallback.isNetworkAvailable(), Constants.Coords.METRIC
            )
        )
    }

    private fun updateWeatherParams(latLng: AbstractQuery.LatLng) {
        setCurrentWeatherParams(
            CurrentWeatherUseCase.CurrentWeatherParams(
                latLng.lat, latLng.lng,
                networkAvailableCallback.isNetworkAvailable(), Constants.Coords.METRIC
            )
        )
    }

    private fun setForecastParams(params: CurrentWeatherUseCase.CurrentWeatherParams) {
        if (forecastParams.value == params) return
        forecastParams.postValue(params)
    }

    fun setCurrentWeatherParams(params: CurrentWeatherUseCase.CurrentWeatherParams) {
        if (currentWeatherParams.value == params) return
        currentWeatherParams.postValue(params)
    }
}
