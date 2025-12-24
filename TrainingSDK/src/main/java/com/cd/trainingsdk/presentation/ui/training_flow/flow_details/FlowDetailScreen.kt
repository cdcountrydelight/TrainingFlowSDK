package com.cd.trainingsdk.presentation.ui.training_flow.flow_details


import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.cd.trainingsdk.R
import com.cd.trainingsdk.domain.contents.flow_details.AnnotationItemResponseContent
import com.cd.trainingsdk.domain.contents.flow_details.StepsResponseContent
import com.cd.trainingsdk.presentation.ImageLoader
import com.cd.trainingsdk.presentation.ui.beans.ButtonHandlerBean
import com.cd.trainingsdk.presentation.ui.beans.Tuple4
import com.cd.trainingsdk.presentation.ui.common.EmptySection
import com.cd.trainingsdk.presentation.ui.common.ErrorAlertDialog
import com.cd.trainingsdk.presentation.ui.common.LoadingSection
import com.cd.trainingsdk.presentation.ui.common.SpacerHeight12
import com.cd.trainingsdk.presentation.ui.training_flow.TrainingFlowViewModel
import com.cd.trainingsdk.presentation.ui.utils.DataUiResponseStatus
import com.cd.trainingsdk.presentation.ui.utils.FunctionHelper.getErrorMessage
import com.cd.trainingsdk.presentation.ui.utils.TextToSpeechManager
import kotlinx.coroutines.delay
import kotlin.math.min

@Composable
internal fun FlowDetailScreen(
    viewModel: TrainingFlowViewModel,
    onBackClicked: () -> Unit,
    onNavigateToQnASection: () -> Unit,
    onNavigateToCompleteTrainingFlow: () -> Unit,
) {

    val steps = remember {
        viewModel.selectedFlow?.steps ?: emptyList()
    }
    var currentStepIndex by remember { mutableIntStateOf(viewModel.currentStepIndex ?: 0) }

    val currentStep = steps.getOrNull(currentStepIndex)

    val context = LocalContext.current

    BackHandler {
        viewModel.showToolTip = false
        onBackClicked()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (viewModel.selectedFlow == null || currentStep == null) {
            EmptySection(
                message = stringResource(R.string.no_steps_available),
                subtitle = stringResource(R.string.this_training_flow_doesn_t_have_any_steps_configured_yet),
                actionText = stringResource(R.string.go_back),
                onActionClick = onBackClicked
            )
        } else {
            StepDetailContent(
                viewModel = viewModel,
                step = currentStep,
                onAnnotationClick = {
                    if (currentStepIndex < steps.size - 1) {
                        currentStepIndex++
                    } else {
                        viewModel.showToolTip = false
                        viewModel.getQuestionsList(viewModel.selectedFlow?.id ?: 0, context)
                    }
                },
            )
        }
    }

    HandleQuestionAndAnswerStateFlow(
        viewModel,
        onNavigateToQnASection,
        onNavigateToCompleteTrainingFlow
    )

    LaunchedEffect(Unit) {
        viewModel.showToolTip = true
    }
}


@Composable
private fun HandleQuestionAndAnswerStateFlow(
    viewModel: TrainingFlowViewModel,
    onNavigateToQnASection: () -> Unit,
    onNavigateToCompleteTrainingFlow: () -> Unit,
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
            if (!isResponseHandled) {
                onNavigateToQnASection()
                isResponseHandled = true
            }
        }

        is DataUiResponseStatus.Failure -> {
            if (!isResponseHandled) {
                val errorMessage = remember {
                    context.getErrorMessage(
                        response.errorMessage,
                        response.errorCode
                    )
                }
                if (errorMessage.contains("quiz not found", ignoreCase = true)) {
                    onNavigateToCompleteTrainingFlow()
                    isResponseHandled = true
                } else {
                    ErrorAlertDialog(
                        errorMessage = errorMessage,
                        negativeButton = ButtonHandlerBean(
                            buttonText = stringResource(R.string.ok),
                            onButtonClicked = {
                                isResponseHandled = true
                            }
                        )
                    )
                }
            }
        }

        else -> {}
    }
}

@Composable
private fun StepDetailContent(
    viewModel: TrainingFlowViewModel,
    step: StepsResponseContent,
    onAnnotationClick: () -> Unit,
) {
    val context = LocalContext.current
    val imageRequest = remember(step.screenshotUrl) {
        ImageRequest.Builder(context)
            .data(step.screenshotUrl)
            .tag(step.screenshotUrl)
            .size(step.width.toInt(), step.height.toInt())
            .crossfade(true)
            .build()
    }

    val painter = rememberAsyncImagePainter(
        model = imageRequest,
        imageLoader = ImageLoader.getImageLoader(LocalContext.current)
    )

    when (painter.state) {
        is AsyncImagePainter.State.Loading -> {
            LoadingSection()
        }

        is AsyncImagePainter.State.Error -> {
            ErrorSection()
        }

        is AsyncImagePainter.State.Success -> {
            ScreenWithOverlays(
                viewModel = viewModel,
                painter = painter,
                step = step,
                onActionClick = onAnnotationClick
            )

        }

        else -> {}
    }
}

