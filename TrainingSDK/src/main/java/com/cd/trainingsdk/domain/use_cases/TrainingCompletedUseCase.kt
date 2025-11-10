package com.cd.trainingsdk.domain.use_cases

import android.content.Context
import com.cd.trainingsdk.data.providers.getTrainingFlowRepository
import com.cd.trainingsdk.domain.contents.complete_flow.CompleteFlowResponseContent
import com.cd.trainingsdk.domain.domain_utils.DataResponseStatus

internal class TrainingCompletedUseCase {

    suspend fun invoke(
        context: Context,
        flowId: Int,
        authToken: String
    ): DataResponseStatus<CompleteFlowResponseContent> {
        return getTrainingFlowRepository(context, authToken).completeTraining(flowId)
    }
}