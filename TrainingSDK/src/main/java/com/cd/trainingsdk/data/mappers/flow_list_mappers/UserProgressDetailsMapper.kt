package com.cd.trainingsdk.data.mappers.flow_list_mappers

import com.cd.trainingsdk.data.entity.UserProgressResponseEntity
import com.cd.trainingsdk.domain.contents.UserProgressResponseContent
import com.cd.trainingsdk.domain.domain_utils.IBaseMapper

internal class UserProgressDetailsMapper : IBaseMapper<UserProgressResponseEntity, UserProgressResponseContent> {
    override fun mapData(data: UserProgressResponseEntity): UserProgressResponseContent {
        return UserProgressResponseContent(
            isStarted = data.isStarted,
            isCompleted = data.isCompleted,
            startedAt = data.startedAt,
            completedAt = data.completedAt
        )
    }

}