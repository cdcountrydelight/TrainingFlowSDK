package com.cd.trainingsdk.domain.domain_utils

internal interface IBaseMapper<Current, Expected> {
    fun mapData(data: Current): Expected
}