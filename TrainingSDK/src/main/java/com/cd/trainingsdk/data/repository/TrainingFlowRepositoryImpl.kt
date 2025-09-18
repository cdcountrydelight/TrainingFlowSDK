package com.cd.trainingsdk.data.repository

import com.cd.trainingsdk.data.entity.FlowDetailsResponseEntity
import com.cd.trainingsdk.data.entity.FlowListResponseEntity
import com.cd.trainingsdk.data.mappers.complete_training_flow.CompleteFlowResponseMapper
import com.cd.trainingsdk.data.mappers.flow_details_mappers.FlowDetailsMapper
import com.cd.trainingsdk.data.mappers.flow_list_mappers.FlowListMapper
import com.cd.trainingsdk.data.network.NetworkCallHelper.networkCall
import com.cd.trainingsdk.domain.contents.CompleteFlowResponseContent
import com.cd.trainingsdk.domain.contents.FlowDetailsResponseContent
import com.cd.trainingsdk.domain.contents.FlowListResponseContent
import com.cd.trainingsdk.domain.domain_utils.AppErrorCodes
import com.cd.trainingsdk.domain.repository.ITrainingFlowRepository
import com.cd.trainingsdk.domain.domain_utils.DataResponseStatus
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post

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

    override suspend fun completeTraining(flowId: Int): DataResponseStatus<CompleteFlowResponseContent> {
        return networkCall(CompleteFlowResponseMapper()) {
            httpClient.post("$flowId/complete/")
        }
    }
}