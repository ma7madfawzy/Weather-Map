package com.app.weather.di

import android.content.Context
import android.os.Environment
import com.algolia.search.saas.places.PlacesClient
import com.app.weather.data.api.DefaultRequestInterceptor
import com.app.weather.data.api.WeatherAppAPI
import com.app.weather.presentation.core.BaseVmFragment
import com.app.weather.presentation.core.Constants
import com.app.weather.utils.extensions.networkAvailable
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideCache(): Cache =
        Cache(Environment.getDownloadCacheDirectory(), 10 * 1024 * 1024)

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .addInterceptor(DefaultRequestInterceptor())
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)

    @Provides
    @Singleton
    fun provideRetrofit(
        moshi: Moshi,
        okHttpClientBuilder: OkHttpClient.Builder,
        cache: Cache,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.NetworkService.BASE_URL)
        .client(okHttpClientBuilder.cache(cache).build())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideService(retrofit: Retrofit): WeatherAppAPI =
        retrofit.create(WeatherAppAPI::class.java)

    @Provides
    @Singleton
    fun providePlacesClient(): PlacesClient =
        PlacesClient(
            Constants.AlgoliaKeys.APPLICATION_ID,
            Constants.AlgoliaKeys.SEARCH_API_KEY
        )

    @Provides
    @Singleton
    fun provideNetworkAvailableCallback(@ApplicationContext context: Context) =
        object : BaseVmFragment.NetworkAvailableCallback {
            override fun isNetworkAvailable() =
                networkAvailable(context)
        }


}