@Composable
private fun ErrorSection() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.failed_to_load_image),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )

    }
}

@Composable
private fun ScreenWithOverlays(
    viewModel: TrainingFlowViewModel,
    painter: Painter,
    step: StepsResponseContent,
    onActionClick: () -> Unit,
) {
    val density = LocalDensity.current
    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { layoutCoordinates ->
                containerSize = layoutCoordinates.size
            }
    ) {
        val (scaleFactor, scaledImageWidth, scaledImageHeight, imageOffsetX, imageOffsetY) =
            remember(containerSize, step.width, step.height) {
                if (containerSize != IntSize.Zero) {
                    val imageWidth = step.width
                    val imageHeight = step.height
                    val containerWidthPx = containerSize.width
                    val containerHeightPx = containerSize.height
                    val scaleX = containerWidthPx / imageWidth
                    val scaleY = containerHeightPx / imageHeight
                    val scaleFactor = min(scaleX, scaleY)
                    val scaledImageWidth = imageWidth * scaleFactor
                    val scaledImageHeight = imageHeight * scaleFactor
                    val imageOffsetX = (containerWidthPx - scaledImageWidth) / 2
                    val imageOffsetY = (containerHeightPx - scaledImageHeight) / 2
                    listOf(
                        scaleFactor,
                        scaledImageWidth,
                        scaledImageHeight,
                        imageOffsetX,
                        imageOffsetY
                    )
                } else {
                    listOf(1.0, step.width, step.height, 0.0, 0.0)
                }
            }

        if (containerSize != IntSize.Zero) {
            Image(
                painter = painter,
                contentDescription = "Training screen ${step.stepNumber}",
                modifier = Modifier
                    .size(
                        width = with(density) { scaledImageWidth.toFloat().toDp() },
                        height = with(density) { scaledImageHeight.toFloat().toDp() }
                    )
                    .offset(
                        x = with(density) { imageOffsetX.toFloat().toDp() },
                        y = with(density) { imageOffsetY.toFloat().toDp() }
                    ),
                contentScale = ContentScale.Fit
            )
            Box(
                modifier = Modifier
                    .size(
                        width = with(density) { scaledImageWidth.toFloat().toDp() },
                        height = with(density) { scaledImageHeight.toFloat().toDp() }
                    )
                    .offset(
                        x = with(density) { imageOffsetX.toFloat().toDp() },
                        y = with(density) { imageOffsetY.toFloat().toDp() }
                    )
            )
            if (!step.annotation?.annotations.isNullOrEmpty()) {
                val annotation = step.annotation.annotations.first()
                AnimatedActionOverlay(
                    viewModel = viewModel,
                    step = step,
                    annotation = annotation,
                    scaleFactor = scaleFactor,
                    imageOffsetX = imageOffsetX,
                    imageOffsetY = imageOffsetY,
                    density = density,
                    onActionClick = onActionClick,
                )
            }
        }
    }
}


@Composable
private fun AnimatedActionOverlay(
    viewModel: TrainingFlowViewModel,
    step: StepsResponseContent,
    annotation: AnnotationItemResponseContent,
    scaleFactor: Double,
    imageOffsetX: Double,
    imageOffsetY: Double,
    density: Density,
    onActionClick: () -> Unit,
) {
    val backgroundColor = Color.Transparent
    val borderDetails = getBorderDetails(annotation, scaleFactor, density)
    val shapeDetails = getShapeDetails(annotation, density, scaleFactor)
    val (xOffset, yOffset, width, height) = if (annotation.type == "circle" && annotation.coordinates.radius != null) {
        val radiusPx = annotation.coordinates.radius
        val totalRadiusPx = radiusPx + (borderDetails?.first?.value ?: 0.0f)
        val scaledRadius = totalRadiusPx * scaleFactor
        val scaledCenterX = annotation.coordinates.x * scaleFactor + imageOffsetX
        val scaledCenterY = annotation.coordinates.y * scaleFactor + imageOffsetY
        val scaledX = scaledCenterX - scaledRadius
        val scaledY = scaledCenterY - scaledRadius
        with(density) {
            Tuple4(
                scaledX.toFloat().toDp(),
                scaledY.toFloat().toDp(),
                (scaledRadius * 2).toFloat().toDp(),
                (scaledRadius * 2).toFloat().toDp()
            )
        }
    } else {
        val scaledX = annotation.coordinates.x * scaleFactor + imageOffsetX
        val scaledY = annotation.coordinates.y * scaleFactor + imageOffsetY
        val scaledWidth = (annotation.coordinates.width ?: 100.0) * scaleFactor
        val scaledHeight = (annotation.coordinates.height ?: 100.0) * scaleFactor

        with(density) {
            Tuple4(
                scaledX.toFloat().toDp(),
                scaledY.toFloat().toDp(),
                scaledWidth.toFloat().toDp(),
                scaledHeight.toFloat().toDp()
            )
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        BlurredOverlay(
            xOffset = xOffset,
            yOffset = yOffset,
            width = width,
            height = height,
            shape = if (annotation.type == "circle") CircleShape else RectangleShape,
            blurRadius = 24.dp
        )
        Box(modifier = Modifier.absoluteOffset(x = xOffset, y = yOffset)) {
            Box(
                modifier = Modifier
                    .size(width = width, height = height)
                    .then(
                        if (borderDetails != null) {
                            Modifier.border(
                                width = borderDetails.first,
                                color = borderDetails.second,
                                shape = shapeDetails
                            )
                        } else {
                            Modifier
                        }
                    )
                    .background(color = backgroundColor, shape = shapeDetails)
                    .clip(shapeDetails)
                    .clickable {
                        onActionClick()
                    }
            )

            if (step.instructions.isNotEmpty()) {
                DropdownMenu(
                    expanded = viewModel.showToolTip,
                    onDismissRequest = { viewModel.showToolTip = false },
                    offset = DpOffset(0.dp, 8.dp),
                    containerColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    properties = PopupProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = false
                    ),
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    InstructionsSection(step)
                }
            }
        }
    }
}


