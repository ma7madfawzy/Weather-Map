package com.app.weather.presentation.search

import com.app.weather.data.db.entity.CitiesForSearchEntity
import com.app.weather.presentation.core.BaseViewState
import com.app.weather.utils.domain.Status


/**
 * Created by Fawzy
 */

class SearchViewState(
    val status: Status,
    val error: String? = null,
    val data: List<CitiesForSearchEntity>? = null
) : BaseViewState(status, error) {
    fun getSearchResult() = data
}
