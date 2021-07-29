package com.app.weather.di

import com.app.weather.data.datasource.currentWeather.CurrentWeatherLocalDataSource
import com.app.weather.data.datasource.currentWeather.CurrentWeatherRemoteDataSource
import com.app.weather.data.datasource.forecast.ForecastLocalDataSource
import com.app.weather.data.datasource.forecast.ForecastRemoteDataSource
import com.app.weather.data.datasource.pinned_locations.PinnedLocationsDataSource
import com.app.weather.data.datasource.searchCities.SearchCitiesLocalDataSource
import com.app.weather.data.datasource.searchCities.SearchCitiesRemoteDataSource
import com.app.weather.data.repositories.PinnedLocationsRepositoryImpl
import com.app.weather.data.repositories.CurrentWeatherRepositoryImpl
import com.app.weather.data.repositories.ForecastRepositoryImpl
import com.app.weather.data.repositories.SearchCitiesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCurrentWeatherRepository(
        currentWeatherRemoteDataSource: CurrentWeatherRemoteDataSource,
        currentWeatherLocalDataSource: CurrentWeatherLocalDataSource,
    ) = CurrentWeatherRepositoryImpl(currentWeatherRemoteDataSource, currentWeatherLocalDataSource)

    @Provides
    @Singleton
    fun provideForecastRepository(
        forecastRemoteDataSource: ForecastRemoteDataSource,
        forecastLocalDataSource: ForecastLocalDataSource,
    ) = ForecastRepositoryImpl(forecastRemoteDataSource, forecastLocalDataSource)

    @Provides
    @Singleton
    fun provideSearchCitiesRepository(
        searchCitiesRemoteDataSource: SearchCitiesRemoteDataSource,
        searchCitiesLocalDataSource: SearchCitiesLocalDataSource,
    ) = SearchCitiesRepositoryImpl(searchCitiesRemoteDataSource, searchCitiesLocalDataSource)

    @Provides
    @Singleton
    fun provideCurrentLatLngRepository(dataSource: PinnedLocationsDataSource) =
        PinnedLocationsRepositoryImpl(dataSource)
}