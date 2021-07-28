package com.app.weather.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.app.weather.data.db.entity.CitiesForSearchEntity
import com.app.weather.data.db.entity.CoordEntity
import com.app.weather.domain.usecase.SearchCitiesUseCase
import com.app.weather.domain.usecase.SetCurrentLatLngUseCase
import com.app.weather.presentation.core.BaseViewModel
import com.app.weather.presentation.core.BaseViewState
import com.google.android.gms.maps.model.LatLng
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
class SearchViewModel @Inject internal constructor(
    private val useCase: SearchCitiesUseCase,
    private val setCurrentLatLngUseCase: SetCurrentLatLngUseCase
) : BaseViewModel() {

    private val cityNameData: MutableLiveData<String> = MutableLiveData()

    val viewState: LiveData<BaseViewState<List<CitiesForSearchEntity>>> =
        cityNameData.switchMap { params ->
            useCase(params)
        }

    fun onTextChange(text: String?) {
        if (cityNameData.value == text) {
            return
        }
        cityNameData.postValue(text)
    }

    @ExperimentalCoroutinesApi
    fun saveCoordinates(coordinateEntity: CoordEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            setCurrentLatLngUseCase(
                LatLng(
                    coordinateEntity.lat ?: 0.0,
                    coordinateEntity.lon ?: 0.0
                )
            )
        }
    }
}
