package com.cd.trainingsdk.data.mappers.flow_list_mappers

import com.cd.trainingsdk.data.entity.FlowListResponseEntity
import com.cd.trainingsdk.domain.contents.flow_list.FlowListResponseContent
import com.cd.trainingsdk.domain.domain_utils.IBaseMapper

internal class FlowListMapper() : IBaseMapper<FlowListResponseEntity, FlowListResponseContent?> {
    override fun mapData(data: FlowListResponseEntity): FlowListResponseContent? {
        return if (data.isActive != true || data.id == null || data.stepCount == 0) {
            null
        } else {
            val mapper = UserProgressDetailsMapper()
            FlowListResponseContent(
                id = data.id,
                name = data.name,
                description = data.description,
                isActive = true,
                stepCount = data.stepCount,
                userProgress = data.userProgress?.let { mapper.mapData(it) }
            )
        }
    }
}