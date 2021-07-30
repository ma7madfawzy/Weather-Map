package com.app.weather.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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
import com.algolia.search.saas.AbstractQuery
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
    var pinnedLocations: List<LocationEntity> = emptyList()
    private val currentWeatherParams: MutableLiveData<CurrentWeatherUseCase.CurrentWeatherParams> =
        MutableLiveData()

    val currentWeatherViewState: LiveData<BaseViewState<CurrentWeatherEntity>> =
        currentWeatherParams.switchMap { currentWeatherUseCase(it) }

    val pinnedLocationsWeatherViewState:
            MediatorLiveData<ArrayList<CurrentWeatherEntity>> = MediatorLiveData()

    fun fetchPinnedLocations() {
        val dataSource = getPinnedLocationsUseCase()
        pinnedLocationsWeatherViewState.addSource(dataSource) {
            pinnedLocationsWeatherViewState.removeSource(dataSource)
            onPinnedLocationLoaded(it)
        }
    }

    private fun onPinnedLocationLoaded(list: List<LocationEntity>) {
        pinnedLocations = list
        pinnedLocations.forEach { getLocationEntityBasedWeather(it) }
    }

    private fun getLocationEntityBasedWeather(locationEntity: LocationEntity) {
        val source = currentWeatherUseCase(getWeatherParams(locationEntity.latLang()))
        pinnedLocationsWeatherViewState.addSource(source) {
            if (!it.isLoading()) {
                pinnedLocationsWeatherViewState.removeSource(source)
                val oldWeatherEntityStateListData: ArrayList<CurrentWeatherEntity> = ArrayList(
                    pinnedLocationsWeatherViewState.value ?: emptyList<CurrentWeatherEntity>()
                )
                it.data?.let { weatherEntity ->
                    oldWeatherEntityStateListData.add(weatherEntity)
                }
                pinnedLocationsWeatherViewState.postValue(oldWeatherEntityStateListData)
            }
        }
    }

    fun updateWeatherParams(latLng: AbstractQuery.LatLng) {
        setCurrentWeatherParams(
            getWeatherParams(latLng)
        )
    }

    private fun getWeatherParams(latLng: AbstractQuery.LatLng) =
        CurrentWeatherUseCase.CurrentWeatherParams(
            latLng.lat, latLng.lng,
            networkAvailableCallback.isNetworkAvailable(), Constants.Coords.METRIC
        )

    private fun setCurrentWeatherParams(params: CurrentWeatherUseCase.CurrentWeatherParams) {
        if (currentWeatherParams.value == params) return
        currentWeatherParams.postValue(params)
    }

    fun getLatByPosition(pos: Int): Double {
        return pinnedLocations[pos].lat ?: 0.0
    }

    fun getLngByPosition(pos: Int): Double {
        return pinnedLocations[pos].lng ?: 0.0
    }
}