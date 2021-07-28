package com.app.weather.presentation.dashboard.forecast

import androidx.databinding.ObservableField
import com.app.weather.presentation.core.BaseViewModel
import com.app.weather.domain.model.ListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Fawzy
 */

@HiltViewModel
class ForecastItemViewModel @Inject internal constructor() : BaseViewModel() {
    var item = ObservableField<ListItem>()
}
