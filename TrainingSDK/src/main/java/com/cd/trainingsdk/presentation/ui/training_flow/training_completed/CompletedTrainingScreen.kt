@file:Suppress("AssignedValueIsNeverRead")

package com.cd.trainingsdk.presentation.ui.training_flow.training_completed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cd.trainingsdk.R
import com.cd.trainingsdk.presentation.ui.beans.ButtonHandlerBean
import com.cd.trainingsdk.presentation.ui.common.ErrorAlertDialog
import com.cd.trainingsdk.presentation.ui.common.LoadingSection
import com.cd.trainingsdk.presentation.ui.common.SpacerHeight12
import com.cd.trainingsdk.presentation.ui.common.SpacerHeight16
import com.cd.trainingsdk.presentation.ui.common.SpacerWidth16
import com.cd.trainingsdk.presentation.ui.common.SpacerWidth8
import com.cd.trainingsdk.presentation.ui.training_flow.TrainingFlowViewModel
import com.cd.trainingsdk.presentation.ui.utils.DataUiResponseStatus
import com.cd.trainingsdk.presentation.ui.utils.FunctionHelper.getErrorMessage
import kotlinx.coroutines.delay
import kotlin.math.sin


@Composable
internal fun CompletedTrainingScreen(
    viewModel: TrainingFlowViewModel,
    calculatedScore: Double?,
    onGoToHome: () -> Unit,
    onStartNextFlow: () -> Unit
) {
    val context = LocalContext.current
    Scaffold {
        Box(Modifier.padding(it)) {
            HandleTrainingCompletedStateFlow(
                viewModel,
                onGoToHome,
                calculatedScore,
                onStartNextFlow
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.completeTraining(viewModel.selectedFlow?.id ?: 0, context)
    }
}


@Composable
private fun HandleTrainingCompletedStateFlow(
    viewModel: TrainingFlowViewModel,
    onGoToHome: () -> Unit,
    calculatedScore: Double?,
    onStartNextFlow: () -> Unit,
) {
    val flowDetailsStateFlow = viewModel.completeTrainingStateFlow.collectAsStateWithLifecycle()

    var isResponseHandled by remember { mutableStateOf(false) }
    val context = LocalContext.current

    when (val response = flowDetailsStateFlow.value) {
        is DataUiResponseStatus.Loading -> {
            isResponseHandled = false
            LoadingSection()
        }

        is DataUiResponseStatus.Success -> {
            TrainingCompletedSection(
                viewModel,
                response.data.flowName,
                calculatedScore,
                onGoToHome,
                onStartNextFlow
            )
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
private fun TrainingCompletedSection(
    viewModel: TrainingFlowViewModel,
    flowName: String?,
    calculatedScore: Double?,
    onGoToHome: () -> Unit,
    onStartNextFlow: () -> Unit
) {
    var showContent by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "background")

    val animatedRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    LaunchedEffect(Unit) {
        delay(100)
        showContent = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Animated gradient background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .rotate(animatedRotation)
                .scale(1.5f)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.03f),
                            Color.Transparent
                        ),
                        radius = 800f
                    )
                )
        )

        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn(animationSpec = tween(600)) + scaleIn(
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                SpacerHeight12()
                HeaderSection(calculatedScore)
                CongratulationsCard(flowName)
                TrainingSummaryCard(calculatedScore, flowName)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            viewModel.resetCompleteTraining()
                            onGoToHome()
                        },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = stringResource(R.string.go_to_home),
                        )
                    }
                    SpacerWidth16()
                    Button(
                        onClick = {
                            viewModel.resetCompleteTraining()
                            onStartNextFlow()
                        },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = stringResource(R.string.start_next_flow),
                        )
                    }
                }
            }
        }

        // Confetti animation
        if (showContent && calculatedScore != null && calculatedScore >= 70) {
            ConfettiAnimation()
        }
    }
}

@Composable
private fun HeaderSection(calculatedScore: Double?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedScoreDisplay(calculatedScore)
        SpacerHeight16()
        calculatedScore?.let {
            GradeIndicator(it)
        }
    }
}

