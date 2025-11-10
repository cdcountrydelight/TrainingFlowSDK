package com.cd.trainingsdk.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.cd.trainingsdk.domain.domain_utils.AppErrorCodes
import com.cd.trainingsdk.domain.domain_utils.DataResponseStatus
import com.cd.trainingsdk.domain.domain_utils.IBaseMapper
import com.cd.trainingsdk.domain.domain_utils.TrainingSDKSessionManager
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

internal object NetworkCallHelper {

    suspend inline fun <reified NetworkReturn> networkCall(
        crossinline api: suspend () -> HttpResponse,
    ): DataResponseStatus<NetworkReturn> {

        return try {
            val response = api()
            if (response.status.isSuccess()) {
                val responseBody = response.body<NetworkReturn>()
                DataResponseStatus.success(responseBody)
            } else {
                val errorDetails = getErrorDetails(response)
                DataResponseStatus.failure(
                    errorMessage = errorDetails.errorMessage,
                    errorCode = errorDetails.errorCode
                )
            }
        } catch (exception: Exception) {
            val networkErrorDetails = parseError(exception)
            DataResponseStatus.failure(
                errorMessage = networkErrorDetails.errorMessage,
                errorCode = networkErrorDetails.errorCode
            )
        }
    }

    private suspend fun getErrorDetails(response: HttpResponse): NetworkError {
        val responseBody = response.bodyAsText()
        val responseStatus = response.status.value

        return try {
            val json = Json.parseToJsonElement(responseBody) as? JsonObject
            val detailMessage = json?.get("detail")?.jsonPrimitive?.contentOrNull
            if (detailMessage != null) {
                NetworkError(detailMessage, responseStatus)
            } else {
                NetworkError(responseBody, responseStatus)
            }
        } catch (_: Exception) {
            NetworkError(responseBody, responseStatus)
        }
    }

    private suspend fun parseError(exception: Exception?): NetworkError {

        return when (exception) {
            is NoInternetConnectionException -> NetworkError(
                "",
                AppErrorCodes.NO_INTERNET_CONNECTION_ERROR
            )


            is AuthenticationException -> {
                TrainingSDKSessionManager.triggerLogout()
                NetworkError(
                    "",
                    AppErrorCodes.AUTHENTICATION_EXCEPTION
                )
            }


            else -> NetworkError(
                exception?.message ?: "",
                AppErrorCodes.UNKNOWN_ERROR
            )
        }

    }

    suspend inline fun <reified NetworkReturn, reified FinalExpected> networkCall(
        mapper: IBaseMapper<NetworkReturn, FinalExpected>,
        crossinline api: suspend () -> HttpResponse,
    ): DataResponseStatus<FinalExpected> {
        val response = networkCall<NetworkReturn>(api)
        return try {
            when (response) {
                is DataResponseStatus.Success -> {
                    DataResponseStatus.success(mapper.mapData(response.data))
                }

                is DataResponseStatus.Failure -> {
                    DataResponseStatus.failure(response.errorMessage, response.errorCode)
                }
            }
        } catch (exception: Exception) {
            DataResponseStatus.failure(
                errorMessage = exception.message ?: "",
                errorCode = AppErrorCodes.UNKNOWN_ERROR
            )
        }
    }

    fun Context.isNetworkAvailable(): Boolean {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val networkCapabilities = connectivityManager?.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}