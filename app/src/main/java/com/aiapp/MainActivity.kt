package com.aiapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiapp.data.TTSManager
import com.aiapp.ui.screens.MainScreen
import com.aiapp.ui.screens.Screen
import com.aiapp.ui.theme.AIAppTheme
import com.aiapp.viewmodel.AppViewModel
import java.io.File

class MainActivity : ComponentActivity() {
    private var ttsManager: TTSManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        ttsManager = TTSManager(this)
        
        setContent {
            val viewModel: AppViewModel = viewModel()
            val darkTheme by viewModel.darkTheme.collectAsState()
            val username by viewModel.username.collectAsState()
            val selectedModel by viewModel.selectedModel.collectAsState()
            val autoSendVoice by viewModel.autoSendVoice.collectAsState()
            val aiMode by viewModel.aiMode.collectAsState()
            val serverUrl by viewModel.serverUrl.collectAsState()
            val apiKey by viewModel.apiKey.collectAsState()
            val isConnected by viewModel.isConnected.collectAsState()
            val chats by viewModel.allChats.collectAsState()
            val currentChatId by viewModel.currentChatId.collectAsState()
            val currentMessages by viewModel.currentMessages.collectAsState()
            val allModels by viewModel.availableModels.collectAsState()
            val downloadedModels by viewModel.availableModels.collectAsState()
            val textSize by viewModel.textSize.collectAsState()
            val ttsEnabled by viewModel.ttsEnabled.collectAsState()

            var currentScreen by remember { mutableStateOf(Screen.CHAT_LIST) }

            LaunchedEffect(Unit) {
                viewModel.checkConnection()
                ttsManager?.init()
            }

            LaunchedEffect(currentChatId) {
                if (currentChatId != null) {
                    currentScreen = Screen.CHAT
                }
            }

            LaunchedEffect(ttsEnabled, currentMessages) {
                if (ttsEnabled && currentMessages.isNotEmpty()) {
                    val lastMsg = currentMessages.lastOrNull()
                    if (lastMsg != null && !lastMsg.isUser) {
                        ttsManager?.speak(lastMsg.text)
                    }
                }
            }

            AIAppTheme(darkTheme = darkTheme) {
                MainScreen(
                    currentScreen = currentScreen,
                    chats = chats,
                    currentMessages = currentMessages,
                    currentChatId = currentChatId,
                    darkTheme = darkTheme,
                    username = username,
                    allModels = allModels,
                    downloadedModels = downloadedModels,
                    selectedModel = selectedModel,
                    autoSendVoice = autoSendVoice,
                    aiMode = aiMode,
                    serverUrl = serverUrl,
                    apiKey = apiKey,
                    isConnected = isConnected,
                    textSize = textSize,
                    onNavigate = { screen -> currentScreen = screen },
                    onCreateChat = { viewModel.createNewChat() },
                    onSelectChat = { chatId -> 
                        viewModel.selectChat(chatId)
                        currentScreen = Screen.CHAT
                    },
                    onDeleteChat = { viewModel.deleteChat(it) },
                    onSendMessage = { viewModel.sendMessage(it) },
                    onDeleteMessage = { viewModel.deleteMessage(it) },
                    onDarkThemeChange = { viewModel.setDarkTheme(it) },
                    onUsernameChange = { viewModel.setUsername(it) },
                    onSelectedModelChange = { viewModel.setSelectedModel(it) },
                    onAutoSendVoiceChange = { viewModel.setAutoSendVoice(it) },
                    onAiModeChange = { viewModel.setAiMode(it) },
                    onServerUrlChange = { viewModel.setServerUrl(it) },
                    onApiKeyChange = { viewModel.setApiKey(it) },
                    onCheckConnection = { viewModel.checkConnection() },
                    onDownloadModel = { },
                    onDeleteModel = { },
                    onClearChat = { viewModel.clearChat(it) },
                    onSearchMessages = { viewModel.searchMessages(it) },
                    onExportChat = { viewModel.exportChat(it) },
                    onTextSizeChange = { viewModel.setTextSize(it) },
                    onTtsEnabledChange = { viewModel.setTtsEnabled(it) },
                    onExportAllChats = {
                        val content = viewModel.exportAllChats()
                        val file = File(getExternalFilesDir(null), "all_chats_${System.currentTimeMillis()}.txt")
                        file.writeText(content)
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_STREAM, android.net.Uri.fromFile(file))
                        }
                        startActivity(Intent.createChooser(shareIntent, "Экспорт чатов"))
                    },
                    onCopyAllMessages = {
                        val content = viewModel.copyAllMessages()
                        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("all_messages", content)
                        clipboard.setPrimaryClip(clip)
                    },
                    onClearAllChats = { viewModel.clearAllChats() },
                    onRefresh = { viewModel.refresh() }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager?.shutdown()
    }
}
