package com.app.weather.presentation.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.weather.data.db.entity.CitiesForSearchEntity
import com.app.weather.domain.usecase.SearchCitiesUseCase
import com.app.weather.presentation.core.BaseViewModel
import com.app.weather.presentation.core.BaseViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Fawzy
 */

@HiltViewModel
class SearchViewModel @Inject internal constructor(
    private val useCase: SearchCitiesUseCase
) : BaseViewModel() {

    private val cityNameData = StringBuilder("")

    val viewState: MutableLiveData<BaseViewState<List<CitiesForSearchEntity>>> = MutableLiveData()


    fun onTextChange(text: String?) {
        cityNameData.setLength(0)
        cityNameData.append(text)

        viewModelScope.launch(Dispatchers.IO) {
            useCase(cityNameData.toString()).collect { viewState.postValue(it) }
        }
    }
}
