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
    onExportAllChats: () -> Unit = {},
    onCopyAllMessages: () -> Unit = {},
    onClearAllChats: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var editedUsername by remember { mutableStateOf(username) }
    var showUsernameDialog by remember { mutableStateOf(false) }
    var showServerUrlDialog by remember { mutableStateOf(false) }
    var showApiKeyDialog by remember { mutableStateOf(false) }
    var tempServerUrl by remember { mutableStateOf(serverUrl) }
    var tempApiKey by remember { mutableStateOf(apiKey) }
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
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/bitplugg/ai-app"))
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
}
