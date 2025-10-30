package com.cd.trainingsdk.data.entity

data class QnaResponseEntity(
    val question: QuestionResponseEntity?,
    val options: List<OptionsEntity>?,
    val isMsq: Boolean? = false
)