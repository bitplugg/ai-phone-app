package com.aiapp.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun CopyButton(
    text: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    IconButton(
        onClick = {
            copyToClipboard(context, text)
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = "Копировать"
        )
    }
}

fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Message", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "Скопировано", Toast.LENGTH_SHORT).show()
}
