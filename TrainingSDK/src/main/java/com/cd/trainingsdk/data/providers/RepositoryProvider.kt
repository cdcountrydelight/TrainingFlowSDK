package com.cd.trainingsdk.data.providers

import android.content.Context
import com.cd.trainingsdk.data.network.HttpClientManager
import com.cd.trainingsdk.data.repository.TrainingFlowRepositoryImpl
import com.cd.trainingsdk.domain.repository.ITrainingFlowRepository


internal fun getTrainingFlowRepository(
    context: Context,
    authToken: String,
    isProdEnv: Boolean
): ITrainingFlowRepository {
    return TrainingFlowRepositoryImpl(HttpClientManager.getInstance(context, authToken, isProdEnv))
}