package com.aiapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val USERNAME = stringPreferencesKey("username")
        val SELECTED_MODEL = stringPreferencesKey("selected_model")
        val AUTO_SEND_VOICE = booleanPreferencesKey("auto_send_voice")
        val TEXT_SIZE = floatPreferencesKey("text_size")
        val TTS_ENABLED = booleanPreferencesKey("tts_enabled")
    }

    val darkTheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DARK_THEME] ?: false
    }

    val username: Flow<String> = dataStore.data.map { preferences ->
        preferences[USERNAME] ?: "Пользователь"
    }

    val selectedModel: Flow<String> = dataStore.data.map { preferences ->
        preferences[SELECTED_MODEL] ?: "assistant"
    }

    val autoSendVoice: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[AUTO_SEND_VOICE] ?: false
    }

    val textSize: Flow<Float> = dataStore.data.map { preferences ->
        preferences[TEXT_SIZE] ?: 16f
    }

    val ttsEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[TTS_ENABLED] ?: false
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_THEME] = enabled
        }
    }

    suspend fun setUsername(name: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME] = name
        }
    }

    suspend fun setSelectedModel(model: String) {
        dataStore.edit { preferences ->
            preferences[SELECTED_MODEL] = model
        }
    }

    suspend fun setAutoSendVoice(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[AUTO_SEND_VOICE] = enabled
        }
    }

    suspend fun setTextSize(size: Float) {
        dataStore.edit { preferences ->
            preferences[TEXT_SIZE] = size
        }
    }

    suspend fun setTtsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[TTS_ENABLED] = enabled
        }
    }
}
