package com.cd.trainingsdk.data.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson

internal object HttpClientManager {

    @Volatile
    private var httpClient: HttpClient? = null

    fun getInstance(
        context: Context,
        authToken: String,
        isProdEnv: Boolean,
    ): HttpClient {
        return httpClient ?: synchronized(this) {
            httpClient ?: createHttpClient(
                context,
                authToken,
                isProdEnv,
            ).also { httpClient = it }
        }
    }

    private fun createHttpClient(
        context: Context,
        authToken: String,
        isProdEnv: Boolean,
    ): HttpClient {
        val engine = OkHttp.create {
            if (!isProdEnv) {
                addInterceptor(getChuckerInterceptor(context))
            }
            addInterceptor(LibraryNetworkInterceptorImpl(context))
        }
        return HttpClient(engine) {
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                    disableHtmlEscaping()
                }
            }

            defaultRequest {
                url(if (isProdEnv) "https://stock.countrydelight.in/api/cd_training/flows/" else "https://qa-stock.countrydelight.in/api/cd_training/flows/")
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $authToken")
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 15_000
                socketTimeoutMillis = 15_000
            }

        }
    }


    private fun getChuckerInterceptor(context: Context): ChuckerInterceptor {
        val chuckerCollector = ChuckerCollector(
            context = context,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )

        return ChuckerInterceptor.Builder(context)
            .collector(chuckerCollector)
            .maxContentLength(250_000L)
            .build()
    }

    fun clearInstance() {
        synchronized(this) {
            httpClient?.close()
            httpClient = null
        }
    }
}