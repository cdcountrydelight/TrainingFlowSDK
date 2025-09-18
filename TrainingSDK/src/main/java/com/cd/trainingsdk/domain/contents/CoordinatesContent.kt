package com.cd.trainingsdk.domain.contents

import kotlinx.serialization.Serializable

@Serializable
internal data class CoordinatesContent(
    val x: Double,
    val y: Double,
    val angle: Double,
    val radius: Double? = null,
    val width: Double? = null,
    val height: Double? = null,
    val scaleX: Double,
    val scaleY: Double
)