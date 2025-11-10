package com.cd.trainingsdk.domain.repository

import com.cd.trainingsdk.domain.contents.complete_flow.CompleteFlowResponseContent
import com.cd.trainingsdk.domain.contents.complete_qna.CompleteQnAContent
import com.cd.trainingsdk.domain.contents.complete_qna.CompleteQnaResponseContent
import com.cd.trainingsdk.domain.contents.flow_details.FlowDetailsResponseContent
import com.cd.trainingsdk.domain.contents.flow_list.FlowListResponseContent
import com.cd.trainingsdk.domain.contents.qna_list.QnaResponseContent
import com.cd.trainingsdk.domain.domain_utils.DataResponseStatus

internal interface ITrainingFlowRepository {

    suspend fun getFlowsList(packageName: String): DataResponseStatus<List<FlowListResponseContent>>

    suspend fun getFlowDetails(flowId: Int): DataResponseStatus<FlowDetailsResponseContent>

    suspend fun getQnADetails(flowId: Int): DataResponseStatus<QnaResponseContent>

    suspend fun completeQnA(
        flowId: Int,
        completeQnAList: List<CompleteQnAContent>
    ): DataResponseStatus<CompleteQnaResponseContent>

    suspend fun completeTraining(flowId: Int): DataResponseStatus<CompleteFlowResponseContent>

}