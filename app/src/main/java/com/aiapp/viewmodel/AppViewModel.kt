package com.aiapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.data.ai.AIMode
import com.aiapp.data.ai.AIService
import com.aiapp.data.ai.ModelInfo
import com.aiapp.data.local.InMemoryStorage
import com.aiapp.data.local.PreferencesManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val storage = InMemoryStorage()
    private val preferencesManager = PreferencesManager(application)
    private val aiService = AIService()
    
    val darkTheme = preferencesManager.darkTheme.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val username = preferencesManager.username.stateIn(viewModelScope, SharingStarted.Eagerly, "Пользователь")
    val autoSendVoice = preferencesManager.autoSendVoice.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val textSize = preferencesManager.textSize.stateIn(viewModelScope, SharingStarted.Eagerly, 16f)
    val ttsEnabled = preferencesManager.ttsEnabled.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val themeColor = preferencesManager.themeColor.stateIn(viewModelScope, SharingStarted.Eagerly, "system")
    val telegramWebhook = preferencesManager.telegramWebhook.stateIn(viewModelScope, SharingStarted.Eagerly, "")
    val tokenCount = preferencesManager.tokenCount.stateIn(viewModelScope, SharingStarted.Eagerly, 0)
    val appLanguage = preferencesManager.appLanguage.stateIn(viewModelScope, SharingStarted.Eagerly, "ru")
    val notificationsEnabled = preferencesManager.notificationsEnabled.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val messageNotifications = preferencesManager.messageNotifications.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val showTypingIndicator = preferencesManager.showTypingIndicator.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val autoScroll = preferencesManager.autoScroll.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val soundEnabled = preferencesManager.soundEnabled.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val vibrationEnabled = preferencesManager.vibrationEnabled.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val showTimestamps = preferencesManager.showTimestamps.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val aiTemperature = preferencesManager.aiTemperature.stateIn(viewModelScope, SharingStarted.Eagerly, 0.7f)
    val maxTokens = preferencesManager.maxTokens.stateIn(viewModelScope, SharingStarted.Eagerly, 1024)
    val systemPrompt = preferencesManager.systemPrompt.stateIn(viewModelScope, SharingStarted.Eagerly, "Ты - полезный AI ассистент. Отвечай на русском языке.")
    val streamingResponse = preferencesManager.streamingResponse.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val appLockPin = preferencesManager.appLockPin.stateIn(viewModelScope, SharingStarted.Eagerly, "")
    val appLockEnabled = preferencesManager.appLockEnabled.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val chatBackground = preferencesManager.chatBackground.stateIn(viewModelScope, SharingStarted.Eagerly, "default")
    val quickReplies = preferencesManager.quickReplies.stateIn(viewModelScope, SharingStarted.Eagerly, "")
    val autoDeleteDays = preferencesManager.autoDeleteDays.stateIn(viewModelScope, SharingStarted.Eagerly, 0)
    val markdownEnabled = preferencesManager.markdownEnabled.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val codeHighlight = preferencesManager.codeHighlight.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val nightModeStart = preferencesManager.nightModeStart.stateIn(viewModelScope, SharingStarted.Eagerly, "22:00")
    val nightModeEnd = preferencesManager.nightModeEnd.stateIn(viewModelScope, SharingStarted.Eagerly, "07:00")
    val nightModeAuto = preferencesManager.nightModeAuto.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val widgetEnabled = preferencesManager.widgetEnabled.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val encryptEnabled = preferencesManager.encryptEnabled.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val voiceReply = preferencesManager.voiceReply.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val autoSpace = preferencesManager.autoSpace.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val doubleSpace = preferencesManager.doubleSpace.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val swipeReply = preferencesManager.swipeReply.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val animationEnabled = preferencesManager.animationEnabled.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val swipeToDelete = preferencesManager.swipeToDelete.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val longPressMenu = preferencesManager.longPressMenu.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val autoSaveDraft = preferencesManager.autoSaveDraft.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val showWordCount = preferencesManager.showWordCount.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val autoExpandUrls = preferencesManager.autoExpandUrls.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val messageBubbleRoundness = preferencesManager.messageBubbleRoundness.stateIn(viewModelScope, SharingStarted.Eagerly, 16f)
    val defaultChatBubbleColor = preferencesManager.defaultChatBubbleColor.stateIn(viewModelScope, SharingStarted.Eagerly, "blue")
    val aiMessageAlignment = preferencesManager.aiMessageAlignment.stateIn(viewModelScope, SharingStarted.Eagerly, "left")
    val maxMessageLength = preferencesManager.maxMessageLength.stateIn(viewModelScope, SharingStarted.Eagerly, 4096)
    val confirmBeforeSend = preferencesManager.confirmBeforeSend.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val showDeliveryStatus = preferencesManager.showDeliveryStatus.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val quickSendButton = preferencesManager.quickSendButton.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val hideKeyboardOnSend = preferencesManager.hideKeyboardOnSend.stateIn(viewModelScope, SharingStarted.Eagerly, true)

    private val _selectedModel = MutableStateFlow("llama3")
    val selectedModel: StateFlow<String> = _selectedModel.asStateFlow()

    private val _aiMode = MutableStateFlow(AIMode.OLLAMA)
    val aiMode: StateFlow<AIMode> = _aiMode.asStateFlow()

    private val _serverUrl = MutableStateFlow("http://192.168.1.100:11434")
    val serverUrl: StateFlow<String> = _serverUrl.asStateFlow()

    private val _apiKey = MutableStateFlow("")
    val apiKey: StateFlow<String> = _apiKey.asStateFlow()

    private val _currentChatId = MutableStateFlow<String?>(null)
    val currentChatId: StateFlow<String?> = _currentChatId.asStateFlow()

    val allChats = storage.chats.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentMessages = _currentChatId.flatMapLatest { chatId ->
        if (chatId != null) storage.getChatMessages(chatId) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _availableModels = MutableStateFlow<List<ModelInfo>>(emptyList())
    val availableModels: StateFlow<List<ModelInfo>> = _availableModels.asStateFlow()

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    fun setAiMode(mode: AIMode) {
        _aiMode.value = mode
        aiService.setMode(mode)
        checkConnection()
    }

    fun setServerUrl(url: String) {
        _serverUrl.value = url
        aiService.setServerUrl(url)
        if (_aiMode.value == AIMode.OLLAMA) checkConnection()
    }

    fun setApiKey(key: String) {
        _apiKey.value = key
        aiService.setCloudApiKey(key)
        if (_aiMode.value == AIMode.CLOUD || _aiMode.value == AIMode.DEEPSEEK) checkConnection()
    }

    fun setSelectedModel(model: String) {
        _selectedModel.value = model
        aiService.setCloudModel(model)
    }

    fun checkConnection() {
        viewModelScope.launch {
            _isConnected.value = aiService.checkConnection()
            _availableModels.value = aiService.getModels()
        }
    }

    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setDarkTheme(enabled) }
    }

    fun setUsername(name: String) {
        viewModelScope.launch { preferencesManager.setUsername(name) }
    }

    fun setAutoSendVoice(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setAutoSendVoice(enabled) }
    }

    fun createNewChat() {
        viewModelScope.launch {
            val chatId = storage.createChat("Новый чат")
            _currentChatId.value = chatId
        }
    }

    fun selectChat(chatId: String) {
        _currentChatId.value = chatId
    }

    fun deleteChat(chatId: String) {
        viewModelScope.launch {
            storage.deleteChat(chatId)
            if (_currentChatId.value == chatId) _currentChatId.value = null
        }
    }

    fun sendMessage(text: String) {
        val chatId = _currentChatId.value ?: return
        val model = _selectedModel.value
        viewModelScope.launch {
            storage.addMessage(chatId, text, true)
            _isLoading.value = true
            
            try {
                var fullResponse = ""
                var totalTokens = 0
                aiService.generate(text).collect { response ->
                    fullResponse = response.text
                    totalTokens = response.promptTokens + response.completionTokens
                }
                
                if (fullResponse.isBlank()) {
                    fullResponse = when (_aiMode.value) {
                        AIMode.OLLAMA -> "Нет ответа. Проверьте подключение к Ollama."
                        AIMode.CLOUD -> "Нет ответа. Проверьте OpenAI API ключ."
                        AIMode.DEEPSEEK -> "Нет ответа. Проверьте DeepSeek API ключ."
                    }
                }
                
                storage.addMessage(chatId, fullResponse, false)
                
                if (totalTokens > 0) {
                    preferencesManager.addTokenCount(totalTokens)
                }
                
                if (telegramWebhook.value.isNotBlank()) {
                    sendTelegramNotification(fullResponse.take(100))
                }
            } catch (e: Exception) {
                storage.addMessage(chatId, "Ошибка: ${e.message}", false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteMessage(messageId: Long) {
        viewModelScope.launch { storage.deleteMessage(messageId) }
    }

    fun clearChat(chatId: String) {
        viewModelScope.launch { storage.clearChat(chatId) }
    }

    fun clearAllChats() {
        viewModelScope.launch {
            storage.clearAllChats()
            _currentChatId.value = null
        }
    }

    fun searchMessages(query: String): List<com.aiapp.data.local.ChatMessage> {
        return storage.searchMessages(query)
    }

    fun exportChat(chatId: String): String {
        return storage.exportChat(chatId)
    }

    fun exportAllChats(): String {
        val chats = allChats.value
        return buildString {
            appendLine("=== Экспорт всех чатов ===")
            appendLine()
            for (chat in chats) {
                appendLine("--- ${chat.title} ---")
                val messages = storage.getChatMessagesSync(chat.id)
                for (msg in messages) {
                    val sender = if (msg.isUser) username.value else "AI"
                    appendLine("$sender: ${msg.text}")
                }
                appendLine()
            }
        }
    }

    fun copyAllMessages(): String {
        return exportAllChats()
    }

    fun setTextSize(size: Float) {
        viewModelScope.launch { preferencesManager.setTextSize(size) }
    }

    fun setTtsEnabled(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setTtsEnabled(enabled) }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            checkConnection()
            _isRefreshing.value = false
        }
    }

    fun setThemeColor(color: String) {
        viewModelScope.launch { preferencesManager.setThemeColor(color) }
    }

    fun setTelegramWebhook(webhook: String) {
        viewModelScope.launch { preferencesManager.setTelegramWebhook(webhook) }
    }

    fun sendTelegramNotification(text: String) {
        viewModelScope.launch {
            val webhook = telegramWebhook.value
            if (webhook.isNotBlank()) {
                try {
                    val client = okhttp3.OkHttpClient()
                    val encodedText = java.net.URLEncoder.encode(text, "UTF-8")
                    val url = "https://api.telegram.org/bot$webhook/sendMessage?text=$encodedText"
                    val request = okhttp3.Request.Builder()
                        .url(url)
                        .get()
                        .build()
                    client.newCall(request).execute()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun clearCache() {
        viewModelScope.launch { preferencesManager.clearCache() }
    }

    fun getDeviceInfoString(): String = preferencesManager.getDeviceInfo()

    suspend fun exportSettings(): String = preferencesManager.exportSettings()

    suspend fun importSettings(json: String) {
        preferencesManager.importSettings(json)
    }

    fun resetTokenCount() {
        viewModelScope.launch { preferencesManager.resetTokenCount() }
    }

    fun setAppLanguage(lang: String) {
        viewModelScope.launch { preferencesManager.setAppLanguage(lang) }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setNotificationsEnabled(enabled) }
    }

    fun setMessageNotifications(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setMessageNotifications(enabled) }
    }

    fun setShowTypingIndicator(show: Boolean) {
        viewModelScope.launch { preferencesManager.setShowTypingIndicator(show) }
    }

    fun setAutoScroll(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setAutoScroll(enabled) }
    }

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setSoundEnabled(enabled) }
    }

    fun setVibrationEnabled(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setVibrationEnabled(enabled) }
    }

    fun setShowTimestamps(show: Boolean) {
        viewModelScope.launch { preferencesManager.setShowTimestamps(show) }
    }

    fun setAiTemperature(temp: Float) {
        viewModelScope.launch { preferencesManager.setAiTemperature(temp) }
    }

    fun setMaxTokens(tokens: Int) {
        viewModelScope.launch { preferencesManager.setMaxTokens(tokens) }
    }

    fun setSystemPrompt(prompt: String) {
        viewModelScope.launch { preferencesManager.setSystemPrompt(prompt) }
    }

    fun setStreamingResponse(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setStreamingResponse(enabled) }
    }

    fun getStatistics(): String {
        var stats = ""
        viewModelScope.launch {
            stats = preferencesManager.getStatistics()
        }
        return stats
    }

    fun incrementChatCount() {
        viewModelScope.launch { preferencesManager.incrementChatCount() }
    }

    fun incrementMessageCount() {
        viewModelScope.launch { preferencesManager.incrementMessageCount() }
    }

    fun setAppLockPin(pin: String) {
        viewModelScope.launch { preferencesManager.setAppLockPin(pin) }
    }

    fun setAppLockEnabled(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setAppLockEnabled(enabled) }
    }

    fun setChatBackground(bg: String) {
        viewModelScope.launch { preferencesManager.setChatBackground(bg) }
    }

    fun setQuickReplies(replies: String) {
        viewModelScope.launch { preferencesManager.setQuickReplies(replies) }
    }

    fun setAutoDeleteDays(days: Int) {
        viewModelScope.launch { preferencesManager.setAutoDeleteDays(days) }
    }

    fun setMarkdownEnabled(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setMarkdownEnabled(enabled) }
    }

    fun setCodeHighlight(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setCodeHighlight(enabled) }
    }

    fun setNightModeStart(time: String) {
        viewModelScope.launch { preferencesManager.setNightModeStart(time) }
    }

    fun setNightModeEnd(time: String) {
        viewModelScope.launch { preferencesManager.setNightModeEnd(time) }
    }

    fun setNightModeAuto(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setNightModeAuto(enabled) }
    }

    fun setWidgetEnabled(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setWidgetEnabled(enabled) }
    }

    fun setEncryptEnabled(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setEncryptEnabled(enabled) }
    }

    fun setVoiceReply(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setVoiceReply(enabled) }
    }

    fun setAutoSpace(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setAutoSpace(enabled) }
    }

    fun setDoubleSpace(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setDoubleSpace(enabled) }
    }

    fun setSwipeReply(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setSwipeReply(enabled) }
    }

    fun setAnimationEnabled(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setAnimationEnabled(enabled) }
    }

    fun setSwipeToDelete(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setSwipeToDelete(enabled) }
    }

    fun setLongPressMenu(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setLongPressMenu(enabled) }
    }

    fun setAutoSaveDraft(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setAutoSaveDraft(enabled) }
    }

    fun setShowWordCount(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setShowWordCount(enabled) }
    }

    fun setAutoExpandUrls(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setAutoExpandUrls(enabled) }
    }

    fun setMessageBubbleRoundness(roundness: Float) {
        viewModelScope.launch { preferencesManager.setMessageBubbleRoundness(roundness) }
    }

    fun setDefaultChatBubbleColor(color: String) {
        viewModelScope.launch { preferencesManager.setDefaultChatBubbleColor(color) }
    }

    fun setAiMessageAlignment(alignment: String) {
        viewModelScope.launch { preferencesManager.setAiMessageAlignment(alignment) }
    }

    fun setMaxMessageLength(length: Int) {
        viewModelScope.launch { preferencesManager.setMaxMessageLength(length) }
    }

    fun setConfirmBeforeSend(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setConfirmBeforeSend(enabled) }
    }

    fun setShowDeliveryStatus(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setShowDeliveryStatus(enabled) }
    }

    fun setQuickSendButton(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setQuickSendButton(enabled) }
    }

    fun setHideKeyboardOnSend(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setHideKeyboardOnSend(enabled) }
    }
}
