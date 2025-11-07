package com.cd.trainingsdk.data.entity.qna_list

import com.google.gson.annotations.SerializedName

internal data class QnaResponseEntity(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("questions")
    val question: List<QuestionResponseEntity>?,
    @SerializedName("flow_id")
    val flowId: String? = null,
)