package com.cd.trainingsdk.data.entity

import com.google.gson.annotations.SerializedName

data class CompleteQnAResponseEntity(@SerializedName("score") val calculatedScore: Double? = null)