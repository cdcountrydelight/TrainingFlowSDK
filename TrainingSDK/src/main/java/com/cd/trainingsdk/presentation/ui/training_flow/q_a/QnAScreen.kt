package com.cd.trainingsdk.presentation.ui.training_flow.q_a

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.cd.trainingsdk.domain.contents.OptionsContent
import com.cd.trainingsdk.domain.contents.QnaResponseContent
import com.cd.trainingsdk.domain.contents.QuestionResponseContent
import com.cd.trainingsdk.presentation.ui.beans.ButtonHandlerBean
import com.cd.trainingsdk.presentation.ui.common.ErrorAlertDialog
import com.cd.trainingsdk.presentation.ui.common.LoadingSection
import com.cd.trainingsdk.presentation.ui.common.SpacerHeight12
import com.cd.trainingsdk.presentation.ui.training_flow.TrainingFlowViewModel
import com.cd.trainingsdk.presentation.ui.utils.DataUiResponseStatus
import com.cd.trainingsdk.presentation.ui.utils.FunctionHelper.getErrorMessage

@Composable
internal fun QnAScreen(viewModel: TrainingFlowViewModel) {
    val context = LocalContext.current
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
        ) {
            val qna = mutableListOf<QnaResponseContent>()
            qna.add(
                QnaResponseContent(
                    QuestionResponseContent("1", "What is your name?"),
                    mutableListOf(
                        OptionsContent("1", "Arpit Katiyar"),
                        OptionsContent("2", "Lakshay Mudgal"),
                        OptionsContent("3", "Hello Bro"),
                        OptionsContent("4", "I dont know")
                    ),
                    false
                )
            )
            qna.add(
                QnaResponseContent(
                    QuestionResponseContent("2", "What is your age?"),
                    mutableListOf(
                        OptionsContent("1", "24"),
                        OptionsContent("2", "25"),
                        OptionsContent("3", "26"),
                        OptionsContent("4", "27")
                    ),
                    false
                )
            )
            QnASection(qna, viewModel)
//            HandleQuestionAndAnswerStateFlow(viewModel)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getQuestionsList(viewModel.selectedFlow?.id ?: 0, context)
    }
}

@Composable
private fun HandleQuestionAndAnswerStateFlow(viewModel: TrainingFlowViewModel) {

    val flowDetailsStateFlow = viewModel.qnaStateFlow.collectAsStateWithLifecycle()

    var isResponseHandled by remember { mutableStateOf(false) }
    val context = LocalContext.current

    when (val response = flowDetailsStateFlow.value) {
        is DataUiResponseStatus.Loading -> {
            isResponseHandled = false
            LoadingSection()
        }

        is DataUiResponseStatus.Success -> {
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
private fun QnASection(data: List<QnaResponseContent>, viewModel: TrainingFlowViewModel) {
    val selectedQuestion = data.getOrNull(viewModel.selectedQuestionIndex) ?: return
    val selectedOptions = remember {
        mutableStateListOf<OptionsContent>()
    }
    ProgressBarSections(viewModel.selectedQuestionIndex + 1, data.size)
    SpacerHeight12()
    Text(
        text = selectedQuestion.question.question,
        color = Color.Black,
        fontWeight = FontWeight.Medium
    )
    SpacerHeight12()
    if (selectedQuestion.isMsq) {

    } else {
        RadioButtonSingleSelection(selectedQuestion.options, selectedOptions) {
            selectedOptions.clear()
            selectedOptions.add(it)
        }
    }
}

@Composable
fun ProgressBarSections(currentQuestion: Int, questionCount: Int) {
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
fun RadioButtonSingleSelection(
    options: List<OptionsContent>,
    selectedOptionsList: List<OptionsContent>,
    onOptionSelected: (OptionsContent) -> Unit
) {
    Column(Modifier.selectableGroup()) {
        options.forEach { option ->
            OptionItem(selectedOptionsList.contains(option), option.option) {
                onOptionSelected(option)
            }
        }
    }
}

@Composable
fun OptionItem(
    isSelected: Boolean,
    optionText: String,
    onOptionSelectionChanged: () -> Unit
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
                onOptionSelectionChanged()
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(isSelected, onClick = {
            onOptionSelectionChanged()
        })
        Text(optionText, fontSize = 14.sp)
    }
}