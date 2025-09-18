package com.cd.trainingsdk.data.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
internal data class AnnotationsResponseEntity(
    @SerializedName("annotations")
    val annotations: List<AnnotationItemResponseEntity>? = null,
    @SerializedName("image_dimensions")
    val imageDimensions: ImageDimensionResponseEntity? = null
)