package com.app.weather.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.app.weather.data.db.entity.CurrentWeatherEntity
import com.app.weather.data.db.entity.LocationEntity
import com.app.weather.domain.usecase.CurrentWeatherUseCase
import com.app.weather.domain.usecase.GetPinnedLocationsUseCase
import com.app.weather.presentation.core.BaseViewModel
import com.app.weather.presentation.core.BaseViewState
import com.app.weather.presentation.core.BaseVmFragment
import com.app.weather.presentation.core.Constants
import com.app.weather.utils.extensions.logE
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Fawzy
 */

@HiltViewModel
class HomeFragmentViewModel @Inject internal constructor(
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    private val getPinnedLocationsUseCase: GetPinnedLocationsUseCase,
    private val networkAvailableCallback: BaseVmFragment.NetworkAvailableCallback
) : BaseViewModel() {

    private val currentWeatherParams: MutableLiveData<CurrentWeatherUseCase.CurrentWeatherParams> =
        MutableLiveData()

    private val pinnedLocationsWeatherParams: MutableLiveData<CurrentWeatherUseCase.CurrentWeatherParams> =
        MutableLiveData()

    val currentWeatherViewState: LiveData<BaseViewState<CurrentWeatherEntity>> =
        currentWeatherParams.switchMap { currentWeatherUseCase(it) }

    val pinnedLocationsWeatherViewState:
            MutableLiveData<BaseViewState<ArrayList<CurrentWeatherEntity>>> = MutableLiveData()

    init {
        logE("start of init")
        getPinnedLocationsUseCase().value?.forEach { getPinnedLocationWeather(it) }
        logE("end of init")
    }

    private fun getPinnedLocationWeather(it: LocationEntity) {
        //take each location and fetch the weather then add it to pinnedLocationsWeatherViewState
        val oldWeatherEntityStateListData = pinnedLocationsWeatherViewState.value
        oldWeatherEntityStateListData?.data?.add(
            currentWeatherUseCase(
                getWeatherParams(
                    it.latLang()
                )
            ).value?.data!!
        )
        pinnedLocationsWeatherViewState.value = oldWeatherEntityStateListData
    }

    fun updateWeatherParams(latLng: LatLng) {
        setCurrentWeatherParams(
            getWeatherParams(latLng)
        )
    }

    private fun getWeatherParams(latLng: LatLng) =
        CurrentWeatherUseCase.CurrentWeatherParams(
            latLng.latitude.toString(), latLng.longitude.toString(),
            networkAvailableCallback.isNetworkAvailable(), Constants.Coords.METRIC
        )

    private fun setCurrentWeatherParams(params: CurrentWeatherUseCase.CurrentWeatherParams) {
        if (currentWeatherParams.value == params) return
        currentWeatherParams.postValue(params)
    }
}
