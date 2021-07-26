package com.weather.map.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weather.map.data.data_sources.local.ThemeLocalDataSource
import com.weather.map.data.data_sources.remote.TracksDataSource
import com.weather.map.data.data_sources.remote.UserDataSource
import com.weather.map.data.repositories.ThemeRepositoryImpl
import com.weather.map.data.repositories.TracksRepositoryImpl
import com.weather.map.data.repositories.UserRepositoryImpl
import com.weather.map.domain.use_cases.GetThemeUCImpl
import com.weather.map.domain.use_cases.GetTokenUCImpl
import com.weather.map.domain.use_cases.GetTracksUCImpl
import com.weather.map.domain.use_cases.SetThemeUCImpl
import com.weather.map.presentation.home.HomeActivityViewModel
import com.weather.map.presentation.home.details.TrackDetailsViewModel
import com.weather.map.presentation.home.theme_handler.ThemeHandlerViewModel

/**
 * Creates a one off view model factory for the given view model instance.
 */
class ViewModelFactory(private val context: Context? = null) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeActivityViewModel::class.java) -> {
                getHomeActivityViewModel() as T
            }
            modelClass.isAssignableFrom(TrackDetailsViewModel::class.java) -> {
                TrackDetailsViewModel() as T
            }
            modelClass.isAssignableFrom(ThemeHandlerViewModel::class.java) -> {
                getThemeHandlerViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private fun getHomeActivityViewModel(): HomeActivityViewModel {
        val tracksRepository = TracksRepositoryImpl(TracksDataSource())
        val getTokenUC = GetTokenUCImpl(UserRepositoryImpl(UserDataSource()))
        return HomeActivityViewModel(GetTracksUCImpl(getTokenUC, tracksRepository))
    }

    private fun getThemeHandlerViewModel(): ThemeHandlerViewModel? {
        context?.let {
            val themeRepository = ThemeRepositoryImpl(ThemeLocalDataSource(it))
            val getThemeUC = GetThemeUCImpl(themeRepository)
            val setThemeUC = SetThemeUCImpl(themeRepository)
            return ThemeHandlerViewModel(getThemeUC, setThemeUC)
        }
        return null
    }
}