package com.cd.trainingsdk.data.entity.flow_details

import com.cd.trainingsdk.data.entity.flow_list.UserProgressResponseEntity
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
internal data class FlowDetailsResponseEntity(

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("is_active")
    val isActive: Boolean? = null,

    @SerializedName("step_count")
    val stepCount: Int? = null,

    @SerializedName("user_progress")
    val userProgress: UserProgressResponseEntity? = null,

    @SerializedName("steps")
    val steps: List<StepsResponseEntity>? = null
)