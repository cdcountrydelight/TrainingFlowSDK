package com.cd.trainingsdk.presentation.ui.training_flow.q_a

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cd.trainingsdk.R
import com.cd.trainingsdk.domain.contents.qna_list.OptionsContent
import com.cd.trainingsdk.domain.contents.qna_list.QnaResponseContent
import com.cd.trainingsdk.presentation.ui.beans.ButtonHandlerBean
import com.cd.trainingsdk.presentation.ui.common.ErrorAlertDialog
import com.cd.trainingsdk.presentation.ui.common.LoadingSection
import com.cd.trainingsdk.presentation.ui.common.SpacerHeight12
import com.cd.trainingsdk.presentation.ui.common.SpacerHeight16
import com.cd.trainingsdk.presentation.ui.training_flow.TrainingFlowViewModel
import com.cd.trainingsdk.presentation.ui.utils.DataUiResponseStatus
import com.cd.trainingsdk.presentation.ui.utils.FunctionHelper.getErrorMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QnAScreen(
    viewModel: TrainingFlowViewModel,
    onNavigateToCompleteTraining: (calculatedScore: Double?) -> Unit
) {
    val context = LocalContext.current
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(stringResource(R.string.quick_check))
        })
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
        ) {
            HandleQuestionAndAnswerStateFlow(viewModel,onNavigateToCompleteTraining)
            HandleQuestionAndAnswerCompleteStateFlow(viewModel, onNavigateToCompleteTraining)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getQuestionsList(viewModel.selectedFlow?.id ?: 0, context)
    }
}

@Composable
private fun HandleQuestionAndAnswerStateFlow(
    viewModel: TrainingFlowViewModel,
    onNavigateToCompleteTraining: (Double?) -> Unit
) {

    val qnaStateFlow = viewModel.qnaStateFlow.collectAsStateWithLifecycle()

    var isResponseHandled by remember { mutableStateOf(false) }
    val context = LocalContext.current

    when (val response = qnaStateFlow.value) {
        is DataUiResponseStatus.Loading -> {
            isResponseHandled = false
            LoadingSection()
        }

        is DataUiResponseStatus.Success -> {
            if (!isResponseHandled && response.data.calculatedScore != null) {
                onNavigateToCompleteTraining(response.data.calculatedScore)
                isResponseHandled = true
                return
            }
            QnASection(response.data, viewModel)
        }

        is DataUiResponseStatus.Failure -> {
            if (!isResponseHandled) {
                ErrorAlertDialog(
                    errorMessage = context.getErrorMessage(
                        response.errorMessage,
                        response.errorCode
                    ),
                    negativeButton = ButtonHandlerBean(
                        buttonText = stringResource(R.string.ok),
                        onButtonClicked = {
                            isResponseHandled = true
                        }
                    )
                )
            }
        }

        else -> {}
    }
}

@Composable
private fun HandleQuestionAndAnswerCompleteStateFlow(
    viewModel: TrainingFlowViewModel,
    onNavigateToCompleteTraining: (calculatedScore: Double?) -> Unit
) {

    val qnaCompleteStateFlow = viewModel.qnaCompleteStateFlow.collectAsStateWithLifecycle()

    var isResponseHandled by remember { mutableStateOf(false) }
    val context = LocalContext.current

    when (val response = qnaCompleteStateFlow.value) {
        is DataUiResponseStatus.Loading -> {
            isResponseHandled = false
            LoadingSection()
        }

        is DataUiResponseStatus.Success -> {
            if (!isResponseHandled) {
                onNavigateToCompleteTraining(response.data.calculatedScore)
                isResponseHandled = true
            }
        }

        is DataUiResponseStatus.Failure -> {
            if (!isResponseHandled) {
                ErrorAlertDialog(
                    errorMessage = context.getErrorMessage(
                        response.errorMessage,
                        response.errorCode
                    ),
                    negativeButton = ButtonHandlerBean(
                        buttonText = stringResource(R.string.ok),
                        onButtonClicked = {
                            isResponseHandled = true
                        }
                    )
                )
            }
        }

        else -> {}
    }
}

