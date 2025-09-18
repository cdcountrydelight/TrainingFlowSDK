package com.cd.trainingsdk.presentation.ui.training_flow

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.cd.trainingsdk.R
import com.cd.trainingsdk.data.network.ConstantHelper.unAuthorizedExceptionCodes
import com.cd.trainingsdk.domain.contents.CompleteFlowResponseContent
import com.cd.trainingsdk.domain.contents.FlowDetailsResponseContent
import com.cd.trainingsdk.domain.contents.FlowListResponseContent
import com.cd.trainingsdk.domain.domain_utils.AppErrorCodes
import com.cd.trainingsdk.domain.domain_utils.DataResponseStatus
import com.cd.trainingsdk.domain.use_cases.CompleteFlowUseCase
import com.cd.trainingsdk.domain.use_cases.GetFlowDetailsUseCase
import com.cd.trainingsdk.domain.use_cases.GetFlowsListUseCase
import com.cd.trainingsdk.presentation.ImageLoader
import com.cd.trainingsdk.presentation.base.BaseViewModel
import com.cd.trainingsdk.presentation.ui.beans.ImageLoadRequest
import com.cd.trainingsdk.presentation.ui.utils.DataUiResponseStatus
import com.cd.trainingsdk.presentation.ui.utils.FunctionHelper.clearAll
import com.cd.trainingsdk.presentation.ui.utils.FunctionHelper.mapToDataUiResponseStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

internal class TrainingFlowViewModel : BaseViewModel() {

    private val _flowsListDetailStateFlow: MutableStateFlow<DataUiResponseStatus<List<FlowListResponseContent>>> =
        MutableStateFlow(DataUiResponseStatus.Companion.none())

    internal val flowsListDetailStateFlow = _flowsListDetailStateFlow.asStateFlow()

    private val _flowDetailsStateFlow: MutableStateFlow<DataUiResponseStatus<FlowDetailsResponseContent>> =
        MutableStateFlow(DataUiResponseStatus.Companion.none())

    val flowDetailsStateFlow = _flowDetailsStateFlow.asStateFlow()

    private val _trainingCompletedStateFlow: MutableStateFlow<DataUiResponseStatus<CompleteFlowResponseContent>> =
        MutableStateFlow(DataUiResponseStatus.none())

    internal val completeTrainingStateFlow = _trainingCompletedStateFlow.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    internal val isRefreshing = _isRefreshing.asStateFlow()

    private var authenticationToken: String = ""

    internal var selectedFlow: FlowDetailsResponseContent? = null

    internal var currentStepIndex: Int? by mutableStateOf(null)

    internal var showToolTip: Boolean by mutableStateOf(true)


    fun getFlowsList(context: Context, authToken: String, packageName: String) {
        authenticationToken = authToken
        if (_flowsListDetailStateFlow.value is DataUiResponseStatus.Success) return
        _flowsListDetailStateFlow.value = DataUiResponseStatus.Companion.loading()
        backgroundCall {
            _flowsListDetailStateFlow.value =
                GetFlowsListUseCase().invoke(context, packageName, authenticationToken)
                    .mapToDataUiResponseStatus()
        }
    }

    fun refreshFlowsList(context: Context, authToken: String, packageName: String) {
        authenticationToken = authToken
        _isRefreshing.value = true
        backgroundCall {
            val result = GetFlowsListUseCase().invoke(context, packageName, authenticationToken)
                .mapToDataUiResponseStatus()
            _flowsListDetailStateFlow.value = result
            _isRefreshing.value = false
        }
    }


    fun resetFlowDetailsState() {
        _flowDetailsStateFlow.value = DataUiResponseStatus.Companion.none()
    }

    fun getFlowDetails(flowId: Int, context: Context) {
        _flowDetailsStateFlow.value = DataUiResponseStatus.Companion.loading()
        backgroundCall {
            val response = GetFlowDetailsUseCase().invoke(context, flowId, authenticationToken)
            _flowDetailsStateFlow.value =
                when (response) {
                    is DataResponseStatus.Success -> {
                        if (response.data.steps.isNullOrEmpty()) {
                            DataUiResponseStatus.Companion.failure(
                                context.getString(R.string.flow_details_not_found),
                                AppErrorCodes.UNKNOWN_ERROR
                            )
                        } else {
                            ImageLoader.loadImages(context, response.data.steps.map {
                                ImageLoadRequest(
                                    it.screenshotUrl,
                                    it.height,
                                    it.width
                                )
                            })
                            withContext(Dispatchers.Main) {
                                selectedFlow = response.data
                                currentStepIndex = 0
                            }
                            DataUiResponseStatus.Companion.success(response.data)
                        }
                    }

                    is DataResponseStatus.Failure -> {
                        DataUiResponseStatus.Companion.failure(
                            response.errorMessage,
                            response.errorCode
                        )
                    }
                }
        }
    }

    fun resetCompleteTraining() {
        _trainingCompletedStateFlow.value = DataUiResponseStatus.none()
    }

    fun completeTraining(flowId: Int, context: Context) {
        _trainingCompletedStateFlow.value = DataUiResponseStatus.loading()
        backgroundCall {
            _trainingCompletedStateFlow.value =
                CompleteFlowUseCase().invoke(context, flowId, authenticationToken)
                    .mapToDataUiResponseStatus()
        }
    }


    fun setUnAuthorizedCodes(unauthorizedCodes: List<Int>) {
        unAuthorizedExceptionCodes = unauthorizedCodes
    }

    override fun onCleared() {
        super.onCleared()
        clearAll()
    }
}