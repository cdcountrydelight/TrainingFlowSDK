package com.cd.trainingsdk.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.cd.trainingsdk.presentation.ui.utils.LanguageHelper
import com.cd.trainingsdk.presentation.ui.utils.withSdkLocale

@Composable
fun TrainingFlowNavGraph(
    authToken: String,
    packageName: String,
    modifier: Modifier = Modifier,
    appName: String = packageName,
    unAuthorizedExceptionCodes: List<Int> = listOf(401),
    navController: NavHostController = rememberNavController(),
    isProdEnv: Boolean,
    onBackPressed: () -> Unit,
) {
    val viewModel: TrainingFlowViewModel = viewModel()
    val baseContext = LocalContext.current

    LaunchedEffect(Unit) {
        LanguageHelper.setSelectedLanguage(baseContext)
        viewModel.setUnAuthorizedCodes(unAuthorizedExceptionCodes, isProdEnv)
    }

    val localizedContext = remember(LanguageHelper.selectedLanguage) {
        baseContext.withSdkLocale(LanguageHelper.selectedLanguage)
    }
    CompositionLocalProvider(LocalContext provides localizedContext) {
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
                    },
                    onNavigateToCompleteTrainingFlow = {
                        navController.navigate(CompletedTrainingScreenDestination(null)) {
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
                    calculatedScore = calculatedScore,
                    onGoToHome = onBackPressed,
                    onStartNextFlow = {
                        navController.popBackStack(FlowListScreenDestination, false)
                    }
                )
            }
        }
    }
}