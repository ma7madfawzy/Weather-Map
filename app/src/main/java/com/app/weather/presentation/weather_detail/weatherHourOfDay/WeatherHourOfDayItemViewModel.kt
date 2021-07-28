package com.app.weather.presentation.weather_detail.weatherHourOfDay

import androidx.databinding.ObservableField
import com.app.weather.presentation.core.BaseViewModel
import com.app.weather.domain.model.ListItem
import javax.inject.Inject

/**
 * Created by Fawzy
 */

class WeatherHourOfDayItemViewModel @Inject internal constructor() : BaseViewModel() {
    var item = ObservableField<ListItem>()
}
