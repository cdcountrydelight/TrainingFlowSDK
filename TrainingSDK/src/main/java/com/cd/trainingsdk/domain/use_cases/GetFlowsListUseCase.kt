package com.cd.trainingsdk.domain.use_cases

import android.content.Context
import com.cd.trainingsdk.data.providers.getTrainingFlowRepository
import com.cd.trainingsdk.domain.contents.FlowListResponseContent
import com.cd.trainingsdk.domain.domain_utils.DataResponseStatus

internal class GetFlowsListUseCase {

    suspend fun invoke(
        context: Context,
        packageName: String,
        authToken: String
    ): DataResponseStatus<List<FlowListResponseContent>> {
        return getTrainingFlowRepository(context, authToken).getFlowsList(packageName)
    }
}