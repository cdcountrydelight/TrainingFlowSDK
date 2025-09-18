package com.cd.trainingsdk.data.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
internal data class CoordinatesEntity(
    @SerializedName("x")
    val x: Double?,
    @SerializedName("y")
    val y: Double?,
    @SerializedName("angle")
    val angle: Double?,
    @SerializedName("radius")
    val radius: Double? = null,
    @SerializedName("width")
    val width: Double? = null,
    @SerializedName("height")
    val height: Double? = null,
    @SerializedName("scaleX")
    val scaleX: Double?,
    @SerializedName("scaleY")
    val scaleY: Double?
)