@Composable
private fun AnimatedScoreDisplay(calculatedScore: Double?) {
    val size = 100.dp
    val strokeWidth = 8.dp
    val fontSize = 34.sp

    if (calculatedScore == null) {
        // Show check icon for null score
        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.Center
        ) {
            // Background circle
            val primaryColor = MaterialTheme.colorScheme.primary
            Canvas(
                modifier = Modifier.size(size)
            ) {
                drawCircle(
                    color = primaryColor.copy(alpha = 0.1f),
                    radius = this.size.minDimension / 2
                )
                drawCircle(
                    color = primaryColor,
                    radius = (this.size.minDimension * 0.9f) / 2,
                    style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                )
            }

            // Check icon - draw manually
            Canvas(
                modifier = Modifier.size(size * 0.5f)
            ) {
                val checkPath = androidx.compose.ui.graphics.Path().apply {
                    moveTo(this@Canvas.size.width * 0.2f, this@Canvas.size.height * 0.5f)
                    lineTo(this@Canvas.size.width * 0.4f, this@Canvas.size.height * 0.7f)
                    lineTo(this@Canvas.size.width * 0.8f, this@Canvas.size.height * 0.3f)
                }
                drawPath(
                    path = checkPath,
                    color = primaryColor,
                    style = Stroke(
                        width = 3.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = androidx.compose.ui.graphics.StrokeJoin.Round
                    )
                )
            }
        }
    } else {
        // Show animated score
        var animatedScore by remember { mutableFloatStateOf(0f) }
        val targetScore = calculatedScore.toFloat()
        val animatedProgress by animateFloatAsState(
            targetValue = if (animatedScore > 0) targetScore / 100f else 0f,
            animationSpec = tween(1500, easing = FastOutSlowInEasing),
            label = "progress"
        )

        LaunchedEffect(calculatedScore) {
            val animationDuration = 1500L
            val steps = 30
            val stepDuration = animationDuration / steps

            for (i in 1..steps) {
                delay(stepDuration)
                animatedScore = (targetScore * i) / steps
            }
        }

        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.Center,
        ) {
            // Background circle with gradient
            Canvas(
                modifier = Modifier.size(size)
            ) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    ),
                    radius = this.size.minDimension / 2
                )
            }

            // Progress ring
            Canvas(
                modifier = Modifier.size(size * 0.9f)
            ) {
                val strokeWidthPx = strokeWidth.toPx()
                val radius = (this.size.minDimension - strokeWidthPx) / 2
                val startAngle = -90f

                // Background ring
                drawCircle(
                    color = Color.Gray.copy(alpha = 0.1f),
                    radius = radius,
                    style = Stroke(strokeWidthPx, cap = StrokeCap.Round)
                )

                // Progress ring
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color(0xFF4CAF50),
                            Color(0xFF8BC34A),
                            Color(0xFF4CAF50)
                        )
                    ),
                    startAngle = startAngle,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    style = Stroke(strokeWidthPx, cap = StrokeCap.Round)
                )
            }

            // Score text
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${animatedScore.toInt()}",
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun GradeIndicator(score: Double) {
    val (gradeText, gradeColor) = when {
        score >= 90 -> Pair(
            stringResource(R.string.excellent),
            Color(0xFF4CAF50)
        )

        score >= 80 -> Pair(
            stringResource(R.string.great_job),
            Color(0xFF8BC34A)
        )

        score >= 70 -> Pair(
            stringResource(R.string.good_work),
            Color(0xFFFFC107)
        )

        else -> Pair(
            stringResource(R.string.completed_exclamatory),
            MaterialTheme.colorScheme.primary
        )
    }


    Box(
        modifier = Modifier
            .background(
                color = gradeColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Text(
            text = gradeText,
            color = gradeColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun CongratulationsCard(flowName: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.congratulations),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.you_have_successfully_completed_training_for))
                withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                    append(flowName ?: stringResource(R.string.untitled_flow))
                }
                append(stringResource(R.string.you_can_revisit_this_training_anytime_from_the_flow_list))
            },
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun TrainingSummaryCard(score: Double?, flowName: String?) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                0.8.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        SummaryRow(
            label = "Training",
            value = flowName ?: "-"
        )
        SummaryRow(
            label = "Score",
            value = score?.let { "${it.toInt()}%" } ?: "Completed"
        )
        SummaryRow(
            label = "Status",
            value = stringResource(R.string.completed)
        )
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
        SpacerWidth8()
        Text(
            value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ConfettiAnimation() {
    // Create 4 waves of particles
    val particleWaves = remember {
        List(5) { waveIndex ->
            List(40) {
                ConfettiParticle(
                    x = kotlin.random.Random.nextFloat(),
                    y = -0.1f - (waveIndex * 0.4f), // Stagger the 4 waves
                    color = listOf(
                        Color(0xFFE91E63),
                        Color(0xFF9C27B0),
                        Color(0xFF2196F3),
                        Color(0xFF4CAF50),
                        Color(0xFFFFC107),
                        Color(0xFFFF5722)
                    ).random(),
                    size = kotlin.random.Random.nextInt(8, 17).toFloat(),
                    velocity = kotlin.random.Random.nextFloat() * 0.8f + 0.4f,
                    angle = kotlin.random.Random.nextInt(-30, 31).toFloat(),
                    cycleCount = 0 // Track how many times particle has fallen
                )
            }
        }.flatten()
    }

    val animatedParticles = remember { mutableStateOf(particleWaves) }
    var isAnimationActive by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (isAnimationActive) {
            delay(16) // 60 FPS
            animatedParticles.value = animatedParticles.value.map { particle ->
                val newY = particle.y + particle.velocity * 0.018f
                val newX = particle.x + sin(particle.angle * Math.PI / 180).toFloat() * 0.01f

                // Check if particle has gone off screen
                if (newY > 1.2f) {
                    // Stop animation if particle has completed 1 cycle (4 waves = 1 full cycle)
                    if (particle.cycleCount >= 0) {
                        particle.copy(y = 2.0f) // Move off screen permanently
                    } else {
                        particle.copy(y = newY, x = newX)
                    }
                } else {
                    particle.copy(y = newY, x = newX)
                }
            }

            // Check if all particles are off screen
            if (animatedParticles.value.all { it.y > 1.2f }) {
                isAnimationActive = false
            }
        }
    }

    if (isAnimationActive) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            animatedParticles.value.forEach { particle ->
                if (particle.y >= -0.1f && particle.y < 1.2f) {
                    drawConfetti(particle)
                }
            }
        }
    }
}

private fun DrawScope.drawConfetti(particle: ConfettiParticle) {
    val x = particle.x * size.width
    val y = particle.y * size.height

    rotate(particle.angle, pivot = Offset(x, y)) {
        drawRect(
            color = particle.color,
            topLeft = Offset(x - particle.size / 2, y - particle.size / 2),
            size = Size(particle.size, particle.size * 2),
            alpha = (1f - particle.y).coerceIn(0f, 1f)
        )
    }
}

private data class ConfettiParticle(
    val x: Float,
    val y: Float,
    val color: Color,
    val size: Float,
    val velocity: Float,
    val angle: Float,
    val cycleCount: Int = 0
)