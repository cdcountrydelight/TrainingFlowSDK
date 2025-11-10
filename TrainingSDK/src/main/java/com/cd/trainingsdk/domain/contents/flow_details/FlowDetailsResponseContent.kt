package com.cd.trainingsdk.domain.contents.flow_details

import com.cd.trainingsdk.domain.contents.flow_list.UserProgressResponseContent
import kotlinx.serialization.Serializable

@Serializable
internal data class FlowDetailsResponseContent(
    val id: Int?,
    val isActive: Boolean?,
    val stepCount: Int?,
    val userProgress: UserProgressResponseContent?,
    val steps: List<StepsResponseContent>?
)
