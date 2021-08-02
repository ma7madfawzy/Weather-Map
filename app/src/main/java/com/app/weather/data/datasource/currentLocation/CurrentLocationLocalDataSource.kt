package com.app.weather.data.datasource.currentLocation

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.algolia.search.saas.AbstractQuery
import com.app.weather.presentation.core.Constants
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val USER_PREFERENCES_NAME = "DataStore"

/**
 * The preferencesDataStore delegate ensures that we have a single instance of DataStore
 **/
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCES_NAME)

class CurrentLocationLocalDataSource constructor(context: Context) {
    private val dataStore = context.dataStore

    //preference keys
    private object PreferencesKeys {
        val locationCoordinatesKey = stringPreferencesKey("locationCoordinates")
        val locationNameKey = stringPreferencesKey("locationName")
    }

    suspend fun loadLocationTitle() = dataStore.data.catch {
        getPreferences(it)
    }.map {
        it[PreferencesKeys.locationNameKey] ?: "DEFAULT"
    }


    suspend fun saveLocationTitle(location: String) {
        dataStore.edit {
            it[PreferencesKeys.locationNameKey] = location
        }
    }

    suspend fun loadLocationCoordinates() = dataStore.data.catch {
        getPreferences(it)
    }.map {
        val string = it[PreferencesKeys.locationCoordinatesKey]
        string?.let { nonNullString ->
            AbstractQuery.LatLng(
                nonNullString.substringBefore(":").toDouble(),
                nonNullString.substringAfter(":").toDouble()
            )
        } ?: run {
            AbstractQuery.LatLng(
                Constants.DataStore.DEFAULT_LAT,
                Constants.DataStore.DEFAULT_LONG
            )
        }
    }


    suspend fun saveCoordinates(location: AbstractQuery.LatLng) {
        dataStore.edit {
            it[PreferencesKeys.locationCoordinatesKey] = "${location.lat}:${location.lng}"
        }
    }

    private suspend fun FlowCollector<Preferences>.getPreferences(it: Throwable) {
        // dataStore.data throws an IOException when an error is encountered when reading data
        if (it is IOException) emit(emptyPreferences())
        else throw it
    }
}