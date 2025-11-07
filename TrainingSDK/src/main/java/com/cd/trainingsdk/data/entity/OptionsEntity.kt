package com.cd.trainingsdk.data.entity

import com.google.gson.annotations.SerializedName


data class OptionsEntity(
    @SerializedName("id") val optionId: String?,
    @SerializedName("text") val option: String?
)