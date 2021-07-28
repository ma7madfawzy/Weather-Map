package com.app.weather.di

import com.app.weather.data.api.WeatherAppAPI
import com.app.weather.data.datasource.currentWeather.CurrentWeatherLocalDataSource
import com.app.weather.data.datasource.currentWeather.CurrentWeatherRemoteDataSource
import com.app.weather.data.datasource.forecast.ForecastLocalDataSource
import com.app.weather.data.datasource.forecast.ForecastRemoteDataSource
import com.app.weather.data.db.dao.CurrentWeatherDao
import com.app.weather.data.db.dao.ForecastDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataSourceModule {

    @Provides
    @Singleton
    fun provideCurrentWeatherRemoteDataSource(api: WeatherAppAPI) =
        CurrentWeatherRemoteDataSource(api)

    @Provides
    @Singleton
    fun provideForecastRemoteDataSource(api: WeatherAppAPI) = ForecastRemoteDataSource(api)

    @Provides
    @Singleton
    fun provideCurrentWeatherLocalDataSource(currentWeatherDao: CurrentWeatherDao) =
        CurrentWeatherLocalDataSource(currentWeatherDao)

    @Provides
    @Singleton
    fun provideForecastLocalDataSource(forecastDao: ForecastDao) =
        ForecastLocalDataSource(forecastDao)
}