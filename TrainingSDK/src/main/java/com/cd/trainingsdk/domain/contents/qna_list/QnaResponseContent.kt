package com.cd.trainingsdk.domain.contents.qna_list

internal data class QnaResponseContent(
    val id: String,
    val question: List<QuestionResponseContent> = listOf(),
    val flowId: String,
)