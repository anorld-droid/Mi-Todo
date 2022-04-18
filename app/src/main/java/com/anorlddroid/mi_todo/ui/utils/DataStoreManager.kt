package com.anorlddroid.mi_todo.ui.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


const val USER_SETTINGS = "Current Selected Theme"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_SETTINGS)

class DataStoreManager(val context: Context) {

    companion object {
        val THEME = stringPreferencesKey("THEME")
        val HIDE = stringPreferencesKey("HIDE")
        val SNOOZE = intPreferencesKey("SNOOZE")
    }

    suspend fun saveHideToDataStore(hide: String) {
        context.dataStore.edit {
            it[HIDE] = hide
        }
    }

    val getHideFromDataStore: Flow<String?>
        get() = context.dataStore.data.map {
            it[HIDE]
        }

    suspend fun saveSnoozeToDataStore(snooze: Int) {
        context.dataStore.edit {
            it[SNOOZE] = snooze
        }
    }

    val getSnoozeFromDataStore: Flow<Int?>
        get() = context.dataStore.data.map {
            it[SNOOZE]
        }

    suspend fun saveThemeToDataStore(option: String) {
        context.dataStore.edit {
            it[THEME] = option
        }
    }

    val getThemeFromDataStore: Flow<String?>
        get() = context.dataStore.data.map {
            it[THEME]
        }
}