package com.cd.trainingsdk.presentation.ui.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

internal fun Context.withSdkLocale(languageCode: String): Context {
    val locale = Locale.forLanguageTag(languageCode)
    Locale.setDefault(locale)
    val config = Configuration(resources.configuration)
    config.setLocale(locale)
    return createConfigurationContext(config)
}
