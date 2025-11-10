package com.cd.trainingsdk.domain.contents.flow_list

internal data class FlowListResponseContent(
    val id: Int,
    val name: String?,
    val description: String? = null,
    val isActive: Boolean? = null,
    val stepCount: Int? = null,
    val userProgress: UserProgressResponseContent? = null,
)