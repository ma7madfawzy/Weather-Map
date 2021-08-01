package com.app.weather.presentation.weather_detail

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.weather.data.datasource.forecast.ForecastLocalDataSource
import com.app.weather.domain.model.ListItem
import com.app.weather.domain.usecase.CurrentWeatherUseCase
import com.app.weather.domain.usecase.ForecastUseCase
import com.app.weather.presentation.core.BaseViewModel
import com.app.weather.presentation.core.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Fawzy
 */
@HiltViewModel
class WeatherDetailViewModel @Inject constructor(
    private val forecastUseCase: ForecastUseCase
) : BaseViewModel() {
    var weatherItem: ObservableField<ListItem> = ObservableField()
    var selectedDayDate: String? = null
    var selectedDayForecastLiveData: MutableLiveData<List<ListItem>> = MutableLiveData()

    fun initWithArgs(args: WeatherDetailFragmentArgs) {
        weatherItem.set(args.weatherItem)
        selectedDayDate = args.weatherItem.dtTxt?.substringBefore(" ")
        requestForecast(args)
    }


    private fun requestForecast(args: WeatherDetailFragmentArgs) {
        viewModelScope.launch(Dispatchers.IO) {
            forecastUseCase(
                CurrentWeatherUseCase.CurrentWeatherParams(
                args.forecastEntity.lat!!, args.forecastEntity.lng!!,
                false, Constants.Coords.METRIC
            )).collect {
                selectedDayForecastLiveData.postValue(it.data?.list?.filter { item ->
                    item.dtTxt?.substringBefore(" ") == selectedDayDate
                })
            }
        }
    }

}
