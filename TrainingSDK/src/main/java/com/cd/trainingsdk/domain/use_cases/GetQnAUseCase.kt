package com.cd.trainingsdk.domain.use_cases

import android.content.Context
import com.cd.trainingsdk.data.providers.getTrainingFlowRepository
import com.cd.trainingsdk.domain.contents.QnaResponseContent
import com.cd.trainingsdk.domain.domain_utils.DataResponseStatus

internal class GetQnAUseCase {

    suspend fun invoke(
        context: Context,
        authToken: String,
        flowId: Int
    ): DataResponseStatus<QnaResponseContent> {
        return getTrainingFlowRepository(context, authToken).getQnADetails(flowId)
    }
}