@Composable
private fun InstructionsSection(step: StepsResponseContent) {
    var currentTooltipIndex by remember { mutableIntStateOf(0) }
    var timerKey by remember { mutableIntStateOf(0) }
    val safeIndex = currentTooltipIndex.coerceIn(0, step.instructions.lastIndex)

    LaunchedEffect(currentTooltipIndex, timerKey, step.instructions.size) {
        if (step.instructions.size > 1) {
            delay(2500)
            currentTooltipIndex = (currentTooltipIndex + 1) % step.instructions.size
        }
    }

    LaunchedEffect(currentTooltipIndex) {
        TextToSpeechManager.speak(step.instructions[safeIndex])
    }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (step.instructions.size > 1) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .clickable {
                        if (currentTooltipIndex > 0) {
                            currentTooltipIndex--
                        } else {
                            currentTooltipIndex = step.instructions.size - 1
                        }
                        timerKey++
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous instruction",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = step.instructions[safeIndex],
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )

            if (step.instructions.size > 1) {
                SpacerHeight12()
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(step.instructions.size) { iteration ->
                        val isActive = currentTooltipIndex == iteration
                        val color = if (isActive) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                        }
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                                .size(if (isActive) 8.dp else 6.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }
            }
        }

        if (step.instructions.size > 1) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .clickable {
                        if (currentTooltipIndex < step.instructions.size - 1) {
                            currentTooltipIndex++
                        } else {
                            currentTooltipIndex = 0
                        }
                        timerKey++
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next instruction",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

internal fun getBorderDetails(
    annotation: AnnotationItemResponseContent,
    scaleFactor: Double,
    density: Density,
): Pair<Dp, Color>? {
    return try {
        val borderWidthPx = annotation.strokeWidth ?: 0f
        val borderScaledWidthPx = borderWidthPx * scaleFactor
        val widthDp = with(density) { borderScaledWidthPx.toFloat().toDp() }
        val borderColor = Color((annotation.strokeColor ?: "#00000000").toColorInt())
        Pair(widthDp, borderColor)
    } catch (_: Exception) {
        null
    }
}


internal fun getShapeDetails(
    annotation: AnnotationItemResponseContent,
    density: Density,
    scaleFactor: Double,
): Shape {
    return when (annotation.type?.lowercase()) {
        "circle" -> CircleShape
        "rounded" -> if (annotation.coordinates.radius != null) {
            val scaledRadius = annotation.coordinates.radius * scaleFactor
            with(density) { RoundedCornerShape(scaledRadius.toFloat().toDp()) }
        } else RectangleShape

        else -> RectangleShape
    }
}


@Composable
fun BlurredOverlay(
    xOffset: Dp,
    yOffset: Dp,
    width: Dp,
    height: Dp,
    shape: Shape,
    blurRadius: Dp = 20.dp,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(blurRadius)
                .background(Color.Black.copy(alpha = 0.6f))
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawIntoCanvas { canvas ->
                val paint = Paint().apply {
                    blendMode = BlendMode.Clear
                }

                val left = xOffset.toPx()
                val top = yOffset.toPx()
                val right = left + width.toPx()
                val bottom = top + height.toPx()

                if (shape == CircleShape) {
                    val radius = width.toPx() / 2f
                    val centerX = left + radius
                    val centerY = top + radius
                    canvas.drawCircle(Offset(centerX, centerY), radius, paint)
                } else {
                    canvas.drawRect(left, top, right, bottom, paint)
                }
            }
        }
    }
}



