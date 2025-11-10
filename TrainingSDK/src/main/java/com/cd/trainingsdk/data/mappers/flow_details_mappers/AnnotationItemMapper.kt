package com.cd.trainingsdk.data.mappers.flow_details_mappers

import com.cd.trainingsdk.data.entity.flow_details.AnnotationItemResponseEntity
import com.cd.trainingsdk.domain.contents.flow_details.AnnotationItemResponseContent
import com.cd.trainingsdk.domain.domain_utils.IBaseMapper

internal class AnnotationItemMapper() :
    IBaseMapper<AnnotationItemResponseEntity, AnnotationItemResponseContent?> {
    override fun mapData(data: AnnotationItemResponseEntity): AnnotationItemResponseContent? {
        val coordinates = CoordinatesMapper().mapData(data.coordinates)
        return if (coordinates == null) {
            null
        } else {
            AnnotationItemResponseContent(
                type = data.type,
                strokeColor = data.strokeColor,
                coordinates = coordinates,
                strokeWidth = data.strokeWidth?.toFloat()
            )
        }
    }
}