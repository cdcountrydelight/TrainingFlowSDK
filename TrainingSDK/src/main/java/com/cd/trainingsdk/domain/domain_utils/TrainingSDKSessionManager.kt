package com.cd.trainingsdk.domain.domain_utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object TrainingSDKSessionManager {

    private val _triggerLogout = MutableSharedFlow<Unit>()

    val triggerLogout: SharedFlow<Unit> get() = _triggerLogout

    suspend fun triggerLogout() {
        _triggerLogout.emit(Unit)
    }
}
