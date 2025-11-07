package com.cd.trainingsdk.data.entity

import com.google.gson.annotations.SerializedName

data class CompleteQnARequestEntity(
    @SerializedName("answers")
    val answers: List<CompleteQuestionsRequestEntity>
)