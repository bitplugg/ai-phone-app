package com.aiapp.data

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.*

class TTSManager(private val context: Context) {
    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var onComplete: (() -> Unit)? = null

    fun init(onComplete: () -> Unit = {}) {
        this.onComplete = onComplete
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
                tts?.language = Locale("ru", "RU")
                tts?.setSpeechRate(1.0f)
                tts?.setPitch(1.0f)
            }
        }
    }

    fun speak(text: String) {
        if (!isInitialized) return
        
        tts?.stop()
        tts?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "utterance_${System.currentTimeMillis()}"
        )
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
