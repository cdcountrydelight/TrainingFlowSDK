package com.cd.trainingsdk.data.mappers.flow_details_mappers

import com.cd.trainingsdk.data.entity.StepsResponseEntity
import com.cd.trainingsdk.domain.contents.StepsResponseContent
import com.cd.trainingsdk.domain.domain_utils.IBaseMapper

internal class StepsMapper() : IBaseMapper<StepsResponseEntity, StepsResponseContent?> {

    override fun mapData(data: StepsResponseEntity): StepsResponseContent? {
        return if (data.id == null || data.stepNumber == null || data.screenshotUrl == null) {
            null
        } else {
            val annotationMapper = AnnotationMapper()
            StepsResponseContent(
                id = data.id,
                stepNumber = data.stepNumber,
                height = data.annotation?.imageDimensions?.height ?: 1280.0,
                width = data.annotation?.imageDimensions?.width ?: 720.0,
                screenshotUrl = data.screenshotUrl,
                annotation = data.annotation?.let { annotationMapper.mapData(it) },
                instructions = data.instructions ?: emptyList()
            )
        }
    }
}