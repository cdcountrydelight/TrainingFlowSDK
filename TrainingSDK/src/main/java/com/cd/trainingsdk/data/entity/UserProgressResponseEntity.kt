package com.cd.trainingsdk.data.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserProgressResponseEntity(
    @SerializedName("started")
    val isStarted: Boolean? = null,
    @SerializedName("completed")
    val isCompleted: Boolean? = null,
    @SerializedName("started_at")
    val startedAt: String? = null,
    @SerializedName("completed_at")
    val completedAt: String? = null,
)