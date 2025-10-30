package com.cd.trainingsdk.data.mappers.qna

import com.cd.trainingsdk.data.entity.QnaResponseEntity
import com.cd.trainingsdk.domain.contents.QnaResponseContent
import com.cd.trainingsdk.domain.domain_utils.IBaseMapper

class QnAResponseEntityToContentMapper : IBaseMapper<QnaResponseEntity, QnaResponseContent?> {
    override fun mapData(data: QnaResponseEntity): QnaResponseContent? {
        val questionMapper = QuestionResponseEntityToContentMapper()
        val optionsMapper = OptionsResponseEntityToContentMapper()
        val mappedQuestion = questionMapper.mapData(data.question)
        val mappedOptions = data.options?.mapNotNull { optionsMapper.mapData(it) }
        return if (mappedQuestion == null || mappedOptions == null) {
            null
        } else {
            QnaResponseContent(mappedQuestion, mappedOptions, data.isMsq ?: false)
        }
    }
}