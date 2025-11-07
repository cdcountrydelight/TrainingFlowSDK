package com.cd.trainingsdk.data.mappers.qna

import com.cd.trainingsdk.data.entity.qna_list.OptionsEntity
import com.cd.trainingsdk.domain.contents.qna_list.OptionsContent
import com.cd.trainingsdk.domain.domain_utils.IBaseMapper

internal class OptionsResponseEntityToContentMapper : IBaseMapper<OptionsEntity, OptionsContent?> {
    override fun mapData(data: OptionsEntity): OptionsContent? {
        return if (data.optionId == null || data.option.isNullOrBlank()) {
            null
        } else {
            OptionsContent(data.optionId, data.option)
        }
    }
}