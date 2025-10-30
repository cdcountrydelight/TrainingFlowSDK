package com.cd.trainingsdk.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.cd.trainingsdk.presentation.ui.training_flow.TrainingFlowViewModel
import com.cd.trainingsdk.presentation.ui.training_flow.flow_details.FlowDetailScreen
import com.cd.trainingsdk.presentation.ui.training_flow.flow_list.FlowListScreen
import com.cd.trainingsdk.presentation.ui.training_flow.q_a.QnAScreen
import com.cd.trainingsdk.presentation.ui.training_flow.training_completed.CompletedTrainingScreen

@Composable
fun TrainingFlowNavGraph(
    authToken: String,
    packageName: String,
    modifier: Modifier = Modifier,
    appName: String = packageName,
    unAuthorizedExceptionCodes: List<Int> = listOf(401),
    navController: NavHostController = rememberNavController(),
    onBackPressed: () -> Unit,
) {
    val viewModel: TrainingFlowViewModel = viewModel()
    LaunchedEffect(Unit) {
        viewModel.setUnAuthorizedCodes(unAuthorizedExceptionCodes)
    }
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = FlowListScreenDestination
    ) {

        composable<FlowListScreenDestination> {
            FlowListScreen(
                authToken = authToken,
                appName = appName,
                packageName = packageName,
                viewModel = viewModel,
                onFlowSelected = { ->
                    viewModel.resetFlowDetailsState()
                    navController.navigate(FlowDetailsScreenDestination)
                },
                onBackClicked = onBackPressed
            )
        }

        composable<FlowDetailsScreenDestination> {
            FlowDetailScreen(
                viewModel = viewModel,
                onBackClicked = {
                    navController.popBackStack()
                },
                onNavigateToQnASection = {
                    navController.navigate(QnAScreenDestination) {
                        popUpTo(navController.currentDestination?.id ?: return@navigate) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<QnAScreenDestination> {
            QnAScreen(
                viewModel = viewModel,
                onNavigateToCompleteTraining = {
                    navController.navigate(CompletedTrainingScreenDestination(it)) {
                        popUpTo(navController.currentDestination?.id ?: return@navigate) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<CompletedTrainingScreenDestination> { backStack ->
            val calculatedScore =
                backStack.toRoute<CompletedTrainingScreenDestination>().calculatedScore
            CompletedTrainingScreen(
                viewModel = viewModel,
                appName = appName,
                calculatedScore = calculatedScore,
                onGoToHome = onBackPressed,
                onStartNextFlow = {
                    navController.popBackStack(FlowListScreenDestination, false)
                }
            )
        }
    }
}