package com.sambock.blackjackgame.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.statsDataStore: DataStore<Preferences> by preferencesDataStore(name = "stats")

class StatsDataStore(private val context: Context) {
    private object PreferencesKeys {
        val TOTAL_HANDS = intPreferencesKey("total_hands")
        val WINS = intPreferencesKey("wins")
        val LOSSES = intPreferencesKey("losses")
        val PUSHES = intPreferencesKey("pushes")
        val BLACKJACKS = intPreferencesKey("blackjacks")
        val BUSTS = intPreferencesKey("busts")
    }

    val statsFlow: Flow<Map<String, Int>> = context.statsDataStore.data
        .map { preferences ->
            mapOf(
                "total_hands" to (preferences[PreferencesKeys.TOTAL_HANDS] ?: 0),
                "wins" to (preferences[PreferencesKeys.WINS] ?: 0),
                "losses" to (preferences[PreferencesKeys.LOSSES] ?: 0),
                "pushes" to (preferences[PreferencesKeys.PUSHES] ?: 0),
                "blackjacks" to (preferences[PreferencesKeys.BLACKJACKS] ?: 0),
                "busts" to (preferences[PreferencesKeys.BUSTS] ?: 0)
            )
        }

    suspend fun updateStats(
        totalHands: Int,
        wins: Int,
        losses: Int,
        pushes: Int,
        blackjacks: Int,
        busts: Int
    ) {
        context.statsDataStore.edit { preferences ->
            preferences[PreferencesKeys.TOTAL_HANDS] = totalHands
            preferences[PreferencesKeys.WINS] = wins
            preferences[PreferencesKeys.LOSSES] = losses
            preferences[PreferencesKeys.PUSHES] = pushes
            preferences[PreferencesKeys.BLACKJACKS] = blackjacks
            preferences[PreferencesKeys.BUSTS] = busts
        }
    }

    suspend fun resetStats() {
        context.statsDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}