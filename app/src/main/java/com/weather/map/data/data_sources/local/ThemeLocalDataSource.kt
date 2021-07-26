package com.weather.map.data.data_sources.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val USER_PREFERENCES_NAME = "DataStore"

/**
 * The preferencesDataStore delegate ensures that we have a single instance of DataStore
 **/
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCES_NAME)

class ThemeLocalDataSource constructor(context: Context) {
    private val DEFAULT_THEME: Int = 2
    private val dataStore = context.dataStore

    //preference keys
    private object PreferencesKeys {
        val THEME_KEY = intPreferencesKey("whichTheme")
    }

    suspend fun getWhichTheme() = dataStore.data.catch {
        getPreferences(it)
    }.map {
        it[PreferencesKeys.THEME_KEY] ?: DEFAULT_THEME
    }


    suspend fun setWhichTheme(which: Int) {
        dataStore.edit {
            it[PreferencesKeys.THEME_KEY] = which
        }
    }

    private suspend fun FlowCollector<Preferences>.getPreferences(it: Throwable) {
        // dataStore.data throws an IOException when an error is encountered when reading data
        if (it is IOException) emit(emptyPreferences())
        else throw it
    }
}