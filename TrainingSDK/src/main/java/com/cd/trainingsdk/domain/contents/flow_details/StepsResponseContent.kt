package com.cd.trainingsdk.domain.contents.flow_details

import kotlinx.serialization.Serializable

@Serializable
internal data class StepsResponseContent(
    val id: Int,
    val stepNumber: Int,
    val height: Double,
    val width: Double,
    val screenshotUrl: String,
    val annotation: AnnotationResponseContent?,
    val instructions: List<String>
)