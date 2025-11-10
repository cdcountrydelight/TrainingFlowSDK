package com.cd.trainingsdk.data.mappers.flow_details_mappers

import com.cd.trainingsdk.data.entity.flow_details.CoordinatesEntity
import com.cd.trainingsdk.domain.contents.flow_details.CoordinatesContent
import com.cd.trainingsdk.domain.domain_utils.IBaseMapper

internal class CoordinatesMapper : IBaseMapper<CoordinatesEntity?, CoordinatesContent?> {

    override fun mapData(data: CoordinatesEntity?): CoordinatesContent? {
        return if (data?.x == null || data.y == null) {
            null
        } else {
            CoordinatesContent(
                x = data.x,
                y = data.y,
                angle = data.angle ?: 0.0,
                radius = data.radius,
                width = data.width,
                height = data.height,
                scaleX = data.scaleX ?: 0.0,
                scaleY = data.scaleY ?: 0.0
            )
        }
    }
}