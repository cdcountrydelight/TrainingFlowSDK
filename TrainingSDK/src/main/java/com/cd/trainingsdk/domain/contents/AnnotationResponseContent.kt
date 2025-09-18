package com.cd.trainingsdk.domain.contents

import kotlinx.serialization.Serializable

@Serializable
internal data class AnnotationResponseContent(
    val annotations: List<AnnotationItemResponseContent>
)