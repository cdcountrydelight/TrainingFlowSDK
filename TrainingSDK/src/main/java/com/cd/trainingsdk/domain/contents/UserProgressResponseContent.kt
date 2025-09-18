package com.cd.trainingsdk.domain.contents

import kotlinx.serialization.Serializable

@Serializable
internal data class UserProgressResponseContent(
    val isStarted: Boolean? = null,
    val isCompleted: Boolean? = null,
    val startedAt: String? = null,
    val completedAt: String? = null,
)