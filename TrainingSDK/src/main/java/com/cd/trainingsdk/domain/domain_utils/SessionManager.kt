package com.cd.trainingsdk.domain.domain_utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object SessionManager {

    private val _logoutEvents = MutableSharedFlow<Unit>()

    val logoutEvents: SharedFlow<Unit> get() = _logoutEvents

    suspend fun triggerLogout() {
        _logoutEvents.emit(Unit)
    }
}
