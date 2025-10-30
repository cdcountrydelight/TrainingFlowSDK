package com.cd.trainingsdk.data.mappers.qna

import com.cd.trainingsdk.data.entity.CompleteQnAResponseEntity
import com.cd.trainingsdk.domain.contents.CompleteQnaResponseContent
import com.cd.trainingsdk.domain.domain_utils.IBaseMapper

class CompleteQnAResponseEntityToContentMapper: IBaseMapper<CompleteQnAResponseEntity, CompleteQnaResponseContent> {
    override fun mapData(data: CompleteQnAResponseEntity): CompleteQnaResponseContent {
        return CompleteQnaResponseContent(data.calculatedScore)
    }
}