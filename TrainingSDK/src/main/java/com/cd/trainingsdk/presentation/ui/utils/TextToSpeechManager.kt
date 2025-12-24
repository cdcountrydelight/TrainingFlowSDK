package com.cd.trainingsdk.presentation.ui.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

internal class TextToSpeechManager(context: Context, private val locale: Locale) {

    private var textToSpeech: TextToSpeech? = null

    private var isInitialized = false

    companion object {
        @Volatile
        private var INSTANCE: TextToSpeechManager? = null

        fun getInstance(context: Context, locale: Locale): TextToSpeechManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TextToSpeechManager(context, locale).also {
                    INSTANCE = it
                }
            }
        }

        fun speak(text: String) {
            INSTANCE?.let {
                if (!it.isInitialized) {
                    return
                }
                stop()
                it.textToSpeech?.speak(
                    text,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "tts_${System.currentTimeMillis()}"
                )
            }
        }

        fun stop() {
            INSTANCE?.let {
                it.textToSpeech?.stop()
            }
        }

        fun shutdown() {
            INSTANCE?.let {
                stop()
                it.textToSpeech?.shutdown()
                it.textToSpeech = null
                it.isInitialized = false
            }
        }
    }

    init {
        textToSpeech = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                setLanguage(locale)
            } else {
                isInitialized = false
            }
        }
    }

    fun setLanguage(locale: Locale): Boolean {
        val tts = textToSpeech ?: return false
        val availability = tts.isLanguageAvailable(locale)
        if (availability < TextToSpeech.LANG_AVAILABLE) {
            isInitialized = false
            return false
        }
        val result = tts.setLanguage(locale)
        isInitialized = result != TextToSpeech.LANG_MISSING_DATA &&
                result != TextToSpeech.LANG_NOT_SUPPORTED
        if (isInitialized) {
            tts.setSpeechRate(1.0f)
            tts.setPitch(1.0f)
        }
        return isInitialized
    }

}