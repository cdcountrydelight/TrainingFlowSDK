package com.cd.trainingsdk.data.mappers.qna

import com.cd.trainingsdk.data.entity.qna_list.QuestionResponseEntity
import com.cd.trainingsdk.domain.contents.qna_list.QuestionResponseContent
import com.cd.trainingsdk.domain.domain_utils.IBaseMapper

internal class QuestionResponseEntityToContentMapper :
    IBaseMapper<QuestionResponseEntity?, QuestionResponseContent?> {
    override fun mapData(data: QuestionResponseEntity?): QuestionResponseContent? {
        val optionsMapper = OptionsResponseEntityToContentMapper()
        val mappedOptions = data?.options?.mapNotNull { optionsMapper.mapData(it) }
        return if (data?.questionId == null || data.question.isNullOrBlank() || mappedOptions == null) {
            null
        } else {
            QuestionResponseContent(
                data.questionId,
                data.question,
                mappedOptions,
                data.isMsq ?: false,
                data.isRequired ?: true
            )
        }
    }
}