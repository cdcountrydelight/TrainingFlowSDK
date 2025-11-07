package com.cd.trainingsdk.data.mappers.flow_details_mappers

import com.cd.trainingsdk.data.entity.FlowDetailsResponseEntity
import com.cd.trainingsdk.data.mappers.flow_list_mappers.UserProgressDetailsMapper
import com.cd.trainingsdk.domain.contents.flow_details.FlowDetailsResponseContent
import com.cd.trainingsdk.domain.domain_utils.IBaseMapper

internal class FlowDetailsMapper : IBaseMapper<FlowDetailsResponseEntity, FlowDetailsResponseContent?> {
    override fun mapData(data: FlowDetailsResponseEntity): FlowDetailsResponseContent? {
        return if (data.id == null) {
            null
        } else {
            val userProgressMapper = UserProgressDetailsMapper()
            val stepsMapper = StepsMapper()
            return FlowDetailsResponseContent(
                id = data.id,
                isActive = data.isActive,
                stepCount = data.stepCount,
                userProgress = data.userProgress?.let { userProgressMapper.mapData(it) },
                steps = data.steps?.mapNotNull { stepsMapper.mapData(it) }
                    ?.sortedBy { it.stepNumber }
                    ?: emptyList()
            )
        }

    }
}