package com.cd.trainingsdk.domain.use_cases

import android.content.Context
import com.cd.trainingsdk.data.providers.getTrainingFlowRepository
import com.cd.trainingsdk.domain.contents.flow_details.FlowDetailsResponseContent
import com.cd.trainingsdk.domain.domain_utils.DataResponseStatus

internal class GetFlowDetailsUseCase {

    suspend fun invoke(
        context: Context,
        flowId: Int,
        authToken: String,
        isProdEnv: Boolean
    ): DataResponseStatus<FlowDetailsResponseContent> {
        return getTrainingFlowRepository(context, authToken, isProdEnv).getFlowDetails(flowId)
    }
}