package com.cd.trainingsdk.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal abstract class BaseViewModel : ViewModel() {

    fun backgroundCall(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        operation: suspend () -> Unit,
    ) {
        viewModelScope.launch(dispatcher) {
            operation()
        }
    }

}