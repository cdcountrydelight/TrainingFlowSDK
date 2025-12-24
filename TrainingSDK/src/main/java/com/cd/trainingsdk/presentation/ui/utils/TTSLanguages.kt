package com.cd.trainingsdk.presentation.ui.utils

import java.util.Locale

enum class TTSLanguages(val locale: Locale) {
    ENGLISH(Locale.ENGLISH),
    HINDI(Locale.forLanguageTag("hi-IN")),
    TAMIL(Locale.forLanguageTag("ta-IN")),
    TELUGU(Locale.forLanguageTag("te-IN")),
    KANNADA(Locale.forLanguageTag("kn-IN"));

    companion object {
        fun getTTSLanguageFromLanguageCode(languageCode: String): TTSLanguages {
            return when (languageCode) {
                "hi" -> HINDI
                "ta" -> TAMIL
                "te" -> TELUGU
                "kn" -> KANNADA
                else -> ENGLISH
            }
        }
    }
}