package com.example.roseledger.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Top-level extension property to create the DataStore instance
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

// Enum to represent the available theme options
enum class ThemeSetting {
    SYSTEM,
    LIGHT,
    DARK
}

class ThemePreferences(context: Context) {

    private val appContext = context.applicationContext

    companion object {
        // Use a string key to store the enum's name
        private val THEME_SETTING = stringPreferencesKey("theme_setting")
    }

    // A Flow that emits the current theme setting.
    // It defaults to 'SYSTEM' if no value is stored yet.
    val themeSetting: Flow<ThemeSetting> = appContext.dataStore.data
        .map { preferences ->
            try {
                ThemeSetting.valueOf(preferences[THEME_SETTING] ?: ThemeSetting.SYSTEM.name)
            } catch (e: IllegalArgumentException) {
                ThemeSetting.SYSTEM
            }
        }

    // A suspend function to update the theme preference.
    suspend fun setThemeSetting(theme: ThemeSetting) {
        appContext.dataStore.edit { settings ->
            settings[THEME_SETTING] = theme.name
        }
    }
}