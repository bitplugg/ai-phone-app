package com.aiapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.data.ai.AIMode
import com.aiapp.data.ai.AIService
import com.aiapp.data.ai.ModelInfo
import com.aiapp.data.local.InMemoryStorage
import com.aiapp.data.local.PreferencesManager
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
                aiService.generate(text).collect { part ->
                    fullResponse += part
                }
                
                if (fullResponse.isBlank()) {
                    fullResponse = when (_aiMode.value) {
                        AIMode.OLLAMA -> "Нет ответа. Проверьте подключение к Ollama."
                        AIMode.CLOUD -> "Нет ответа. Проверьте OpenAI API ключ."
                        AIMode.DEEPSEEK -> "Нет ответа. Проверьте DeepSeek API ключ."
                    }
                }
                
                storage.addMessage(chatId, fullResponse, false)
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
}
