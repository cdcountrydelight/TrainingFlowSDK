package com.cd.trainingsdk.data.network

import android.content.Context
import com.cd.trainingsdk.data.network.ConstantHelper.unAuthorizedExceptionCodes
import com.cd.trainingsdk.data.network.NetworkCallHelper.isNetworkAvailable
import okhttp3.Interceptor
import okhttp3.Response


internal class LibraryNetworkInterceptorImpl(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        if (!context.isNetworkAvailable()) {
            throw NoInternetConnectionException()
        }
        val response = chain.proceed(request)
        if (response.code in (unAuthorizedExceptionCodes ?: emptyList())) {
            throw AuthenticationException()
        }
        return response
    }
}