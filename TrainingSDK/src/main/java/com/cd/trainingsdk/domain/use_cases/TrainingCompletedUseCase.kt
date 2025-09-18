package com.cd.trainingsdk.domain.use_cases

import android.content.Context
import com.cd.trainingsdk.data.providers.getTrainingFlowRepository
import com.cd.trainingsdk.domain.contents.CompleteFlowResponseContent
import com.cd.trainingsdk.domain.domain_utils.DataResponseStatus

internal class CompleteFlowUseCase {

    suspend fun invoke(
        context: Context,
        flowId: Int,
        authToken: String
    ): DataResponseStatus<CompleteFlowResponseContent> {
        return getTrainingFlowRepository(context, authToken).completeTraining(flowId)
    }
}