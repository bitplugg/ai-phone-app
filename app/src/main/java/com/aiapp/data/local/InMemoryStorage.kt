package com.aiapp.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

data class Chat(
    val id: String,
    val title: String,
    val model: String = "assistant",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val category: String = "default",
    val isPinned: Boolean = false,
    val isMuted: Boolean = false
)

data class ChatMessage(
    val id: Long = System.currentTimeMillis(),
    val chatId: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val reactions: Map<String, Int> = emptyMap(),
    val isPinned: Boolean = false,
    val isEdited: Boolean = false
)

data class ModelInfo(
    val id: String,
    val name: String,
    val description: String,
    val size: Long,
    val downloadUrl: String,
    val fileName: String,
    val isDownloaded: Boolean = false,
    val downloadProgress: Int = 0,
    val isDownloading: Boolean = false,
    val requiredRam: Long = 0
)

class InMemoryStorage {
    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: Flow<List<Chat>> = _chats

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: Flow<List<ChatMessage>> = _messages

    private val _models = MutableStateFlow<List<ModelInfo>>(emptyList())
    val models: Flow<List<ModelInfo>> = _models

    private var chatIdCounter = 0L
    private var messageIdCounter = 0L

    init {
        _models.value = listOf(
            ModelInfo(
                id = "llama3-8b",
                name = "Llama 3 8B",
                description = "Meta's Llama 3 8B - отличная производительность",
                size = 4_900_000_000,
                downloadUrl = "https://huggingface.co/QuantFactory/Meta-Llama-3-8B-Instruct-GGUF/resolve/main/Meta-Llama-3-8B-Instruct.Q4_K_M.gguf",
                fileName = "llama3-8b-q4km.gguf",
                requiredRam = 6_000_000_000
            ),
            ModelInfo(
                id = "phi3-mini",
                name = "Phi-3 Mini",
                description = "Microsoft Phi-3 Mini - быстрая и эффективная",
                size = 2_300_000_000,
                downloadUrl = "https://huggingface.co/microsoft/Phi-3-mini-4k-instruct-gguf/resolve/main/Phi-3-mini-4k-instruct.Q4_K_M.gguf",
                fileName = "phi3-mini-q4km.gguf",
                requiredRam = 4_000_000_000
            ),
            ModelInfo(
                id = "gemma-2b",
                name = "Gemma 2B",
                description = "Google Gemma 2B - компактная модель",
                size = 1_600_000_000,
                downloadUrl = "https://huggingface.co/google/gemma-2b-it-GGUF/resolve/main/gemma-2b-it.Q4_K_M.gguf",
                fileName = "gemma-2b-q4km.gguf",
                requiredRam = 3_000_000_000
            ),
            ModelInfo(
                id = "tinyllama",
                name = "TinyLlama",
                description = "Самая легкая модель для слабых устройств",
                size = 800_000_000,
                downloadUrl = "https://huggingface.co/TheBloke/TinyLlama-1.1B-Chat-v1.0-GGUF/resolve/main/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf",
                fileName = "tinyllama-q4km.gguf",
                requiredRam = 1_500_000_000
            ),
            ModelInfo(
                id = "mistral-7b",
                name = "Mistral 7B",
                description = "Популярная открытая модель",
                size = 4_100_000_000,
                downloadUrl = "https://huggingface.co/TheBloke/Mistral-7B-Instruct-v0.2-GGUF/resolve/main/mistral-7b-instruct-v0.2.Q4_K_M.gguf",
                fileName = "mistral-7b-q4km.gguf",
                requiredRam = 6_000_000_000
            )
        )
    }

    fun createChat(title: String): String {
        val id = "chat_${++chatIdCounter}"
        _chats.value = _chats.value + Chat(id = id, title = title)
        
        val welcomeMsg = ChatMessage(
            id = ++messageIdCounter,
            chatId = id,
            text = "Привет! Я AI ассистент. Чем могу помочь?",
            isUser = false
        )
        _messages.value = _messages.value + welcomeMsg
        
        return id
    }

    fun getChatMessages(chatId: String): Flow<List<ChatMessage>> {
        return _messages.map { msgs -> msgs.filter { it.chatId == chatId }.sortedBy { it.timestamp } }
    }

    fun addMessage(chatId: String, text: String, isUser: Boolean) {
        val msg = ChatMessage(
            id = ++messageIdCounter,
            chatId = chatId,
            text = text,
            isUser = isUser
        )
        _messages.value = _messages.value + msg
        
        _chats.value = _chats.value.map { chat ->
            if (chat.id == chatId) chat.copy(updatedAt = System.currentTimeMillis())
            else chat
        }
    }

    fun deleteChat(chatId: String) {
        _chats.value = _chats.value.filter { it.id != chatId }
        _messages.value = _messages.value.filter { it.chatId != chatId }
    }

    fun deleteMessage(messageId: Long) {
        _messages.value = _messages.value.filter { it.id != messageId }
    }

