package com.cd.trainingsdk.data.mappers.qna

import com.cd.trainingsdk.data.entity.OptionsEntity
import com.cd.trainingsdk.domain.contents.OptionsContent
import com.cd.trainingsdk.domain.domain_utils.IBaseMapper

class OptionsResponseEntityToContentMapper : IBaseMapper<OptionsEntity, OptionsContent?> {
    override fun mapData(data: OptionsEntity): OptionsContent? {
        return if (data.optionId == null || data.option.isNullOrBlank()) {
            null
        } else {
            OptionsContent(data.optionId, data.option)
        }
    }
}