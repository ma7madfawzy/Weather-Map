package com.app.weather.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.app.weather.data.db.entity.CitiesForSearchEntity
import com.app.weather.data.db.entity.CoordEntity
import com.app.weather.domain.usecase.SearchCitiesUseCase
import com.app.weather.presentation.core.BaseViewModel
import com.app.weather.presentation.core.BaseViewState
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
    private val useCase: SearchCitiesUseCase
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
}
