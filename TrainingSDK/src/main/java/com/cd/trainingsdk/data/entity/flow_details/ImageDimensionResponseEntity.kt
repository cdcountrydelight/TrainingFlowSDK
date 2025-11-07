package com.cd.trainingsdk.data.entity.flow_details

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
internal data class ImageDimensionResponseEntity(
    @SerializedName("width")
    val width: Double? = null,
    @SerializedName("height")
    val height: Double? = null
)