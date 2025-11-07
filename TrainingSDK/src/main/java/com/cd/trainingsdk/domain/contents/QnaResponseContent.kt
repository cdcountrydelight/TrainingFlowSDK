package com.cd.trainingsdk.domain.contents

data class QnaResponseContent(
    val id: String,
    val question: List<QuestionResponseContent> = listOf(),
    val flowId: String,
)