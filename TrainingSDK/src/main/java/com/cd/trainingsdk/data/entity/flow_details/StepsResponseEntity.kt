package com.cd.trainingsdk.data.entity.flow_details

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
internal data class StepsResponseEntity(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("step_number")
    val stepNumber: Int? = null,
    @SerializedName("screenshot_url")
    val screenshotUrl: String? = null,
    @SerializedName("annotation")
    val annotation: AnnotationsResponseEntity? = null,
    @SerializedName("instructions")
    val instructions: List<String>? = null
)