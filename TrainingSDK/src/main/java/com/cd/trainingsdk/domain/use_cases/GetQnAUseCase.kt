package com.cd.trainingsdk.domain.use_cases

import android.content.Context
import com.cd.trainingsdk.data.providers.getTrainingFlowRepository
import com.cd.trainingsdk.domain.contents.qna_list.QnaResponseContent
import com.cd.trainingsdk.domain.domain_utils.DataResponseStatus

internal class GetQnAUseCase {

    suspend fun invoke(
        context: Context,
        authToken: String,
        flowId: Int,
        isProdEnv: Boolean
    ): DataResponseStatus<QnaResponseContent> {
        return getTrainingFlowRepository(context, authToken, isProdEnv).getQnADetails(flowId)
    }
}