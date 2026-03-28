package com.aiapp.data.local

import android.content.Context
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONObject

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
        val THEME_COLOR = stringPreferencesKey("theme_color")
        val TELEGRAM_WEBHOOK = stringPreferencesKey("telegram_webhook")
        val TOKEN_COUNT = intPreferencesKey("token_count")
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

    val themeColor: Flow<String> = dataStore.data.map { preferences ->
        preferences[THEME_COLOR] ?: "system"
    }

    val telegramWebhook: Flow<String> = dataStore.data.map { preferences ->
        preferences[TELEGRAM_WEBHOOK] ?: ""
    }

    val tokenCount: Flow<Int> = dataStore.data.map { preferences ->
        preferences[TOKEN_COUNT] ?: 0
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

    suspend fun setThemeColor(color: String) {
        dataStore.edit { preferences ->
            preferences[THEME_COLOR] = color
        }
    }

    suspend fun setTelegramWebhook(webhook: String) {
        dataStore.edit { preferences ->
            preferences[TELEGRAM_WEBHOOK] = webhook
        }
    }

    suspend fun addTokenCount(tokens: Int) {
        dataStore.edit { preferences ->
            val current = preferences[TOKEN_COUNT] ?: 0
            preferences[TOKEN_COUNT] = current + tokens
        }
    }

    suspend fun resetTokenCount() {
        dataStore.edit { preferences ->
            preferences[TOKEN_COUNT] = 0
        }
    }

    suspend fun clearCache() {
        dataStore.edit { preferences ->
            preferences[TOKEN_COUNT] = 0
        }
    }

    suspend fun exportSettings(): String {
        val prefs = dataStore.data.first()
        val json = JSONObject()
        prefs.asMap().forEach { (key, value) ->
            when (value) {
                is Boolean -> json.put(key.name, value)
                is String -> json.put(key.name, value)
                is Int -> json.put(key.name, value)
                is Float -> json.put(key.name, value.toDouble())
            }
        }
        return json.toString()
    }

    suspend fun importSettings(jsonStr: String) {
        try {
            val json = JSONObject(jsonStr)
            dataStore.edit { prefs ->
                json.keys().forEach { key ->
                    val value = json.get(key)
                    when (value) {
                        is Boolean -> prefs[booleanPreferencesKey(key)] = value
                        is String -> prefs[stringPreferencesKey(key)] = value
                        is Int -> prefs[intPreferencesKey(key)] = value
                        is Double -> prefs[floatPreferencesKey(key)] = value.toFloat()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getDeviceInfo(): String {
        return buildString {
            appendLine("📱 Устройство")
            appendLine("Модель: ${Build.MANUFACTURER} ${Build.MODEL}")
            appendLine("Android: ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})")
            appendLine("Процессор: ${Build.HARDWARE}")
            appendLine("RAM: ${Runtime.getRuntime().maxMemory() / 1024 / 1024} MB")
        }
    }
}
