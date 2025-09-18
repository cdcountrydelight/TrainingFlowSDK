package com.cd.trainingsdk.data.mappers.complete_training_flow

import com.cd.trainingsdk.data.entity.CompleteFlowResponseEntity
import com.cd.trainingsdk.domain.contents.CompleteFlowResponseContent
import com.cd.trainingsdk.domain.domain_utils.IBaseMapper

internal class CompleteFlowResponseMapper :
    IBaseMapper<CompleteFlowResponseEntity, CompleteFlowResponseContent> {
    override fun mapData(data: CompleteFlowResponseEntity): CompleteFlowResponseContent {
        return CompleteFlowResponseContent(
            message = data.message,
            flowId = data.flowId,
            flowName = data.flowName,
            completedAt = data.completedAt,
            userId = data.userId
        )
    }
}