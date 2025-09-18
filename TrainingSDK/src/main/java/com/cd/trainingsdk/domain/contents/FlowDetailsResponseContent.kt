package com.cd.trainingsdk.domain.contents

import kotlinx.serialization.Serializable

@Serializable
internal data class FlowDetailsResponseContent(
    val id: Int?,
    val isActive: Boolean?,
    val stepCount: Int?,
    val userProgress: UserProgressResponseContent?,
    val steps: List<StepsResponseContent>?
)
