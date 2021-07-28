package com.app.weather.presentation.search.result

import androidx.databinding.ObservableField
import com.app.weather.data.db.entity.CitiesForSearchEntity
import com.app.weather.presentation.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Fawzy
 */

@HiltViewModel
class SearchResultItemViewModel @Inject internal constructor() : BaseViewModel() {
    var item = ObservableField<CitiesForSearchEntity>()
}