    fun clearChat(chatId: String) {
        _messages.value = _messages.value.filter { it.chatId != chatId }
        val welcomeMsg = ChatMessage(
            id = ++messageIdCounter,
            chatId = chatId,
            text = "Привет! Я AI ассистент. Чем могу помочь?",
            isUser = false
        )
        _messages.value = _messages.value + welcomeMsg
    }

    fun searchMessages(query: String): List<ChatMessage> {
        if (query.isBlank()) return emptyList()
        return _messages.value.filter { 
            it.text.contains(query, ignoreCase = true) 
        }
    }

    fun exportChat(chatId: String, format: String = "txt"): String {
        val chatMessages = _messages.value
            .filter { it.chatId == chatId }
            .sortedBy { it.timestamp }
        
        return when (format) {
            "json" -> {
                val chat = _chats.value.find { it.id == chatId }
                val json = StringBuilder()
                json.appendLine("{")
                json.appendLine("  \"title\": \"${chat?.title ?: "Chat"}\",")
                json.appendLine("  \"exportedAt\": \"${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())}\",")
                json.appendLine("  \"messages\": [")
                chatMessages.forEachIndexed { index, msg ->
                    val sender = if (msg.isUser) "user" else "assistant"
                    json.appendLine("    {")
                    json.appendLine("      \"role\": \"$sender\",")
                    json.appendLine("      \"content\": \"${msg.text.replace("\"", "\\\"")}\",")
                    json.appendLine("      \"timestamp\": ${msg.timestamp}")
                    json.append("    }")
                    if (index < chatMessages.size - 1) json.appendLine(",") else json.appendLine()
                }
                json.appendLine("  ]")
                json.appendLine("}")
                json.toString()
            }
            else -> {
                buildString {
                    chatMessages.forEach { msg ->
                        val sender = if (msg.isUser) "Пользователь" else "AI"
                        appendLine("[$sender]")
                        appendLine(msg.text)
                        appendLine()
                    }
                }
            }
        }
    }

    fun updateModelProgress(modelId: String, progress: Int) {
        _models.value = _models.value.map { model ->
            if (model.id == modelId) model.copy(
                downloadProgress = progress,
                isDownloading = progress in 1..99,
                isDownloaded = progress >= 100
            )
            else model
        }
    }

    fun deleteModel(modelId: String) {
        _models.value = _models.value.map { model ->
            if (model.id == modelId) model.copy(
                isDownloaded = false,
                downloadProgress = 0,
                isDownloading = false
            )
            else model
        }
    }

    fun clearAllChats() {
        _chats.value = emptyList()
        _messages.value = emptyList()
    }

    fun getChatMessagesSync(chatId: String): List<ChatMessage> {
        return _messages.value.filter { it.chatId == chatId }.sortedBy { it.timestamp }
    }

    fun addReaction(messageId: Long, emoji: String) {
        _messages.value = _messages.value.map { msg ->
            if (msg.id == messageId) {
                val reactions = msg.reactions.toMutableMap()
                reactions[emoji] = (reactions[emoji] ?: 0) + 1
                msg.copy(reactions = reactions)
            } else msg
        }
    }

    fun removeReaction(messageId: Long, emoji: String) {
        _messages.value = _messages.value.map { msg ->
            if (msg.id == messageId) {
                val reactions = msg.reactions.toMutableMap()
                val count = (reactions[emoji] ?: 1) - 1
                if (count > 0) reactions[emoji] = count else reactions.remove(emoji)
                msg.copy(reactions = reactions)
            } else msg
        }
    }

    fun toggleMessagePin(messageId: Long) {
        _messages.value = _messages.value.map { msg ->
            if (msg.id == messageId) msg.copy(isPinned = !msg.isPinned)
            else msg
        }
    }

    fun setChatCategory(chatId: String, category: String) {
        _chats.value = _chats.value.map { chat ->
            if (chat.id == chatId) chat.copy(category = category)
            else chat
        }
    }

    fun toggleChatPin(chatId: String) {
        _chats.value = _chats.value.map { chat ->
            if (chat.id == chatId) chat.copy(isPinned = !chat.isPinned)
            else chat
        }
    }

    fun toggleChatMute(chatId: String) {
        _chats.value = _chats.value.map { chat ->
            if (chat.id == chatId) chat.copy(isMuted = !chat.isMuted)
            else chat
        }
    }

    fun getChatsByCategory(category: String): List<Chat> {
        return if (category == "all") _chats.value
        else _chats.value.filter { it.category == category }
    }

    fun searchMessagesInChat(chatId: String, query: String): List<ChatMessage> {
        return _messages.value.filter { 
            it.chatId == chatId && it.text.contains(query, ignoreCase = true) 
        }.sortedBy { it.timestamp }
    }

    fun getCategories(): List<String> {
        return _chats.value.map { it.category }.distinct().filter { it != "default" }
    }
}
