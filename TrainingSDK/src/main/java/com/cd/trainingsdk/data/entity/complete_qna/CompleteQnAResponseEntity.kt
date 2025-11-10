package com.cd.trainingsdk.data.entity.complete_qna

import com.google.gson.annotations.SerializedName

internal data class CompleteQnAResponseEntity(@SerializedName("score") val calculatedScore: Double? = null)