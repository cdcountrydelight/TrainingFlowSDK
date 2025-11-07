package com.cd.trainingsdk.data.entity

import com.google.gson.annotations.SerializedName

data class CompleteQuestionsRequestEntity(
    @SerializedName("question_id") val question: String,
    @SerializedName("selected_option_ids") val options: List<String>
)