package com.app.weather.presentation.dashboard

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.algolia.search.saas.AbstractQuery
import com.app.weather.data.db.entity.ForecastEntity
import com.app.weather.domain.usecase.CurrentWeatherUseCase
import com.app.weather.domain.usecase.DeletePinnedLocationUseCase
import com.app.weather.domain.usecase.ForecastUseCase
import com.app.weather.domain.usecase.InsertPinnedLocationUseCase
import com.app.weather.presentation.core.BaseViewModel
import com.app.weather.presentation.core.BaseViewState
import com.app.weather.presentation.core.BaseVmFragment
import com.app.weather.presentation.core.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Fawzy
 */

@HiltViewModel
class DashboardFragmentViewModel @Inject internal constructor(
    private val forecastUseCase: ForecastUseCase,
    private val insertPinnedLocationUseCase: InsertPinnedLocationUseCase,
    private val deletePinnedLocationUseCase: DeletePinnedLocationUseCase,
    private val networkAvailableCallback: BaseVmFragment.NetworkAvailableCallback
) : BaseViewModel() {
    private lateinit var currentWeatherParams: CurrentWeatherUseCase.CurrentWeatherParams
    lateinit var forecastParams: CurrentWeatherUseCase.CurrentWeatherParams

    val isPinned: ObservableBoolean = ObservableBoolean(false)

    val forecastViewState: MutableLiveData<BaseViewState<ForecastEntity>> = MutableLiveData()


    private fun updateForecastParams(params: CurrentWeatherUseCase.CurrentWeatherParams) {
        forecastParams = params
        viewModelScope.launch(Dispatchers.IO) {
            forecastUseCase(forecastParams).collect {
                forecastViewState.postValue(map(it))
            }
        }
    }

    private fun map(entity: BaseViewState<ForecastEntity>)
            : BaseViewState<ForecastEntity> {
        val mappedList = entity.data?.list?.let { ForecastMapper().mapFrom(it) }
        entity.data?.list = mappedList
        return entity
    }

    @ExperimentalCoroutinesApi
    fun onPinLocationClicked() {
        if (isPinned.get())
            unPinLocation()
        else
            pinLocation()
    }

    private fun pinLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            insertPinnedLocationUseCase(
                AbstractQuery.LatLng(
                    currentWeatherParams.lat,
                    currentWeatherParams.lon
                )
            )
            isPinned.set(true)
        }
    }

    private fun unPinLocation() {
        CoroutineScope(Dispatchers.IO).launch {
            deletePinnedLocationUseCase(
                AbstractQuery.LatLng(
                    currentWeatherParams.lat,
                    currentWeatherParams.lon
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
        updateForecastParams(
            CurrentWeatherUseCase.CurrentWeatherParams(
                latLng.lat, latLng.lng,
                networkAvailableCallback.isNetworkAvailable()
            )
        )
    }

    private fun updateWeatherParams(latLng: AbstractQuery.LatLng) {
        currentWeatherParams =
            CurrentWeatherUseCase.CurrentWeatherParams(
                latLng.lat, latLng.lng,
                networkAvailableCallback.isNetworkAvailable()
            )

    }
}
