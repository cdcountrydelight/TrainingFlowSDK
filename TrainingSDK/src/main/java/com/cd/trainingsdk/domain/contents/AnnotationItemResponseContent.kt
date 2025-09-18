package com.cd.trainingsdk.domain.contents

import kotlinx.serialization.Serializable

@Serializable
internal data class AnnotationItemResponseContent(
    val type: String?,
    val strokeColor: String?,
    val coordinates: CoordinatesContent,
    val strokeWidth: Float?
)