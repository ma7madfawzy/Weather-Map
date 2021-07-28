package com.app.weather.presentation.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.app.weather.data.db.entity.CurrentWeatherEntity
import com.app.weather.data.db.entity.ForecastEntity
import com.app.weather.domain.usecase.CurrentWeatherUseCase
import com.app.weather.domain.usecase.ForecastUseCase
import com.app.weather.domain.usecase.GetCurrentLatLngUseCase
import com.app.weather.presentation.core.BaseViewModel
import com.app.weather.presentation.core.BaseViewState
import com.app.weather.presentation.core.BaseVmFragment
import com.app.weather.presentation.core.Constants
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Fawzy
 */

@HiltViewModel
class DashboardFragmentViewModel @Inject internal constructor(
    private val forecastUseCase: ForecastUseCase,
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    private val getCurrentLatLngUseCase: GetCurrentLatLngUseCase,
    private val networkAvailableCallback: BaseVmFragment.NetworkAvailableCallback
) : BaseViewModel() {

    private val forecastParams: MutableLiveData<CurrentWeatherUseCase.CurrentWeatherParams> =
        MutableLiveData()
    private val currentWeatherParams: MutableLiveData<CurrentWeatherUseCase.CurrentWeatherParams> =
        MutableLiveData()

    val forecastViewState: LiveData<BaseViewState<ForecastEntity>> =
        forecastParams.switchMap { params ->
            forecastUseCase(params)
        }
    val currentWeatherViewState: LiveData<BaseViewState<CurrentWeatherEntity>> =
        currentWeatherParams.switchMap { params ->
            currentWeatherUseCase(params)
        }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getCurrentLatLngUseCase().collect {
                updateWeatherParams(it)
                updateForecastParams(it)
            }
        }
    }

    private fun updateForecastParams(latLng: LatLng) {
        setForecastParams(
            CurrentWeatherUseCase.CurrentWeatherParams(
                latLng.latitude.toString(), latLng.longitude.toString(),
                networkAvailableCallback.isNetworkAvailable(), Constants.Coords.METRIC
            )
        )
    }

    private fun updateWeatherParams(latLng: LatLng) {
        setCurrentWeatherParams(
            CurrentWeatherUseCase.CurrentWeatherParams(
                latLng.latitude.toString(), latLng.longitude.toString(),
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