@Composable
private fun QnASection(data: QnaResponseContent, viewModel: TrainingFlowViewModel) {
    val selectedQuestion = data.question.getOrNull(viewModel.selectedQuestionIndex) ?: return
    val selectedOptions = remember {
        mutableStateListOf<OptionsContent>()
    }.also {
        it.addAll(selectedQuestion.selectedOptions)
    }
    val context = LocalContext.current
    ProgressBarSections(viewModel.selectedQuestionIndex + 1, data.question.size)
    SpacerHeight12()
    Text(
        text = selectedQuestion.question,
        color = Color.Black,
        fontWeight = FontWeight.Medium
    )
    SpacerHeight16()
    if (selectedQuestion.isMsq) {
        MultipleSelectionOptions(selectedQuestion.options, selectedOptions) { option, isSelected ->
            if (isSelected) {
                selectedOptions.add(option)
                selectedQuestion.selectedOptions.add(option)
            } else {
                selectedOptions.remove(option)
                selectedQuestion.selectedOptions.remove(option)
            }
        }
    } else {
        SingleSelectionOptions(selectedQuestion.options, selectedOptions) {
            selectedOptions.clear()
            selectedOptions.add(it)
            selectedQuestion.selectedOptions.clear()
            selectedQuestion.selectedOptions.add(it)
        }
    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Row(modifier = Modifier.weight(1f)) {
            if (viewModel.selectedQuestionIndex > 0) {
                TextButton(onClick = {
                    if (viewModel.selectedQuestionIndex > 0) {
                        viewModel.selectedQuestionIndex--
                    }
                }) {
                    Text(stringResource(R.string.previous))
                }
            }
        }

        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = {
                if (selectedOptions.isEmpty()) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.please_select_answer_for_this_question),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (viewModel.selectedQuestionIndex < data.question.size - 1) {
                        viewModel.selectedQuestionIndex++
                    } else {
                        viewModel.completeQnA(viewModel.selectedFlow?.id ?: 0, context, data)
                    }
                }
            }) {
                Text(stringResource(R.string.next))
            }
        }
    }
}

@Composable
private fun ProgressBarSections(currentQuestion: Int, questionCount: Int) {
    val progress = currentQuestion.toFloat() / questionCount
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = { progress.coerceIn(0f, 1f) },
            color = ProgressIndicatorDefaults.linearColor,
            trackColor = ProgressIndicatorDefaults.linearTrackColor,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            gapSize = 0.dp,
            drawStopIndicator = {}
        )
        Text(
            text = "$currentQuestion/$questionCount",
            modifier = Modifier.padding(top = 8.dp),
            fontSize = 12.sp
        )
    }
}

@Composable
private fun SingleSelectionOptions(
    options: List<OptionsContent>,
    selectedOptionsList: List<OptionsContent>,
    onOptionSelected: (OptionsContent) -> Unit
) {
    Column(Modifier.selectableGroup()) {
        options.forEach { option ->
            OptionItem(false, selectedOptionsList.contains(option), option.option) {
                onOptionSelected(option)
            }
        }
    }
}

@Composable
private fun MultipleSelectionOptions(
    options: List<OptionsContent>,
    selectedOptionsList: List<OptionsContent>,
    onOptionSelectionChanged: (OptionsContent, Boolean) -> Unit
) {
    Column(Modifier.selectableGroup()) {
        options.forEach { option ->
            OptionItem(true, selectedOptionsList.contains(option), option.option) {
                onOptionSelectionChanged(option, it)
            }
        }
    }
}

@Composable
private fun OptionItem(
    isMsq: Boolean,
    isSelected: Boolean,
    optionText: String,
    onOptionSelectionChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                1.dp, if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                RoundedCornerShape(12.dp)
            )
            .padding(2.dp)
            .clickable {
                onOptionSelectionChanged(!isSelected)
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        if (isMsq) {
            Checkbox(isSelected, onCheckedChange = {
                onOptionSelectionChanged(it)
            })
        } else {
            RadioButton(isSelected, onClick = {
                onOptionSelectionChanged(true)
            })
        }

        Text(optionText, fontSize = 14.sp)
    }
}