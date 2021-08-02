package com.app.weather.di

import com.app.weather.data.repositories.*
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
    fun provideGetPinnedLocationsUseCase(currentLatLngRepositoryImpl: PinnedLocationsRepositoryImpl) =
        GetPinnedLocationsUseCase(currentLatLngRepositoryImpl)

    @Provides
    @Singleton
    fun provideInsertPinnedLocationUseCase(currentLatLngRepositoryImpl: PinnedLocationsRepositoryImpl) =
        InsertPinnedLocationUseCase(currentLatLngRepositoryImpl)
    @Provides
    @Singleton
    fun provideDeletePinnedLocationUseCase(currentLatLngRepositoryImpl: PinnedLocationsRepositoryImpl) =
        DeletePinnedLocationUseCase(currentLatLngRepositoryImpl)

    @Provides
    @Singleton
    fun provideLoadLocationToPrefUseCase(repo: CurrentLocationRepositoryImpl) =
        LoadCurrentLocationUseCase(repo)
    @Provides
    @Singleton
    fun provideSaveLocationToPrefUseCase(repo: CurrentLocationRepositoryImpl) =
        SaveCurrentLocationUseCase(repo)

}