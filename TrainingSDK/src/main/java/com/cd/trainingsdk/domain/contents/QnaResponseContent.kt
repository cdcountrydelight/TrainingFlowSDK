package com.cd.trainingsdk.domain.contents

data class QnaResponseContent(
    val question: QuestionResponseContent,
    val options: List<OptionsContent>,
    val isMsq: Boolean = false,
    val selectedAnswers: MutableList<OptionsContent> = mutableListOf()
)