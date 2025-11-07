package com.cd.trainingsdk.data.entity.flow_details

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
internal data class AnnotationItemResponseEntity(
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("stroke")
    val strokeColor: String? = null,
    @SerializedName("coordinates")
    val coordinates: CoordinatesEntity? = null,
    @SerializedName("strokeWidth")
    val strokeWidth: Int? = null
)