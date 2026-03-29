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
        val APP_LANGUAGE = stringPreferencesKey("app_language")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val MESSAGE_NOTIFICATIONS = booleanPreferencesKey("message_notifications")
        val SHOW_TYPING_INDICATOR = booleanPreferencesKey("show_typing_indicator")
        val AUTO_SCROLL = booleanPreferencesKey("auto_scroll")
        val CHAT_COUNT = intPreferencesKey("chat_count")
        val MESSAGE_COUNT = intPreferencesKey("message_count")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        val SHOW_TIMESTAMPS = booleanPreferencesKey("show_timestamps")
        val AI_TEMPERATURE = floatPreferencesKey("ai_temperature")
        val MAX_TOKENS = intPreferencesKey("max_tokens")
        val SYSTEM_PROMPT = stringPreferencesKey("system_prompt")
        val STREAMING_RESPONSE = booleanPreferencesKey("streaming_response")
        val PINNED_CHATS = stringPreferencesKey("pinned_chats")
        val MUTED_CHATS = stringPreferencesKey("muted_chats")
        val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val LAST_SYNC_TIME = longPreferencesKey("last_sync_time")
        val AUTO_BACKUP_ENABLED = booleanPreferencesKey("auto_backup_enabled")
        val BACKUP_PATH = stringPreferencesKey("backup_path")
        val BUBBLE_STYLE = stringPreferencesKey("bubble_style")
        val FONT_FAMILY = stringPreferencesKey("font_family")
        val ANIMATION_SPEED = floatPreferencesKey("animation_speed")
        val HAPTIC_FEEDBACK = booleanPreferencesKey("haptic_feedback")
        val SAVE_CHATS = booleanPreferencesKey("save_chats")
        val LAST_MODEL = stringPreferencesKey("last_model")
        val CONVERSATION_TITLE = booleanPreferencesKey("conversation_title")
        val APP_LOCK_PIN = stringPreferencesKey("app_lock_pin")
        val APP_LOCK_ENABLED = booleanPreferencesKey("app_lock_enabled")
        val CHAT_BACKGROUND = stringPreferencesKey("chat_background")
        val QUICK_REPLIES = stringPreferencesKey("quick_replies")
        val AUTO_DELETE_DAYS = intPreferencesKey("auto_delete_days")
        val MARKDOWN_ENABLED = booleanPreferencesKey("markdown_enabled")
        val CODE_HIGHLIGHT = booleanPreferencesKey("code_highlight")
        val NIGHT_MODE_START = stringPreferencesKey("night_mode_start")
        val NIGHT_MODE_END = stringPreferencesKey("night_mode_end")
        val NIGHT_MODE_AUTO = booleanPreferencesKey("night_mode_auto")
        val WIDGET_ENABLED = booleanPreferencesKey("widget_enabled")
        val ENCRYPT_ENABLED = booleanPreferencesKey("encrypt_enabled")
        val VOICE_REPLY = booleanPreferencesKey("voice_reply")
        val AUTO_SPACE = booleanPreferencesKey("auto_space")
        val DOUBLE_SPACE = booleanPreferencesKey("double_space")
        val SWIPE_REPLY = booleanPreferencesKey("swipe_reply")
        val MESSAGE_COUNT_PER_PAGE = intPreferencesKey("message_count_per_page")
        val LAST_CLEANUP = longPreferencesKey("last_cleanup")
        val ANIMATION_ENABLED = booleanPreferencesKey("animation_enabled")
        val SWIPE_TO_DELETE = booleanPreferencesKey("swipe_to_delete")
        val LONG_PRESS_MENU = booleanPreferencesKey("long_press_menu")
        val AUTO_SAVE_DRAFT = booleanPreferencesKey("auto_save_draft")
        val SHOW_WORD_COUNT = booleanPreferencesKey("show_word_count")
        val AUTO_EXPAND_URLS = booleanPreferencesKey("auto_expand_urls")
        val MESSAGE_BUBBLE_ROUNDNESS = floatPreferencesKey("message_bubble_roundness")
        val DEFAULT_CHAT_BUBBLE_COLOR = stringPreferencesKey("default_chat_bubble_color")
        val AI_MESSAGE_ALIGNMENT = stringPreferencesKey("ai_message_alignment")
        val MAX_MESSAGE_LENGTH = intPreferencesKey("max_message_length")
        val CONFIRM_BEFORE_SEND = booleanPreferencesKey("confirm_before_send")
        val SHOW_DELIVERY_STATUS = booleanPreferencesKey("show_delivery_status")
        val QUICK_SEND_BUTTON = booleanPreferencesKey("quick_send_button")
        val HIDE_KEYBOARD_ON_SEND = booleanPreferencesKey("hide_keyboard_on_send")
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

    val appLanguage: Flow<String> = dataStore.data.map { preferences ->
        preferences[APP_LANGUAGE] ?: "ru"
    }

    val notificationsEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[NOTIFICATIONS_ENABLED] ?: true
    }

    val messageNotifications: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[MESSAGE_NOTIFICATIONS] ?: false
    }

    val showTypingIndicator: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SHOW_TYPING_INDICATOR] ?: true
    }

    val autoScroll: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[AUTO_SCROLL] ?: true
    }

    val chatCount: Flow<Int> = dataStore.data.map { preferences ->
        preferences[CHAT_COUNT] ?: 0
    }

    val messageCount: Flow<Int> = dataStore.data.map { preferences ->
        preferences[MESSAGE_COUNT] ?: 0
    }

    val soundEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SOUND_ENABLED] ?: true
    }

    val vibrationEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[VIBRATION_ENABLED] ?: true
    }

    val showTimestamps: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SHOW_TIMESTAMPS] ?: true
    }

    val aiTemperature: Flow<Float> = dataStore.data.map { preferences ->
        preferences[AI_TEMPERATURE] ?: 0.7f
    }

    val maxTokens: Flow<Int> = dataStore.data.map { preferences ->
        preferences[MAX_TOKENS] ?: 1024
    }

    val systemPrompt: Flow<String> = dataStore.data.map { preferences ->
        preferences[SYSTEM_PROMPT] ?: "Ты - полезный AI ассистент. Отвечай на русском языке."
    }

    val streamingResponse: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[STREAMING_RESPONSE] ?: true
    }

    val firstLaunch: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[FIRST_LAUNCH] ?: true
    }

    val onboardingCompleted: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[ONBOARDING_COMPLETED] ?: false
    }

    val lastSyncTime: Flow<Long> = dataStore.data.map { preferences ->
        preferences[LAST_SYNC_TIME] ?: 0L
    }

    val autoBackupEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[AUTO_BACKUP_ENABLED] ?: false
    }

    val backupPath: Flow<String> = dataStore.data.map { preferences ->
        preferences[BACKUP_PATH] ?: ""
    }

    val bubbleStyle: Flow<String> = dataStore.data.map { preferences ->
        preferences[BUBBLE_STYLE] ?: "modern"
    }

    val fontFamily: Flow<String> = dataStore.data.map { preferences ->
        preferences[FONT_FAMILY] ?: "default"
    }

    val animationSpeed: Flow<Float> = dataStore.data.map { preferences ->
        preferences[ANIMATION_SPEED] ?: 1.0f
    }

    val hapticFeedback: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[HAPTIC_FEEDBACK] ?: true
    }

    val saveChats: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SAVE_CHATS] ?: true
    }

    val lastModel: Flow<String> = dataStore.data.map { preferences ->
        preferences[LAST_MODEL] ?: "llama3"
    }

    val conversationTitle: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[CONVERSATION_TITLE] ?: true
    }

    val appLockPin: Flow<String> = dataStore.data.map { preferences ->
        preferences[APP_LOCK_PIN] ?: ""
    }

    val appLockEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[APP_LOCK_ENABLED] ?: false
    }

    val chatBackground: Flow<String> = dataStore.data.map { preferences ->
        preferences[CHAT_BACKGROUND] ?: "default"
    }

    val quickReplies: Flow<String> = dataStore.data.map { preferences ->
        preferences[QUICK_REPLIES] ?: ""
    }

    val autoDeleteDays: Flow<Int> = dataStore.data.map { preferences ->
        preferences[AUTO_DELETE_DAYS] ?: 0
    }

    val markdownEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[MARKDOWN_ENABLED] ?: true
    }

    val codeHighlight: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[CODE_HIGHLIGHT] ?: true
    }

    val nightModeStart: Flow<String> = dataStore.data.map { preferences ->
        preferences[NIGHT_MODE_START] ?: "22:00"
    }

    val nightModeEnd: Flow<String> = dataStore.data.map { preferences ->
        preferences[NIGHT_MODE_END] ?: "07:00"
    }

    val nightModeAuto: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[NIGHT_MODE_AUTO] ?: false
    }

    val widgetEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[WIDGET_ENABLED] ?: false
    }

    val encryptEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[ENCRYPT_ENABLED] ?: false
    }

    val voiceReply: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[VOICE_REPLY] ?: true
    }

    val autoSpace: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[AUTO_SPACE] ?: true
    }

    val doubleSpace: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DOUBLE_SPACE] ?: false
    }

    val swipeReply: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SWIPE_REPLY] ?: true
    }

    val animationEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[ANIMATION_ENABLED] ?: true
    }

    val swipeToDelete: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SWIPE_TO_DELETE] ?: true
    }

    val longPressMenu: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[LONG_PRESS_MENU] ?: true
    }

    val autoSaveDraft: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[AUTO_SAVE_DRAFT] ?: true
    }

    val showWordCount: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SHOW_WORD_COUNT] ?: false
    }

    val autoExpandUrls: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[AUTO_EXPAND_URLS] ?: true
    }

    val messageBubbleRoundness: Flow<Float> = dataStore.data.map { preferences ->
        preferences[MESSAGE_BUBBLE_ROUNDNESS] ?: 16f
    }

    val defaultChatBubbleColor: Flow<String> = dataStore.data.map { preferences ->
        preferences[DEFAULT_CHAT_BUBBLE_COLOR] ?: "blue"
    }

    val aiMessageAlignment: Flow<String> = dataStore.data.map { preferences ->
        preferences[AI_MESSAGE_ALIGNMENT] ?: "left"
    }

    val maxMessageLength: Flow<Int> = dataStore.data.map { preferences ->
        preferences[MAX_MESSAGE_LENGTH] ?: 4096
    }

    val confirmBeforeSend: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[CONFIRM_BEFORE_SEND] ?: false
    }

    val showDeliveryStatus: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SHOW_DELIVERY_STATUS] ?: false
    }

    val quickSendButton: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[QUICK_SEND_BUTTON] ?: true
    }

    val hideKeyboardOnSend: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[HIDE_KEYBOARD_ON_SEND] ?: true
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

    suspend fun setAppLanguage(lang: String) {
        dataStore.edit { preferences ->
            preferences[APP_LANGUAGE] = lang
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun setMessageNotifications(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[MESSAGE_NOTIFICATIONS] = enabled
        }
    }

    suspend fun setShowTypingIndicator(show: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_TYPING_INDICATOR] = show
        }
    }

    suspend fun setAutoScroll(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[AUTO_SCROLL] = enabled
        }
    }

    suspend fun incrementChatCount() {
        dataStore.edit { preferences ->
            val current = preferences[CHAT_COUNT] ?: 0
            preferences[CHAT_COUNT] = current + 1
        }
    }

    suspend fun incrementMessageCount() {
        dataStore.edit { preferences ->
            val current = preferences[MESSAGE_COUNT] ?: 0
            preferences[MESSAGE_COUNT] = current + 1
        }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[SOUND_ENABLED] = enabled
        }
    }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[VIBRATION_ENABLED] = enabled
        }
    }

    suspend fun setShowTimestamps(show: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_TIMESTAMPS] = show
        }
    }

    suspend fun setAiTemperature(temp: Float) {
        dataStore.edit { preferences ->
            preferences[AI_TEMPERATURE] = temp
        }
    }

    suspend fun setMaxTokens(tokens: Int) {
        dataStore.edit { preferences ->
            preferences[MAX_TOKENS] = tokens
        }
    }

    suspend fun setSystemPrompt(prompt: String) {
        dataStore.edit { preferences ->
            preferences[SYSTEM_PROMPT] = prompt
        }
    }

    suspend fun setStreamingResponse(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[STREAMING_RESPONSE] = enabled
        }
    }

    suspend fun setFirstLaunch(first: Boolean) {
        dataStore.edit { preferences ->
            preferences[FIRST_LAUNCH] = first
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun setLastSyncTime(time: Long) {
        dataStore.edit { preferences ->
            preferences[LAST_SYNC_TIME] = time
        }
    }

    suspend fun setAutoBackupEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[AUTO_BACKUP_ENABLED] = enabled
        }
    }

    suspend fun setBackupPath(path: String) {
        dataStore.edit { preferences ->
            preferences[BACKUP_PATH] = path
        }
    }

    suspend fun setBubbleStyle(style: String) {
        dataStore.edit { preferences ->
            preferences[BUBBLE_STYLE] = style
        }
    }

    suspend fun setFontFamily(font: String) {
        dataStore.edit { preferences ->
            preferences[FONT_FAMILY] = font
        }
    }

    suspend fun setAnimationSpeed(speed: Float) {
        dataStore.edit { preferences ->
            preferences[ANIMATION_SPEED] = speed
        }
    }

    suspend fun setHapticFeedback(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[HAPTIC_FEEDBACK] = enabled
        }
    }

    suspend fun setSaveChats(save: Boolean) {
        dataStore.edit { preferences ->
            preferences[SAVE_CHATS] = save
        }
    }

    suspend fun setLastModel(model: String) {
        dataStore.edit { preferences ->
            preferences[LAST_MODEL] = model
        }
    }

    suspend fun setConversationTitle(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[CONVERSATION_TITLE] = enabled
        }
    }

    suspend fun setAppLockPin(pin: String) {
        dataStore.edit { preferences ->
            preferences[APP_LOCK_PIN] = pin
        }
    }

    suspend fun setAppLockEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[APP_LOCK_ENABLED] = enabled
        }
    }

    suspend fun setChatBackground(bg: String) {
        dataStore.edit { preferences ->
            preferences[CHAT_BACKGROUND] = bg
        }
    }

    suspend fun setQuickReplies(replies: String) {
        dataStore.edit { preferences ->
            preferences[QUICK_REPLIES] = replies
        }
    }

    suspend fun setAutoDeleteDays(days: Int) {
        dataStore.edit { preferences ->
            preferences[AUTO_DELETE_DAYS] = days
        }
    }

    suspend fun setMarkdownEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[MARKDOWN_ENABLED] = enabled
        }
    }

    suspend fun setCodeHighlight(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[CODE_HIGHLIGHT] = enabled
        }
    }

    suspend fun setNightModeStart(time: String) {
        dataStore.edit { preferences ->
            preferences[NIGHT_MODE_START] = time
        }
    }

    suspend fun setNightModeEnd(time: String) {
        dataStore.edit { preferences ->
            preferences[NIGHT_MODE_END] = time
        }
    }

    suspend fun setNightModeAuto(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NIGHT_MODE_AUTO] = enabled
        }
    }

    suspend fun setWidgetEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[WIDGET_ENABLED] = enabled
        }
    }

    suspend fun setEncryptEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[ENCRYPT_ENABLED] = enabled
        }
    }

    suspend fun setVoiceReply(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[VOICE_REPLY] = enabled
        }
    }

    suspend fun setAutoSpace(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[AUTO_SPACE] = enabled
        }
    }

    suspend fun setDoubleSpace(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[DOUBLE_SPACE] = enabled
        }
    }

    suspend fun setSwipeReply(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[SWIPE_REPLY] = enabled
        }
    }

    suspend fun setAnimationEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[ANIMATION_ENABLED] = enabled
        }
    }

    suspend fun setSwipeToDelete(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[SWIPE_TO_DELETE] = enabled
        }
    }

    suspend fun setLongPressMenu(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[LONG_PRESS_MENU] = enabled
        }
    }

    suspend fun setAutoSaveDraft(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[AUTO_SAVE_DRAFT] = enabled
        }
    }

    suspend fun setShowWordCount(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_WORD_COUNT] = enabled
        }
    }

    suspend fun setAutoExpandUrls(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[AUTO_EXPAND_URLS] = enabled
        }
    }

    suspend fun setMessageBubbleRoundness(roundness: Float) {
        dataStore.edit { preferences ->
            preferences[MESSAGE_BUBBLE_ROUNDNESS] = roundness
        }
    }

    suspend fun setDefaultChatBubbleColor(color: String) {
        dataStore.edit { preferences ->
            preferences[DEFAULT_CHAT_BUBBLE_COLOR] = color
        }
    }

    suspend fun setAiMessageAlignment(alignment: String) {
        dataStore.edit { preferences ->
            preferences[AI_MESSAGE_ALIGNMENT] = alignment
        }
    }

    suspend fun setMaxMessageLength(length: Int) {
        dataStore.edit { preferences ->
            preferences[MAX_MESSAGE_LENGTH] = length
        }
    }

    suspend fun setConfirmBeforeSend(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[CONFIRM_BEFORE_SEND] = enabled
        }
    }

    suspend fun setShowDeliveryStatus(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_DELIVERY_STATUS] = enabled
        }
    }

    suspend fun setQuickSendButton(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[QUICK_SEND_BUTTON] = enabled
        }
    }

    suspend fun setHideKeyboardOnSend(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[HIDE_KEYBOARD_ON_SEND] = enabled
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

    suspend fun getStatistics(): String {
        val prefs = dataStore.data.first()
        val chats = prefs[CHAT_COUNT] ?: 0
        val messages = prefs[MESSAGE_COUNT] ?: 0
        val tokens = prefs[TOKEN_COUNT] ?: 0
        
        return buildString {
            appendLine("📊 Статистика использования")
            appendLine("────────────────────")
            appendLine("💬 Всего чатов: $chats")
            appendLine("📝 Всего сообщений: $messages")
            appendLine("🔢 Всего токенов: $tokens")
            appendLine()
            appendLine("💰 Примерная стоимость:")
            val cost = tokens * 0.00001
            appendLine("  ~$${String.format("%.4f", cost)} (DeepSeek)")
            appendLine("  ~$${String.format("%.2f", cost * 15)} (GPT-4)")
        }
    }
}
