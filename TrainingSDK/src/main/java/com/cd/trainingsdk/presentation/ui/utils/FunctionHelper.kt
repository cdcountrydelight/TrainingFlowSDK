package com.cd.trainingsdk.presentation.ui.utils

import android.content.Context
import com.cd.trainingsdk.R
import com.cd.trainingsdk.data.network.HttpClientManager
import com.cd.trainingsdk.domain.domain_utils.AppErrorCodes
import com.cd.trainingsdk.domain.domain_utils.DataResponseStatus
import com.cd.trainingsdk.presentation.ImageLoader
import com.cd.trainingsdk.presentation.ui.beans.LanguageBean

internal object FunctionHelper {

    fun <T> DataResponseStatus<T>.mapToDataUiResponseStatus(): DataUiResponseStatus<T> {
        return when (this) {
            is DataResponseStatus.Success -> DataUiResponseStatus.success(data)
            is DataResponseStatus.Failure -> {
                DataUiResponseStatus.failure(errorMessage, errorCode)
            }
        }
    }

    fun Context.getErrorMessage(errorMessage: String, errorCode: Int): String {
        return when (errorCode) {
            AppErrorCodes.NO_INTERNET_CONNECTION_ERROR -> {
                getString(R.string.no_internet_connection_please_make_sure_your_device_is_connected_to_an_active_internet_connection)
            }


            else -> {
                errorMessage.ifBlank {
                    getString(R.string.something_went_wrong_please_try_again_later)
                }
            }
        }
    }

    fun getAllAvailableLanguages(): List<LanguageBean> {
        val languagesList = mutableListOf<LanguageBean>()
        languagesList.add(LanguageBean("English", "en"))
        languagesList.add(LanguageBean("Hindi (हिन्दी)", "hi"))
        languagesList.add(LanguageBean("Telugu (తెలుగు)", "te"))
        languagesList.add(LanguageBean("Tamil (தமிழ்)", "ta"))
        languagesList.add(LanguageBean("Kannada (ಕನ್ನಡ)", "kn"))
        return languagesList
    }

    fun clearAll() {
        HttpClientManager.clearInstance()
        ImageLoader.clear()
        TextToSpeechManager.shutdown()
    }
}