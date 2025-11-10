package com.cd.trainingsdk.data.mappers.flow_details_mappers

import com.cd.trainingsdk.data.entity.flow_details.AnnotationsResponseEntity
import com.cd.trainingsdk.domain.contents.flow_details.AnnotationResponseContent
import com.cd.trainingsdk.domain.domain_utils.IBaseMapper

internal class AnnotationMapper() :
    IBaseMapper<AnnotationsResponseEntity, AnnotationResponseContent?> {

    override fun mapData(data: AnnotationsResponseEntity): AnnotationResponseContent? {
        return data.annotations?.let { annotationList ->
            val annotationItemMapper = AnnotationItemMapper()
            AnnotationResponseContent(
                annotations = annotationList.mapNotNull { annotationItemMapper.mapData(it) }
            )
        }
    }
}