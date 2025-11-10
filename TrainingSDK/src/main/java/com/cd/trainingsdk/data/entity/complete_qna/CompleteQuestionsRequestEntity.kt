package com.cd.trainingsdk.data.entity.complete_qna

import com.google.gson.annotations.SerializedName

internal data class CompleteQuestionsRequestEntity(
    @SerializedName("question_id") val question: String,
    @SerializedName("selected_option_ids") val options: List<String>
)