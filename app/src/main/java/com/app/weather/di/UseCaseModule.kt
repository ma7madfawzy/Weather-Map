package com.app.weather.di

import com.app.weather.data.repositories.PinnedLocationsRepositoryImpl
import com.app.weather.data.repositories.CurrentWeatherRepositoryImpl
import com.app.weather.data.repositories.ForecastRepositoryImpl
import com.app.weather.data.repositories.SearchCitiesRepositoryImpl
import com.app.weather.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UseCaseModule {

    @Provides
    @Singleton
    fun provideCurrentWeatherUseCase(currentWeatherRepository: CurrentWeatherRepositoryImpl) =
        CurrentWeatherUseCase(currentWeatherRepository)

    @Provides
    @Singleton
    fun provideForecastUseCase(forecastRepository: ForecastRepositoryImpl) =
        ForecastUseCase(forecastRepository)

    @Provides
    @Singleton
    fun provideSearchCitiesUseCase(searchCitiesRepository: SearchCitiesRepositoryImpl) =
        SearchCitiesUseCase(searchCitiesRepository)

    @Provides
    @Singleton
    fun provideGetCurrentCoordUseCase(currentLatLngRepositoryImpl: PinnedLocationsRepositoryImpl) =
        GetPinnedLocationsUseCase(currentLatLngRepositoryImpl)

    @Provides
    @Singleton
    fun provideSetCurrentCoordUseCase(currentLatLngRepositoryImpl: PinnedLocationsRepositoryImpl) =
        InsertPinnedLocationUseCase(currentLatLngRepositoryImpl)

}