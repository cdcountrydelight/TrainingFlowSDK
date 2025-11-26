package com.cd.trainingsdk.domain.use_cases

import android.content.Context
import com.cd.trainingsdk.data.providers.getTrainingFlowRepository
import com.cd.trainingsdk.domain.contents.complete_qna.CompleteQnAContent
import com.cd.trainingsdk.domain.contents.complete_qna.CompleteQnaResponseContent
import com.cd.trainingsdk.domain.domain_utils.DataResponseStatus

internal class CompleteQnAUseCase {

    suspend fun invoke(
        context: Context,
        authToken: String,
        flowId: Int,
        completeQnAContent: List<CompleteQnAContent>,
        isProdEnv: Boolean
    ): DataResponseStatus<CompleteQnaResponseContent> {
        return getTrainingFlowRepository(context, authToken, isProdEnv).completeQnA(
            flowId,
            completeQnAContent
        )
    }
}