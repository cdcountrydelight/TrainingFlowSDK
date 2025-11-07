package com.cd.trainingsdk.data.repository

import com.cd.trainingsdk.data.entity.CompleteQnARequestEntity
import com.cd.trainingsdk.data.entity.CompleteQuestionsRequestEntity
import com.cd.trainingsdk.data.entity.FlowDetailsResponseEntity
import com.cd.trainingsdk.data.entity.FlowListResponseEntity
import com.cd.trainingsdk.data.entity.QnaResponseEntity
import com.cd.trainingsdk.data.mappers.complete_training_flow.CompleteFlowResponseMapper
import com.cd.trainingsdk.data.mappers.flow_details_mappers.FlowDetailsMapper
import com.cd.trainingsdk.data.mappers.flow_list_mappers.FlowListMapper
import com.cd.trainingsdk.data.mappers.qna.CompleteQnAResponseEntityToContentMapper
import com.cd.trainingsdk.data.mappers.qna.QnAResponseEntityToContentMapper
import com.cd.trainingsdk.data.network.NetworkCallHelper.networkCall
import com.cd.trainingsdk.domain.contents.complete_flow.CompleteFlowResponseContent
import com.cd.trainingsdk.domain.contents.complete_qna.CompleteQnAContent
import com.cd.trainingsdk.domain.contents.complete_qna.CompleteQnaResponseContent
import com.cd.trainingsdk.domain.contents.flow_details.FlowDetailsResponseContent
import com.cd.trainingsdk.domain.contents.flow_list.FlowListResponseContent
import com.cd.trainingsdk.domain.contents.qna_list.QnaResponseContent
import com.cd.trainingsdk.domain.domain_utils.AppErrorCodes
import com.cd.trainingsdk.domain.domain_utils.DataResponseStatus
import com.cd.trainingsdk.domain.repository.ITrainingFlowRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

internal class TrainingFlowRepositoryImpl(private val httpClient: HttpClient) :
    ITrainingFlowRepository {

    override suspend fun getFlowsList(packageName: String): DataResponseStatus<List<FlowListResponseContent>> {
        val flowListMapper = FlowListMapper()
        val response = networkCall<List<FlowListResponseEntity>> {
            httpClient.get {
                parameter("app_package", packageName)
            }
        }

        return when (response) {
            is DataResponseStatus.Failure -> {
                DataResponseStatus.failure(response.errorMessage, response.errorCode)

            }

            is DataResponseStatus.Success -> {
                val flows = response.data.mapNotNull { flowListMapper.mapData(it) }
                DataResponseStatus.success(flows)
            }
        }
    }

    override suspend fun getFlowDetails(flowId: Int): DataResponseStatus<FlowDetailsResponseContent> {
        val response = networkCall<FlowDetailsResponseEntity> {
            httpClient.get("$flowId/")
        }

        return when (response) {
            is DataResponseStatus.Failure -> {
                DataResponseStatus.failure(response.errorMessage, response.errorCode)
            }

            is DataResponseStatus.Success -> {
                val flowDetailsMapper = FlowDetailsMapper()
                val flowDetails = flowDetailsMapper.mapData(response.data)
                if (flowDetails == null) {
                    DataResponseStatus.failure("", AppErrorCodes.UNKNOWN_ERROR)
                } else {
                    DataResponseStatus.success(flowDetails)
                }
            }
        }
    }

    override suspend fun getQnADetails(flowId: Int): DataResponseStatus<QnaResponseContent> {
        val mapper = QnAResponseEntityToContentMapper()
        val response = networkCall<QnaResponseEntity> {
            httpClient.get("$flowId/quiz/")
        }

        return when (response) {
            is DataResponseStatus.Failure -> {
                DataResponseStatus.failure(response.errorMessage, response.errorCode)

            }

            is DataResponseStatus.Success -> {
                val mappedData = mapper.mapData(response.data)
                if (mappedData == null) {
                    DataResponseStatus.failure("", AppErrorCodes.UNKNOWN_ERROR)
                } else {
                    DataResponseStatus.success(mappedData)
                }
            }
        }
    }

    override suspend fun completeQnA(
        flowId: Int,
        completeQnAList: List<CompleteQnAContent>
    ): DataResponseStatus<CompleteQnaResponseContent> {
        return networkCall(CompleteQnAResponseEntityToContentMapper()) {
            httpClient.post("$flowId/quiz/submit/") {
                setBody(CompleteQnARequestEntity(completeQnAList.map {
                    CompleteQuestionsRequestEntity(
                        it.question,
                        it.options
                    )
                }))
            }
        }
    }

    override suspend fun completeTraining(flowId: Int): DataResponseStatus<CompleteFlowResponseContent> {
        return networkCall(CompleteFlowResponseMapper()) {
            httpClient.post("$flowId/complete/")
        }
    }
}