package com.cd.trainingsdk.domain.contents.flow_details

import kotlinx.serialization.Serializable

@Serializable
internal data class AnnotationResponseContent(
    val annotations: List<AnnotationItemResponseContent>
)