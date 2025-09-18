package com.cd.trainingsdk.data.network

import java.io.IOException

internal class NoInternetConnectionException : IOException() {
    override val message: String
        get() = "No internet connection"
}