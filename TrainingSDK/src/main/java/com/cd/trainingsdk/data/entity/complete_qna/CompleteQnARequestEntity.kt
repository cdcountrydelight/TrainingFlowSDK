package com.cd.trainingsdk.data.entity.complete_qna

import com.google.gson.annotations.SerializedName

internal data class CompleteQnARequestEntity(
    @SerializedName("answers")
    val answers: List<CompleteQuestionsRequestEntity>
)