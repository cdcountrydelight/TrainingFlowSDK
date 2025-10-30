package com.cd.trainingsdk.domain.repository

import com.cd.trainingsdk.domain.contents.CompleteFlowResponseContent
import com.cd.trainingsdk.domain.contents.FlowDetailsResponseContent
import com.cd.trainingsdk.domain.contents.FlowListResponseContent
import com.cd.trainingsdk.domain.contents.QnaResponseContent
import com.cd.trainingsdk.domain.domain_utils.DataResponseStatus

internal interface ITrainingFlowRepository {

    suspend fun getFlowsList(packageName: String): DataResponseStatus<List<FlowListResponseContent>>

    suspend fun getFlowDetails(flowId: Int): DataResponseStatus<FlowDetailsResponseContent>

    suspend fun getQnADetails(flowId: Int): DataResponseStatus<List<QnaResponseContent>>

    suspend fun completeTraining(flowId: Int): DataResponseStatus<CompleteFlowResponseContent>

}