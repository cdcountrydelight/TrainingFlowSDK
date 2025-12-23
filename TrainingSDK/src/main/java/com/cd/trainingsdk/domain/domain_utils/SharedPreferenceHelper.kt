package com.cd.trainingsdk.domain.domain_utils

import android.content.Context
import androidx.core.content.edit

internal class SharedPreferenceHelper(private val context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(
            "${context.packageName}.trainingsdk.shared_pref",
            Context.MODE_PRIVATE
        )

    companion object {
        private const val SELECTED_LANGUAGE_CODE = "selected_language_code"
        fun getSharedPreference(context: Context): SharedPreferenceHelper {
            return SharedPreferenceHelper(context)
        }
    }

    var selectedLanguageCode: String
        get() {
            return sharedPreferences.getString(SELECTED_LANGUAGE_CODE, "en") ?: "en"
        }
        set(newLanguage) {
            sharedPreferences.edit {
                putString(SELECTED_LANGUAGE_CODE, newLanguage)
            }
        }
}