package com.cd.trainingsdk.domain.contents

internal data class CompleteFlowResponseContent(
    val message: String? = null,
    val flowId: Int? = null,
    val flowName: String? = null,
    val completedAt: String? = null,
    val userId: Int? = null
)