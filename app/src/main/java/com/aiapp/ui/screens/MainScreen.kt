package com.aiapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aiapp.data.ai.AIMode
import com.aiapp.data.ai.ModelInfo
import com.aiapp.data.local.Chat
import com.aiapp.data.local.ChatMessage
import com.aiapp.ui.components.CopyButton
import com.aiapp.ui.components.ShareUtils
import com.aiapp.ui.components.VoiceInputButton
import kotlinx.coroutines.launch

enum class Screen {
    CHAT_LIST,
    CHAT,
    MODELS,
    SETTINGS,
    SEARCH
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    currentScreen: Screen,
    chats: List<Chat>,
    currentMessages: List<ChatMessage>,
    currentChatId: String?,
    darkTheme: Boolean,
    username: String,
    allModels: List<ModelInfo>,
    downloadedModels: List<ModelInfo>,
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
    onNavigate: (Screen) -> Unit,
    onCreateChat: () -> Unit,
    onSelectChat: (String) -> Unit,
    onDeleteChat: (String) -> Unit,
    onSendMessage: (String) -> Unit,
    onDeleteMessage: (Long) -> Unit,
    onDarkThemeChange: (Boolean) -> Unit,
    onUsernameChange: (String) -> Unit,
    onSelectedModelChange: (String) -> Unit,
    onAutoSendVoiceChange: (Boolean) -> Unit,
    onAiModeChange: (AIMode) -> Unit,
    onServerUrlChange: (String) -> Unit,
    onApiKeyChange: (String) -> Unit,
    onCheckConnection: () -> Unit,
    onDownloadModel: (ModelInfo) -> Unit,
    onDeleteModel: (ModelInfo) -> Unit,
    onClearChat: (String) -> Unit,
    onSearchMessages: (String) -> List<ChatMessage>,
    onExportChat: (String) -> String,
    onTextSizeChange: (Float) -> Unit = {},
    onTtsEnabledChange: (Boolean) -> Unit = {},
    onExportAllChats: () -> Unit = {},
    onCopyAllMessages: () -> Unit = {},
    onClearAllChats: () -> Unit = {},
    onRefresh: () -> Unit = {},
    onThemeColorChange: (String) -> Unit = {},
    onTelegramWebhookChange: (String) -> Unit = {},
    onBackupSettings: () -> Unit = {},
    onClearCache: () -> Unit = {},
    onResetTokenCount: () -> Unit = {}
) {
    var inputText by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(currentMessages.size) {
        if (currentMessages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(currentMessages.size - 1)
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (currentScreen == Screen.CHAT_LIST || currentScreen == Screen.CHAT) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Chat, contentDescription = null) },
                        label = { Text("Чаты") },
                        selected = currentScreen == Screen.CHAT_LIST,
                        onClick = { onNavigate(Screen.CHAT_LIST) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Psychology, contentDescription = null) },
                        label = { Text("Модели") },
                        selected = currentScreen == Screen.MODELS,
                        onClick = { onNavigate(Screen.MODELS) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                        label = { Text("Настройки") },
                        selected = currentScreen == Screen.SETTINGS,
                        onClick = { onNavigate(Screen.SETTINGS) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Search, contentDescription = null) },
                        label = { Text("Поиск") },
                        selected = currentScreen == Screen.SEARCH,
                        onClick = { onNavigate(Screen.SEARCH) }
                    )
                }
            }
        }
    ) { padding ->
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            label = "screen"
        ) { screen ->
            when (screen) {
                Screen.CHAT_LIST -> {
                    ChatListScreen(
                        chats = chats,
                        currentChatId = currentChatId,
                        onChatClick = onSelectChat,
                        onCreateChat = {
                            onCreateChat()
                            onNavigate(Screen.CHAT)
                        },
                        onDeleteChat = onDeleteChat,
                        modifier = Modifier.padding(padding)
                    )
                }
                Screen.CHAT -> {
                    ChatScreenContent(
                        messages = currentMessages,
                        inputText = inputText,
                        onInputChange = { inputText = it },
                        onSend = {
                            if (inputText.isNotBlank()) {
                                onSendMessage(inputText)
                                inputText = ""
                            }
                        },
                        username = username,
                        selectedModel = downloadedModels.find { it.id == selectedModel }?.name ?: "AI",
                        autoSendVoice = autoSendVoice,
                        currentChatId = currentChatId,
                        onClearChat = onClearChat,
                        onExportChat = onExportChat,
                        modifier = Modifier.padding(padding)
                    )
                }
                Screen.MODELS -> {
                    ModelsScreen(
                        models = allModels,
                        isConnected = isConnected,
                        selectedModel = selectedModel,
                        onSelectModel = onSelectedModelChange,
                        onBackClick = { onNavigate(Screen.CHAT_LIST) },
                        modifier = Modifier.padding(padding)
                    )
                }
                Screen.SETTINGS -> {
                    SettingsScreen(
                        darkTheme = darkTheme,
                        username = username,
                        selectedModel = selectedModel,
                        autoSendVoice = autoSendVoice,
                        aiMode = aiMode,
                        serverUrl = serverUrl,
                        apiKey = apiKey,
                        isConnected = isConnected,
                        textSize = textSize,
                        ttsEnabled = ttsEnabled,
                        themeColor = themeColor,
                        telegramWebhook = telegramWebhook,
                        tokenCount = tokenCount,
                        deviceInfo = deviceInfo,
                        onDarkThemeChange = onDarkThemeChange,
                        onUsernameChange = onUsernameChange,
                        onSelectedModelChange = onSelectedModelChange,
                        onAutoSendVoiceChange = onAutoSendVoiceChange,
                        onAiModeChange = onAiModeChange,
                        onServerUrlChange = onServerUrlChange,
                        onApiKeyChange = onApiKeyChange,
                        onCheckConnection = onCheckConnection,
                        onBackClick = { onNavigate(Screen.CHAT_LIST) },
                        onTextSizeChange = onTextSizeChange,
                        onTtsEnabledChange = onTtsEnabledChange,
                        onThemeColorChange = onThemeColorChange,
                        onTelegramWebhookChange = onTelegramWebhookChange,
                        onBackupSettings = onBackupSettings,
                        onClearCache = onClearCache,
                        onResetTokenCount = onResetTokenCount,
                        onExportAllChats = onExportAllChats,
                        onCopyAllMessages = onCopyAllMessages,
                        onClearAllChats = onClearAllChats,
                        modifier = Modifier.padding(padding)
                    )
                }
                Screen.SEARCH -> {
                    SearchScreen(
                        onSearch = onSearchMessages,
                        onBackClick = { onNavigate(Screen.CHAT_LIST) },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    chats: List<Chat>,
    currentChatId: String?,
    onChatClick: (String) -> Unit,
    onCreateChat: () -> Unit,
    onDeleteChat: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Assistant") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateChat,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "Новый чат")
            }
        },
        modifier = modifier
    ) { padding ->
        if (chats.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Chat,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Нет чатов",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Нажмите + чтобы создать новый чат",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(chats, key = { it.id }) { chat ->
                    ChatListItem(
                        chat = chat,
                        isSelected = chat.id == currentChatId,
                        onClick = { onChatClick(chat.id) },
                        onDelete = { onDeleteChat(chat.id) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListItem(
    chat: Chat,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chat.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = formatTimestamp(chat.updatedAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Удалить",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Удалить чат?") },
            text = { Text("Это действие нельзя отменить.") },
            confirmButton = {
                FilledTonalButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}

@Composable
fun ChatScreenContent(
    messages: List<ChatMessage>,
    inputText: String,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit,
    username: String,
    selectedModel: String,
    autoSendVoice: Boolean = false,
    currentChatId: String? = null,
    onClearChat: ((String) -> Unit)? = null,
    onExportChat: ((String) -> String)? = null,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        var showMenu by remember { mutableStateOf(false) }
        
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 4.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Psychology,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = selectedModel,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Локальная модель",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Меню")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Очистить чат") },
                            onClick = {
                                currentChatId?.let { onClearChat?.invoke(it) }
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.DeleteSweep, contentDescription = null)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Экспорт чата") },
                            onClick = {
                                currentChatId?.let { onExportChat?.invoke(it) }
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.FileDownload, contentDescription = null)
                            }
                        )
                    }
                }
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages, key = { it.id }) { message ->
                MessageBubble(
                    message = message,
                    isUser = message.isUser,
                    username = username
                )
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                VoiceInputButton(
                    onTextRecognized = { text ->
                        val newText = if (inputText.isBlank()) text else "$inputText $text"
                        onInputChange(newText)
                    },
                    autoSend = autoSendVoice,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = inputText,
                    onValueChange = onInputChange,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Введите сообщение...") },
                    maxLines = 4,
                    shape = MaterialTheme.shapes.extraLarge
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilledIconButton(
                    onClick = onSend,
                    enabled = inputText.isNotBlank(),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Отправить")
                }
            }
        }
    }
}

@Composable
fun MessageBubble(
    message: ChatMessage,
    isUser: Boolean,
    username: String
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Psychology,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = if (isUser) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
            tonalElevation = if (isUser) 0.dp else 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (!isUser) {
                    Text(
                        text = "AI",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isUser) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    CopyButton(text = message.text)
                    IconButton(
                        onClick = {
                            ShareUtils.shareMessage(
                                context = context,
                                messageText = message.text,
                                senderName = if (isUser) username else "AI"
                            )
                        }
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Поделиться"
                        )
                    }
                }
            }
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.tertiaryContainer,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = username.firstOrNull()?.uppercase() ?: "U",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    return when {
        diff < 60_000 -> "Только что"
        diff < 3_600_000 -> "${diff / 60_000} мин назад"
        diff < 86_400_000 -> "${diff / 3_600_000} ч назад"
        else -> "${diff / 86_400_000} дн назад"
    }
}
