package com.cd.trainingsdk.data.mappers.qna

import com.cd.trainingsdk.data.entity.QuestionResponseEntity
import com.cd.trainingsdk.domain.contents.QuestionResponseContent
import com.cd.trainingsdk.domain.domain_utils.IBaseMapper

class QuestionResponseEntityToContentMapper :
    IBaseMapper<QuestionResponseEntity?, QuestionResponseContent?> {
    override fun mapData(data: QuestionResponseEntity?): QuestionResponseContent? {
        return if (data?.questionId == null || data.question.isNullOrBlank()) {
            null
        } else {
            QuestionResponseContent(data.questionId, data.question)
        }
    }
}