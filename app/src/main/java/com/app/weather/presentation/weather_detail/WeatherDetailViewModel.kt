package com.app.weather.presentation.weather_detail

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.app.weather.presentation.core.BaseViewModel
import com.app.weather.data.db.entity.ForecastEntity
import com.app.weather.data.datasource.forecast.ForecastLocalDataSource
import com.app.weather.domain.model.ListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Fawzy
 */

@HiltViewModel
class WeatherDetailViewModel @Inject constructor(
    private val forecastLocalDataSource: ForecastLocalDataSource
) : BaseViewModel() {

    var weatherItem: ObservableField<ListItem> = ObservableField()
    var selectedDayDate: String? = null
    var selectedDayForecastLiveData: MediatorLiveData<List<ListItem>> = MediatorLiveData()
    init {
        val dataSource=forecastLocalDataSource.getForecast()
        selectedDayForecastLiveData.addSource(dataSource) {
            selectedDayForecastLiveData.removeSource(selectedDayForecastLiveData)
            selectedDayForecastLiveData.postValue(it.list?.filter { item ->
                item.dtTxt?.substringBefore(" ") == selectedDayDate
            })
        }
    }

}
