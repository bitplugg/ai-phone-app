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
            val themeColor by viewModel.themeColor.collectAsState()
            val telegramWebhook by viewModel.telegramWebhook.collectAsState()
            val tokenCount by viewModel.tokenCount.collectAsState()
            val appLanguage by viewModel.appLanguage.collectAsState()
            val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
            val messageNotifications by viewModel.messageNotifications.collectAsState()
            val showTypingIndicator by viewModel.showTypingIndicator.collectAsState()
            val autoScroll by viewModel.autoScroll.collectAsState()
            val soundEnabled by viewModel.soundEnabled.collectAsState()
            val vibrationEnabled by viewModel.vibrationEnabled.collectAsState()
            val showTimestamps by viewModel.showTimestamps.collectAsState()
            val aiTemperature by viewModel.aiTemperature.collectAsState()
            val maxTokens by viewModel.maxTokens.collectAsState()
            val streamingResponse by viewModel.streamingResponse.collectAsState()
            val appLockPin by viewModel.appLockPin.collectAsState()
            val appLockEnabled by viewModel.appLockEnabled.collectAsState()
            val chatBackground by viewModel.chatBackground.collectAsState()
            val quickReplies by viewModel.quickReplies.collectAsState()
            val autoDeleteDays by viewModel.autoDeleteDays.collectAsState()
            val markdownEnabled by viewModel.markdownEnabled.collectAsState()
            val codeHighlight by viewModel.codeHighlight.collectAsState()
            val nightModeStart by viewModel.nightModeStart.collectAsState()
            val nightModeEnd by viewModel.nightModeEnd.collectAsState()
            val nightModeAuto by viewModel.nightModeAuto.collectAsState()
            val widgetEnabled by viewModel.widgetEnabled.collectAsState()
            val encryptEnabled by viewModel.encryptEnabled.collectAsState()
            val voiceReply by viewModel.voiceReply.collectAsState()
            val autoSpace by viewModel.autoSpace.collectAsState()
            val doubleSpace by viewModel.doubleSpace.collectAsState()
            val swipeReply by viewModel.swipeReply.collectAsState()
            val animationEnabled by viewModel.animationEnabled.collectAsState()
            val swipeToDelete by viewModel.swipeToDelete.collectAsState()
            val longPressMenu by viewModel.longPressMenu.collectAsState()
            val autoSaveDraft by viewModel.autoSaveDraft.collectAsState()
            val showWordCount by viewModel.showWordCount.collectAsState()
            val autoExpandUrls by viewModel.autoExpandUrls.collectAsState()
            val messageBubbleRoundness by viewModel.messageBubbleRoundness.collectAsState()
            val defaultChatBubbleColor by viewModel.defaultChatBubbleColor.collectAsState()
            val aiMessageAlignment by viewModel.aiMessageAlignment.collectAsState()
            val maxMessageLength by viewModel.maxMessageLength.collectAsState()
            val confirmBeforeSend by viewModel.confirmBeforeSend.collectAsState()
            val showDeliveryStatus by viewModel.showDeliveryStatus.collectAsState()
            val quickSendButton by viewModel.quickSendButton.collectAsState()
            val hideKeyboardOnSend by viewModel.hideKeyboardOnSend.collectAsState()

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

            AIAppTheme(darkTheme = darkTheme, themeColor = themeColor) {
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
                    onRefresh = { viewModel.refresh() },
                    onThemeColorChange = { viewModel.setThemeColor(it) },
                    onTelegramWebhookChange = { viewModel.setTelegramWebhook(it) },
                    onBackupSettings = {
                        val content = kotlinx.coroutines.runBlocking { viewModel.exportSettings() }
                        val file = File(getExternalFilesDir(null), "ai_settings_backup_${System.currentTimeMillis()}.json")
                        file.writeText(content)
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "application/json"
                            putExtra(Intent.EXTRA_STREAM, android.net.Uri.fromFile(file))
                        }
                        startActivity(Intent.createChooser(shareIntent, "Backup настроек"))
                    },
                    onClearCache = { viewModel.clearCache() },
                    onResetTokenCount = { viewModel.resetTokenCount() },
                    onLanguageChange = { viewModel.setAppLanguage(it) },
                    onNotificationsChange = { viewModel.setNotificationsEnabled(it) },
                    onMessageNotificationsChange = { viewModel.setMessageNotifications(it) },
                    onTypingIndicatorChange = { viewModel.setShowTypingIndicator(it) },
                    onAutoScrollChange = { viewModel.setAutoScroll(it) },
                    onSoundChange = { viewModel.setSoundEnabled(it) },
                    onVibrationChange = { viewModel.setVibrationEnabled(it) },
                    onTimestampChange = { viewModel.setShowTimestamps(it) },
                    onTemperatureChange = { viewModel.setAiTemperature(it) },
                    onMaxTokensChange = { viewModel.setMaxTokens(it) },
                    onStreamingChange = { viewModel.setStreamingResponse(it) },
                    themeColor = themeColor,
                    telegramWebhook = telegramWebhook,
                    tokenCount = tokenCount,
                    deviceInfo = viewModel.getDeviceInfoString(),
                    statistics = viewModel.getStatistics(),
                    appLanguage = appLanguage,
                    notificationsEnabled = notificationsEnabled,
                    messageNotifications = messageNotifications,
                    showTypingIndicator = showTypingIndicator,
                    autoScroll = autoScroll,
                    soundEnabled = soundEnabled,
                    vibrationEnabled = vibrationEnabled,
                    showTimestamps = showTimestamps,
                    aiTemperature = aiTemperature,
                    maxTokens = maxTokens,
                    streamingResponse = streamingResponse,
                    appLockPin = appLockPin,
                    appLockEnabled = appLockEnabled,
                    chatBackground = chatBackground,
                    quickReplies = quickReplies,
                    autoDeleteDays = autoDeleteDays,
                    markdownEnabled = markdownEnabled,
                    codeHighlight = codeHighlight,
                    nightModeStart = nightModeStart,
                    nightModeEnd = nightModeEnd,
                    nightModeAuto = nightModeAuto,
                    widgetEnabled = widgetEnabled,
                    encryptEnabled = encryptEnabled,
                    voiceReply = voiceReply,
                    autoSpace = autoSpace,
                    doubleSpace = doubleSpace,
                    swipeReply = swipeReply,
                    onAppLockPinChange = { viewModel.setAppLockPin(it) },
                    onAppLockEnabledChange = { viewModel.setAppLockEnabled(it) },
                    onChatBackgroundChange = { viewModel.setChatBackground(it) },
                    onQuickRepliesChange = { viewModel.setQuickReplies(it) },
                    onAutoDeleteDaysChange = { viewModel.setAutoDeleteDays(it) },
                    onMarkdownEnabledChange = { viewModel.setMarkdownEnabled(it) },
                    onCodeHighlightChange = { viewModel.setCodeHighlight(it) },
                    onNightModeStartChange = { viewModel.setNightModeStart(it) },
                    onNightModeEndChange = { viewModel.setNightModeEnd(it) },
                    onNightModeAutoChange = { viewModel.setNightModeAuto(it) },
                    onWidgetEnabledChange = { viewModel.setWidgetEnabled(it) },
                    onEncryptEnabledChange = { viewModel.setEncryptEnabled(it) },
                    onVoiceReplyChange = { viewModel.setVoiceReply(it) },
                    onAutoSpaceChange = { viewModel.setAutoSpace(it) },
                    onDoubleSpaceChange = { viewModel.setDoubleSpace(it) },
                    onSwipeReplyChange = { viewModel.setSwipeReply(it) },
                    animationEnabled = animationEnabled,
                    swipeToDelete = swipeToDelete,
                    longPressMenu = longPressMenu,
                    autoSaveDraft = autoSaveDraft,
                    showWordCount = showWordCount,
                    autoExpandUrls = autoExpandUrls,
                    messageBubbleRoundness = messageBubbleRoundness,
                    defaultChatBubbleColor = defaultChatBubbleColor,
                    aiMessageAlignment = aiMessageAlignment,
                    maxMessageLength = maxMessageLength,
                    confirmBeforeSend = confirmBeforeSend,
                    showDeliveryStatus = showDeliveryStatus,
                    quickSendButton = quickSendButton,
                    hideKeyboardOnSend = hideKeyboardOnSend,
                    onAnimationEnabledChange = { viewModel.setAnimationEnabled(it) },
                    onSwipeToDeleteChange = { viewModel.setSwipeToDelete(it) },
                    onLongPressMenuChange = { viewModel.setLongPressMenu(it) },
                    onAutoSaveDraftChange = { viewModel.setAutoSaveDraft(it) },
                    onShowWordCountChange = { viewModel.setShowWordCount(it) },
                    onAutoExpandUrlsChange = { viewModel.setAutoExpandUrls(it) },
                    onMessageBubbleRoundnessChange = { viewModel.setMessageBubbleRoundness(it) },
                    onDefaultChatBubbleColorChange = { viewModel.setDefaultChatBubbleColor(it) },
                    onAiMessageAlignmentChange = { viewModel.setAiMessageAlignment(it) },
                    onMaxMessageLengthChange = { viewModel.setMaxMessageLength(it) },
                    onConfirmBeforeSendChange = { viewModel.setConfirmBeforeSend(it) },
                    onShowDeliveryStatusChange = { viewModel.setShowDeliveryStatus(it) },
                    onQuickSendButtonChange = { viewModel.setQuickSendButton(it) },
                    onHideKeyboardOnSendChange = { viewModel.setHideKeyboardOnSend(it) },
                    onAddReaction = { messageId, emoji -> viewModel.addReaction(messageId, emoji) }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager?.shutdown()
    }
}
