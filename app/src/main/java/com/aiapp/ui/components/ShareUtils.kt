package com.aiapp.ui.components

import android.content.Context
import android.content.Intent

object ShareUtils {
    fun shareText(context: Context, text: String, title: String = "Поделиться") {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, title)
        context.startActivity(shareIntent)
    }

    fun shareMessage(context: Context, messageText: String, senderName: String) {
        val formattedText = "$senderName: $messageText"
        shareText(context, formattedText)
    }
}
