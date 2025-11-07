package com.cd.trainingsdk.data.entity

import com.google.gson.annotations.SerializedName

data class QuestionResponseEntity(
    @SerializedName("id") val questionId: String?,
    @SerializedName("text") val question: String?,
    @SerializedName("options") val options: List<OptionsEntity>?,
    @SerializedName("is_multiple_choice") val isMsq: Boolean? = false,
    @SerializedName("required") val isRequired: Boolean? = true
)