package com.app.weather.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.app.weather.domain.repositories.CurrentLatLngRepository
import com.app.weather.presentation.core.Constants
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


/**
 * Class that requests token on the remote data source.
 */

class CurrentLatLngRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>) :
    CurrentLatLngRepository {
    private object PreferencesKeys {
        val LAT_LNG_KEY = stringPreferencesKey("latlng")
    }

    override suspend fun getLatLng() = dataStore.data.catch {
        getPreferences(it)
    }.map {
        val latLng = it[PreferencesKeys.LAT_LNG_KEY]
         if (latLng != null) {
            val latLngSplit = latLng.split(":")
            LatLng(latLngSplit[0].toDouble(), latLngSplit[1].toDouble())
        } else LatLng(
            Constants.DataStore.DEFAULT_LAT,
            Constants.DataStore.DEFAULT_LONG
        )
    }

    override suspend fun setLatLng(latlng: String) {
        dataStore.edit {
            it[PreferencesKeys.LAT_LNG_KEY] = latlng
        }
    }

    private suspend fun FlowCollector<Preferences>.getPreferences(it: Throwable) {
        // dataStore.data throws an IOException when an error is encountered when reading data
        if (it is IOException) emit(emptyPreferences())
        else throw it
    }
}