package com.app.weather.presentation.home

import androidx.databinding.ObservableField
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.algolia.search.saas.AbstractQuery
import com.app.weather.data.db.entity.CurrentWeatherEntity
import com.app.weather.data.db.entity.LocationEntity
import com.app.weather.domain.usecase.CurrentWeatherUseCase
import com.app.weather.domain.usecase.GetPinnedLocationsUseCase
import com.app.weather.domain.usecase.SaveCurrentLocationUseCase
import com.app.weather.presentation.core.BaseViewModel
import com.app.weather.presentation.core.BaseViewState
import com.app.weather.presentation.core.BaseVmFragment
import com.app.weather.presentation.core.Constants
import com.app.weather.utils.extensions.logE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Fawzy
 */

@HiltViewModel
class HomeFragmentViewModel @Inject internal constructor(
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    private val getPinnedLocationsUseCase: GetPinnedLocationsUseCase,
    private val saveCurrentLocationUseCase: SaveCurrentLocationUseCase,
    private val networkAvailableCallback: BaseVmFragment.NetworkAvailableCallback
) : BaseViewModel() {
    var pinnedLocations: ObservableField<List<LocationEntity>> = ObservableField(emptyList())
    private lateinit var currentWeatherParams: CurrentWeatherUseCase.CurrentWeatherParams

    val currentWeatherViewState: MutableLiveData<BaseViewState<CurrentWeatherEntity>> =
        MutableLiveData()

    val pinnedLocationsWeatherViewState: MediatorLiveData<ArrayList<CurrentWeatherEntity>> =
        MediatorLiveData()

    private fun requestCurrentWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            currentWeatherUseCase(currentWeatherParams).collect {
                currentWeatherViewState.postValue(it)
            }
        }
    }


    fun requestPinnedLocations(clearOldData: Boolean) {
        clearOldLocationsData(clearOldData)
        viewModelScope.launch(Dispatchers.IO) {
            getPinnedLocationsUseCase().collect {
                pinnedLocations.set(it)
                pinnedLocations.get()?.forEach { requestLocationEntityBasedWeather(it) }
            }
        }
    }

    private fun clearOldLocationsData(clearOldData: Boolean) {
        if (clearOldData)
            pinnedLocationsWeatherViewState.value = ArrayList()
    }


    private fun requestLocationEntityBasedWeather(locationEntity: LocationEntity) {
        logE("lat: for pinned ${locationEntity.lat}, long: ${locationEntity.lng}")
        viewModelScope.launch(Dispatchers.IO) {
            currentWeatherUseCase(getWeatherParams(locationEntity.latLang())).collect {
                if (!it.isLoading()) {
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
    }

    private fun saveCurrentLocationCoordinates(latLng: AbstractQuery.LatLng) =
        viewModelScope.launch(Dispatchers.IO) {
            saveCurrentLocationUseCase(latLng)
        }

    fun updateWeatherParams(latLng: AbstractQuery.LatLng) {
        val params = getWeatherParams(latLng)
        currentWeatherParams = params
        saveCurrentLocationCoordinates(latLng)
        requestCurrentWeather()
    }


    private fun getWeatherParams(latLng: AbstractQuery.LatLng) =
        CurrentWeatherUseCase.CurrentWeatherParams(
            latLng.lat, latLng.lng,
            networkAvailableCallback.isNetworkAvailable()
        )
}