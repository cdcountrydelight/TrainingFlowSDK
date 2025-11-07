package com.cd.trainingsdk.data.entity.qna_list

import com.google.gson.annotations.SerializedName


internal data class OptionsEntity(
    @SerializedName("id") val optionId: String?,
    @SerializedName("text") val option: String?
)