package com.cd.trainingsdk.data.mappers.qna

import com.cd.trainingsdk.data.entity.complete_qna.CompleteQnAResponseEntity
import com.cd.trainingsdk.domain.contents.complete_qna.CompleteQnaResponseContent
import com.cd.trainingsdk.domain.domain_utils.IBaseMapper

internal class CompleteQnAResponseEntityToContentMapper :
    IBaseMapper<CompleteQnAResponseEntity, CompleteQnaResponseContent> {
    override fun mapData(data: CompleteQnAResponseEntity): CompleteQnaResponseContent {
        return CompleteQnaResponseContent(data.calculatedScore)
    }
}