package com.cd.trainingsdk.data.mappers.qna

import com.cd.trainingsdk.data.entity.QnaResponseEntity
import com.cd.trainingsdk.domain.contents.qna_list.QnaResponseContent
import com.cd.trainingsdk.domain.domain_utils.IBaseMapper

internal class QnAResponseEntityToContentMapper : IBaseMapper<QnaResponseEntity, QnaResponseContent?> {
    override fun mapData(data: QnaResponseEntity): QnaResponseContent? {
        val questionMapper = QuestionResponseEntityToContentMapper()
        val mappedQuestion = data.question?.mapNotNull { questionMapper.mapData(it) }
        return if (data.id == null || data.flowId == null || mappedQuestion == null) {
            null
        } else {
            QnaResponseContent(data.id, mappedQuestion, data.flowId)
        }
    }
}