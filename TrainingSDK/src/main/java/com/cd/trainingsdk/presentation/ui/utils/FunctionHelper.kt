package com.cd.trainingsdk.presentation.ui.utils

import android.content.Context
import com.cd.trainingsdk.R
import com.cd.trainingsdk.data.network.HttpClientManager
import com.cd.trainingsdk.domain.domain_utils.AppErrorCodes
import com.cd.trainingsdk.domain.domain_utils.DataResponseStatus
import com.cd.trainingsdk.presentation.ImageLoader

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

    fun clearAll() {
        HttpClientManager.clearInstance()
        ImageLoader.clear()
    }
}