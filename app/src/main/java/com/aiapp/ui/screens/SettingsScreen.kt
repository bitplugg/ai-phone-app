package com.aiapp.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aiapp.data.ai.AIMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    darkTheme: Boolean,
    username: String,
    selectedModel: String,
    autoSendVoice: Boolean,
    aiMode: AIMode,
    serverUrl: String,
    apiKey: String,
    isConnected: Boolean,
    textSize: Float = 16f,
    ttsEnabled: Boolean = false,
    themeColor: String = "system",
    telegramWebhook: String = "",
    tokenCount: Int = 0,
    deviceInfo: String = "",
    statistics: String = "",
    appLanguage: String = "ru",
    notificationsEnabled: Boolean = true,
    messageNotifications: Boolean = false,
    showTypingIndicator: Boolean = true,
    autoScroll: Boolean = true,
    soundEnabled: Boolean = true,
    vibrationEnabled: Boolean = true,
    showTimestamps: Boolean = true,
    aiTemperature: Float = 0.7f,
    maxTokens: Int = 1024,
    streamingResponse: Boolean = true,
    onDarkThemeChange: (Boolean) -> Unit,
    onUsernameChange: (String) -> Unit,
    onSelectedModelChange: (String) -> Unit,
    onAutoSendVoiceChange: (Boolean) -> Unit,
    onAiModeChange: (AIMode) -> Unit,
    onServerUrlChange: (String) -> Unit,
    onApiKeyChange: (String) -> Unit,
    onCheckConnection: () -> Unit,
    onBackClick: () -> Unit,
    onTextSizeChange: (Float) -> Unit = {},
    onTtsEnabledChange: (Boolean) -> Unit = {},
    onThemeColorChange: (String) -> Unit = {},
    onBackupSettings: () -> Unit = {},
    onClearCache: () -> Unit = {},
    onResetTokenCount: () -> Unit = {},
    onTelegramWebhookChange: (String) -> Unit = {},
    onExportAllChats: () -> Unit = {},
    onCopyAllMessages: () -> Unit = {},
    onClearAllChats: () -> Unit = {},
    onLanguageChange: (String) -> Unit = {},
    onNotificationsChange: (Boolean) -> Unit = {},
    onMessageNotificationsChange: (Boolean) -> Unit = {},
    onTypingIndicatorChange: (Boolean) -> Unit = {},
    onAutoScrollChange: (Boolean) -> Unit = {},
    onSoundChange: (Boolean) -> Unit = {},
    onVibrationChange: (Boolean) -> Unit = {},
    onTimestampChange: (Boolean) -> Unit = {},
    onTemperatureChange: (Float) -> Unit = {},
    onMaxTokensChange: (Int) -> Unit = {},
    onStreamingChange: (Boolean) -> Unit = {},
    appLockPin: String = "",
    appLockEnabled: Boolean = false,
    chatBackground: String = "default",
    quickReplies: String = "",
    autoDeleteDays: Int = 0,
    markdownEnabled: Boolean = true,
    codeHighlight: Boolean = true,
    nightModeStart: String = "22:00",
    nightModeEnd: String = "07:00",
    nightModeAuto: Boolean = false,
    widgetEnabled: Boolean = false,
    encryptEnabled: Boolean = false,
    voiceReply: Boolean = true,
    autoSpace: Boolean = true,
    doubleSpace: Boolean = false,
    swipeReply: Boolean = true,
    onAppLockPinChange: (String) -> Unit = {},
    onAppLockEnabledChange: (Boolean) -> Unit = {},
    onChatBackgroundChange: (String) -> Unit = {},
    onQuickRepliesChange: (String) -> Unit = {},
    onAutoDeleteDaysChange: (Int) -> Unit = {},
    onMarkdownEnabledChange: (Boolean) -> Unit = {},
    onCodeHighlightChange: (Boolean) -> Unit = {},
    onNightModeStartChange: (String) -> Unit = {},
    onNightModeEndChange: (String) -> Unit = {},
    onNightModeAutoChange: (Boolean) -> Unit = {},
    onWidgetEnabledChange: (Boolean) -> Unit = {},
    onEncryptEnabledChange: (Boolean) -> Unit = {},
    onVoiceReplyChange: (Boolean) -> Unit = {},
    onAutoSpaceChange: (Boolean) -> Unit = {},
    onDoubleSpaceChange: (Boolean) -> Unit = {},
    onSwipeReplyChange: (Boolean) -> Unit = {},
    animationEnabled: Boolean = true,
    swipeToDelete: Boolean = true,
    longPressMenu: Boolean = true,
    autoSaveDraft: Boolean = true,
    showWordCount: Boolean = false,
    autoExpandUrls: Boolean = true,
    messageBubbleRoundness: Float = 16f,
    defaultChatBubbleColor: String = "blue",
    aiMessageAlignment: String = "left",
    maxMessageLength: Int = 4096,
    confirmBeforeSend: Boolean = false,
    showDeliveryStatus: Boolean = false,
    quickSendButton: Boolean = true,
    hideKeyboardOnSend: Boolean = true,
    onAnimationEnabledChange: (Boolean) -> Unit = {},
    onSwipeToDeleteChange: (Boolean) -> Unit = {},
    onLongPressMenuChange: (Boolean) -> Unit = {},
    onAutoSaveDraftChange: (Boolean) -> Unit = {},
    onShowWordCountChange: (Boolean) -> Unit = {},
    onAutoExpandUrlsChange: (Boolean) -> Unit = {},
    onMessageBubbleRoundnessChange: (Float) -> Unit = {},
    onDefaultChatBubbleColorChange: (String) -> Unit = {},
    onAiMessageAlignmentChange: (String) -> Unit = {},
    onMaxMessageLengthChange: (Int) -> Unit = {},
    onConfirmBeforeSendChange: (Boolean) -> Unit = {},
    onShowDeliveryStatusChange: (Boolean) -> Unit = {},
    onQuickSendButtonChange: (Boolean) -> Unit = {},
    onHideKeyboardOnSendChange: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var editedUsername by remember { mutableStateOf(username) }
    var showUsernameDialog by remember { mutableStateOf(false) }
    var showServerUrlDialog by remember { mutableStateOf(false) }
    var showApiKeyDialog by remember { mutableStateOf(false) }
    var tempServerUrl by remember { mutableStateOf(serverUrl) }
    var tempApiKey by remember { mutableStateOf(apiKey) }
    var showTelegramDialog by remember { mutableStateOf(false) }
    var showDeviceInfoDialog by remember { mutableStateOf(false) }
    var tempTelegramWebhook by remember { mutableStateOf(telegramWebhook) }
    val context = LocalContext.current

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Настройки") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Author Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.tertiary
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(60.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "bitplugg",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "AI Assistant",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/bitplugg/ai-phone-app/releases"))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                        ) {
                            Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("GitHub")
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = "Версия: 0.0.debug",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // AI Mode Selection
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Режим AI",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = aiMode == AIMode.OLLAMA,
                            onClick = { onAiModeChange(AIMode.OLLAMA) },
                            label = { Text("WiFi") },
                            leadingIcon = {
                                Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        )
                        FilterChip(
                            selected = aiMode == AIMode.CLOUD,
                            onClick = { onAiModeChange(AIMode.CLOUD) },
                            label = { Text("OpenAI") },
                            leadingIcon = {
                                Icon(Icons.Default.Cloud, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        )
                        FilterChip(
                            selected = aiMode == AIMode.DEEPSEEK,
                            onClick = { onAiModeChange(AIMode.DEEPSEEK) },
                            label = { Text("DeepSeek") },
                            leadingIcon = {
                                Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Connection status
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = if (isConnected) Icons.Default.CloudDone else Icons.Default.CloudOff,
                            contentDescription = null,
                            tint = if (isConnected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isConnected) "Подключено" else "Не подключено",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isConnected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(onClick = onCheckConnection) {
                            Text("Проверить")
                        }
                    }
                }
            }

            // Settings based on mode
            if (aiMode == AIMode.OLLAMA) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    onClick = { showServerUrlDialog = true }
                ) {
                    ListItem(
                        headlineContent = { Text("IP сервер Ollama") },
                        supportingContent = { Text(serverUrl) },
                        leadingContent = { Icon(Icons.Default.Router, contentDescription = null) },
                        trailingContent = { Icon(Icons.Default.Edit, contentDescription = null) }
                    )
                }
            } else if (aiMode == AIMode.CLOUD) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    onClick = { showApiKeyDialog = true }
                ) {
                    ListItem(
                        headlineContent = { Text("OpenAI API Key") },
                        supportingContent = { 
                            Text(if (apiKey.isNotEmpty()) "${apiKey.take(8)}..." else "Не настроен") 
                        },
                        leadingContent = { Icon(Icons.Default.Key, contentDescription = null) },
                        trailingContent = { Icon(Icons.Default.Edit, contentDescription = null) }
                    )
                }
            } else if (aiMode == AIMode.DEEPSEEK) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    onClick = { showApiKeyDialog = true }
                ) {
                    ListItem(
                        headlineContent = { Text("DeepSeek API Key") },
                        supportingContent = { 
                            Text(if (apiKey.isNotEmpty()) "${apiKey.take(8)}..." else "Не настроен") 
                        },
                        leadingContent = { Icon(Icons.Default.Key, contentDescription = null) },
                        trailingContent = { Icon(Icons.Default.Edit, contentDescription = null) }
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Profile
            ListItem(
                headlineContent = { Text("Профиль") },
                supportingContent = { Text(username) },
                leadingContent = { Icon(Icons.Default.Person, contentDescription = null) },
                trailingContent = {
                    IconButton(onClick = { showUsernameDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Изменить")
                    }
                }
            )

            // Dark theme
            ListItem(
                headlineContent = { Text("Тёмная тема") },
                supportingContent = { Text(if (darkTheme) "Включена" else "Выключена") },
                leadingContent = { Icon(if (darkTheme) Icons.Default.DarkMode else Icons.Default.LightMode, contentDescription = null) },
                trailingContent = { Switch(checked = darkTheme, onCheckedChange = onDarkThemeChange) }
            )

            // Auto voice
            ListItem(
                headlineContent = { Text("Автоотправка голоса") },
                supportingContent = { Text("Автоматически отправлять после распознавания") },
                leadingContent = { Icon(Icons.Default.Mic, contentDescription = null) },
                trailingContent = { Switch(checked = autoSendVoice, onCheckedChange = onAutoSendVoiceChange) }
            )

            // TTS
            ListItem(
                headlineContent = { Text("Озвучивание ответов") },
                supportingContent = { Text("AI будет читать ответы вслух") },
                leadingContent = { Icon(Icons.Default.VolumeUp, contentDescription = null) },
                trailingContent = { Switch(checked = ttsEnabled, onCheckedChange = onTtsEnabledChange) }
            )

            // Text size
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.FormatSize, contentDescription = null)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Размер текста")
                        }
                        Text("${textSize.toInt()}sp", style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Slider(
                        value = textSize,
                        onValueChange = onTextSizeChange,
                        valueRange = 12f..24f,
                        steps = 5
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Export all chats
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onClick = onExportAllChats
            ) {
                ListItem(
                    headlineContent = { Text("Экспорт всех чатов") },
                    supportingContent = { Text("Сохранить в TXT файл") },
                    leadingContent = { Icon(Icons.Default.FileDownload, contentDescription = null) }
                )
            }

            // Copy all messages
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onClick = onCopyAllMessages
            ) {
                ListItem(
                    headlineContent = { Text("Копировать все сообщения") },
                    supportingContent = { Text("Скопировать всю историю в буфер") },
                    leadingContent = { Icon(Icons.Default.ContentCopy, contentDescription = null) }
                )
            }

            // Clear all chats
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onClick = onClearAllChats
            ) {
                ListItem(
                    headlineContent = { 
                        Text("Удалить все чаты", color = MaterialTheme.colorScheme.error) 
                    },
                    supportingContent = { Text("Удалить всю историю чатов") },
                    leadingContent = { 
                        Icon(
                            Icons.Default.DeleteForever, 
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        ) 
                    }
                )
            }

            // Theme Color Selection
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Palette, contentDescription = null)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Цвет темы")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf(
                            "system" to "Системный",
                            "purple" to "Фиолетовый",
                            "blue" to "Синий",
                            "green" to "Зелёный",
                            "orange" to "Оранжевый",
                            "red" to "Красный"
                        ).forEach { (color, name) ->
                            FilterChip(
                                selected = themeColor == color,
                                onClick = { onThemeColorChange(color) },
                                label = { Text(name, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                    }
                }
            }

            // Telegram Webhook
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onClick = { showTelegramDialog = true }
            ) {
                ListItem(
                    headlineContent = { Text("Telegram уведомления") },
                    supportingContent = { 
                        Text(if (telegramWebhook.isNotBlank()) "Настроены" else "Не настроены") 
                    },
                    leadingContent = { Icon(Icons.Default.Send, contentDescription = null) },
                    trailingContent = { Icon(Icons.Default.Edit, contentDescription = null) }
                )
            }

            // Device Info
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onClick = { showDeviceInfoDialog = true }
            ) {
                ListItem(
                    headlineContent = { Text("Информация об устройстве") },
                    supportingContent = { Text("Нажмите для подробностей") },
                    leadingContent = { Icon(Icons.Default.PhoneAndroid, contentDescription = null) }
                )
            }

            // Token Counter
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                ListItem(
                    headlineContent = { Text("Использовано токенов") },
                    supportingContent = { Text("$tokenCount токенов") },
                    leadingContent = { Icon(Icons.Default.Token, contentDescription = null) },
                    trailingContent = {
                        TextButton(onClick = onResetTokenCount) {
                            Text("Сбросить")
                        }
                    }
                )
            }

            // Notifications
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Notifications, contentDescription = null)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Уведомления")
                        }
                        Switch(checked = notificationsEnabled, onCheckedChange = onNotificationsChange)
                    }
                    if (notificationsEnabled) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Уведомления о сообщениях", style = MaterialTheme.typography.bodySmall)
                            Switch(checked = messageNotifications, onCheckedChange = onMessageNotificationsChange)
                        }
                    }
                }
            }

            // Chat Settings
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TextSnippet, contentDescription = null)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Показывать \"печатает...\"")
                        }
                        Switch(checked = showTypingIndicator, onCheckedChange = onTypingIndicatorChange)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.SwapVert, contentDescription = null)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Авто-прокрутка")
                        }
                        Switch(checked = autoScroll, onCheckedChange = onAutoScrollChange)
                    }
                }
            }

            // Sound & Vibration
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.VolumeUp, contentDescription = null)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Звук")
                        }
                        Switch(checked = soundEnabled, onCheckedChange = onSoundChange)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Vibration, contentDescription = null)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Вибрация")
                        }
                        Switch(checked = vibrationEnabled, onCheckedChange = onVibrationChange)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, contentDescription = null)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Показывать время")
                        }
                        Switch(checked = showTimestamps, onCheckedChange = onTimestampChange)
                    }
                }
            }

            // AI Settings
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Psychology, contentDescription = null)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Настройки AI")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text("Temperature: ${String.format("%.1f", aiTemperature)}", style = MaterialTheme.typography.bodySmall)
                    Slider(
                        value = aiTemperature,
                        onValueChange = onTemperatureChange,
                        valueRange = 0.1f..2.0f,
                        steps = 18
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text("Max Tokens: $maxTokens", style = MaterialTheme.typography.bodySmall)
                    Slider(
                        value = maxTokens.toFloat(),
                        onValueChange = { onMaxTokensChange(it.toInt()) },
                        valueRange = 256f..4096f,
                        steps = 14
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Streaming ответ", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = streamingResponse, onCheckedChange = onStreamingChange)
                    }
                }
            }

            // Language
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Language, contentDescription = null)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Язык приложения")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            "ru" to "Русский",
                            "en" to "English",
                            "uk" to "Українська"
                        ).forEach { (code, name) ->
                            FilterChip(
                                selected = appLanguage == code,
                                onClick = { onLanguageChange(code) },
                                label = { Text(name) }
                            )
                        }
                    }
                }
            }

            // App Lock
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Блокировка приложения")
                        }
                        Switch(checked = appLockEnabled, onCheckedChange = onAppLockEnabledChange)
                    }
                    if (appLockEnabled) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("PIN: ${appLockPin.take(4)}${if (appLockPin.length > 4) "****" else ""}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            // Chat Settings Extended
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Chat, contentDescription = null)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Настройки чата")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Markdown", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = markdownEnabled, onCheckedChange = onMarkdownEnabledChange)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Подсветка кода", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = codeHighlight, onCheckedChange = onCodeHighlightChange)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Голосовой ответ", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = voiceReply, onCheckedChange = onVoiceReplyChange)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Свайп для ответа", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = swipeReply, onCheckedChange = onSwipeReplyChange)
                    }
                }
            }

            // Input Settings
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Keyboard, contentDescription = null)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Ввод текста")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Авто-пробел", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = autoSpace, onCheckedChange = onAutoSpaceChange)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Двойной пробел = точка", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = doubleSpace, onCheckedChange = onDoubleSpaceChange)
                    }
                }
            }

            // Night Mode
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.NightsStay, contentDescription = null)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Ночной режим")
                        }
                        Switch(checked = nightModeAuto, onCheckedChange = onNightModeAutoChange)
                    }
                    if (nightModeAuto) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("${nightModeStart} - ${nightModeEnd}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            // Auto Delete
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoDelete, contentDescription = null)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Автоудаление чатов")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Через $autoDeleteDays дней", style = MaterialTheme.typography.bodySmall)
                    Slider(
                        value = autoDeleteDays.toFloat(),
                        onValueChange = { onAutoDeleteDaysChange(it.toInt()) },
                        valueRange = 0f..90f,
                        steps = 89
                    )
                }
            }

            // Widget & Encryption
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Widgets, contentDescription = null)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Виджет на главном экране")
                        }
                        Switch(checked = widgetEnabled, onCheckedChange = onWidgetEnabledChange)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Шифрование чатов")
                        }
                        Switch(checked = encryptEnabled, onCheckedChange = onEncryptEnabledChange)
                    }
                }
            }

            // Animations & Gestures
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Animation, contentDescription = null)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Анимации и жесты")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Анимации", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = animationEnabled, onCheckedChange = onAnimationEnabledChange)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Свайп для удаления", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = swipeToDelete, onCheckedChange = onSwipeToDeleteChange)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Меню по долгому нажатию", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = longPressMenu, onCheckedChange = onLongPressMenuChange)
                    }
                }
            }

            // Chat Bubble Settings
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ChatBubble, contentDescription = null)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Настройка пузырей сообщений")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Скругление: ${messageBubbleRoundness.toInt()}px", style = MaterialTheme.typography.bodySmall)
                    Slider(
                        value = messageBubbleRoundness,
                        onValueChange = onMessageBubbleRoundnessChange,
                        valueRange = 0f..32f,
                        steps = 7
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf(
                            "blue" to "Синий",
                            "green" to "Зелёный",
                            "purple" to "Фиолетовый",
                            "orange" to "Оранжевый"
                        ).forEach { (color, name) ->
                            FilterChip(
                                selected = defaultChatBubbleColor == color,
                                onClick = { onDefaultChatBubbleColorChange(color) },
                                label = { Text(name, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                    }
                }
            }

            // Input Behavior
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Поведение ввода")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Сохранять черновик", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = autoSaveDraft, onCheckedChange = onAutoSaveDraftChange)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Кнопка быстрой отправки", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = quickSendButton, onCheckedChange = onQuickSendButtonChange)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Скрывать клавиатуру после отправки", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = hideKeyboardOnSend, onCheckedChange = onHideKeyboardOnSendChange)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Подтверждение перед отправкой", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = confirmBeforeSend, onCheckedChange = onConfirmBeforeSendChange)
                    }
                }
            }

            // Display Options
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Visibility, contentDescription = null)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Параметры отображения")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Показывать кол-во слов", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = showWordCount, onCheckedChange = onShowWordCountChange)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Авто-раскрытие URL", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = autoExpandUrls, onCheckedChange = onAutoExpandUrlsChange)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Статус доставки", style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = showDeliveryStatus, onCheckedChange = onShowDeliveryStatusChange)
                    }
                }
            }

            // Backup
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onClick = onBackupSettings
            ) {
                ListItem(
                    headlineContent = { Text("Backup настроек") },
                    supportingContent = { Text("Сохранить настройки") },
                    leadingContent = { Icon(Icons.Default.Backup, contentDescription = null) }
                )
            }

            // Clear Cache
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onClick = onClearCache
            ) {
                ListItem(
                    headlineContent = { Text("Очистить кэш") },
                    supportingContent = { Text("Очистить временные данные") },
                    leadingContent = { Icon(Icons.Default.CleaningServices, contentDescription = null) }
                )
            }
        }
    }

    // Dialogs
    if (showUsernameDialog) {
        AlertDialog(
            onDismissRequest = { showUsernameDialog = false },
            title = { Text("Изменить имя") },
            text = {
                OutlinedTextField(
                    value = editedUsername,
                    onValueChange = { editedUsername = it },
                    label = { Text("Имя") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onUsernameChange(editedUsername)
                    showUsernameDialog = false
                }) { Text("Сохранить") }
            },
            dismissButton = {
                TextButton(onClick = { showUsernameDialog = false }) { Text("Отмена") }
            }
        )
    }

    if (showServerUrlDialog) {
        AlertDialog(
            onDismissRequest = { showServerUrlDialog = false },
            title = { Text("IP сервер Ollama") },
            text = {
                Column {
                    Text("Введите IP адрес ПК с Ollama", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = tempServerUrl,
                        onValueChange = { tempServerUrl = it },
                        label = { Text("URL (напр. http://192.168.1.100:11434)") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onServerUrlChange(tempServerUrl)
                    showServerUrlDialog = false
                }) { Text("Сохранить") }
            },
            dismissButton = {
                TextButton(onClick = { showServerUrlDialog = false }) { Text("Отмена") }
            }
        )
    }

    if (showApiKeyDialog) {
        AlertDialog(
            onDismissRequest = { showApiKeyDialog = false },
            title = { Text("OpenAI API Key") },
            text = {
                Column {
                    Text("Введите API ключ OpenAI", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = tempApiKey,
                        onValueChange = { tempApiKey = it },
                        label = { Text("API Key (sk-...)") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onApiKeyChange(tempApiKey)
                    showApiKeyDialog = false
                }) { Text("Сохранить") }
            },
            dismissButton = {
                TextButton(onClick = { showApiKeyDialog = false }) { Text("Отмена") }
            }
        )
    }

    // Telegram Webhook Dialog
    if (showTelegramDialog) {
        AlertDialog(
            onDismissRequest = { showTelegramDialog = false },
            title = { Text("Telegram Webhook") },
            text = {
                Column {
                    Text("Введите токен бота", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = tempTelegramWebhook,
                        onValueChange = { tempTelegramWebhook = it },
                        label = { Text("Bot Token (123456:ABC-DEF...)") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onTelegramWebhookChange(tempTelegramWebhook)
                    showTelegramDialog = false
                }) { Text("Сохранить") }
            },
            dismissButton = {
                TextButton(onClick = { showTelegramDialog = false }) { Text("Отмена") }
            }
        )
    }

    // Device Info Dialog
    if (showDeviceInfoDialog) {
        AlertDialog(
            onDismissRequest = { showDeviceInfoDialog = false },
            title = { Text("Информация об устройстве") },
            text = {
                Text(deviceInfo, style = MaterialTheme.typography.bodyMedium)
            },
            confirmButton = {
                TextButton(onClick = { showDeviceInfoDialog = false }) { Text("OK") }
            }
        )
    }
}
