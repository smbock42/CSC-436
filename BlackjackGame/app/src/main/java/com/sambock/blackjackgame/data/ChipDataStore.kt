package com.sambock.blackjackgame.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create a DataStore instance using the preferencesDataStore delegate
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ChipDataStore(private val context: Context) {
    companion object {
        private val CHIP_COUNT = intPreferencesKey("chip_count")
        private const val DEFAULT_CHIPS = 1000
    }

    val chipCount: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[CHIP_COUNT] ?: DEFAULT_CHIPS
        }

    suspend fun saveChipCount(count: Int) {
        context.dataStore.edit { preferences ->
            preferences[CHIP_COUNT] = count
        }
    }

    suspend fun resetChips() {
        context.dataStore.edit { preferences ->
            preferences[CHIP_COUNT] = DEFAULT_CHIPS
        }
    }
}
