package com.cd.trainingsdk.presentation.ui.beans

internal data class ButtonHandlerBean (
    val buttonText: String,
    val onButtonClicked: () -> Unit)
