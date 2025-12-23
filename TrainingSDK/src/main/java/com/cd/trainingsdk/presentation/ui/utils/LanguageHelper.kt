package com.cd.trainingsdk.presentation.ui.utils

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.cd.trainingsdk.domain.domain_utils.SharedPreferenceHelper

internal object LanguageHelper {

    var selectedLanguage by mutableStateOf("en")

    fun setSelectedLanguage(context: Context) {
        val sharedPreference = SharedPreferenceHelper.getSharedPreference(context)
        if (sharedPreference.isLanguageSet) {
            selectedLanguage = sharedPreference.selectedLanguageCode
        } else {
            selectedLanguage = sharedPreference.selectedLanguageCode
        }
    }
}