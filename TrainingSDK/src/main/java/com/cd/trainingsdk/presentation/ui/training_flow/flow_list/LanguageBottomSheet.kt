package com.cd.trainingsdk.presentation.ui.training_flow.flow_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cd.trainingsdk.R
import com.cd.trainingsdk.domain.domain_utils.SharedPreferenceHelper
import com.cd.trainingsdk.presentation.ui.beans.LanguageBean
import com.cd.trainingsdk.presentation.ui.common.SpacerHeight16
import com.cd.trainingsdk.presentation.ui.utils.FunctionHelper
import com.cd.trainingsdk.presentation.ui.utils.LanguageHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LanguageBottomSheet(
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), Arrangement.Center) {
                Text(
                    stringResource(R.string.select_language_for_training),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            SpacerHeight16()
            LanguagesList(onLanguageSelected)
        }
    }
}

@Composable
private fun LanguagesList(onLanguageSelected: (String) -> Unit) {
    val allLanguagesList = remember {
        FunctionHelper.getAllAvailableLanguages()
    }
    val context = LocalContext.current
    val sharedPreferenceHelper = remember {
        SharedPreferenceHelper.getSharedPreference(context)
    }
    LazyColumn(Modifier.fillMaxWidth()) {
        items(allLanguagesList, key = { it.code }) {
            LanguageItem(it, sharedPreferenceHelper.selectedLanguageCode == it.code) {
                sharedPreferenceHelper.selectedLanguageCode = it.code
                sharedPreferenceHelper.isLanguageSet = true
                LanguageHelper.selectedLanguage = it.code
                onLanguageSelected(it.code)
            }
        }
    }
}


@Composable
private fun LanguageItem(
    language: LanguageBean,
    isLanguageSelected: Boolean,
    onLanguageSelected: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onLanguageSelected()
                }
                .padding(vertical = 4.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = language.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            RadioButton(isLanguageSelected, onClick = { onLanguageSelected() })
        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.primary,
            thickness = 0.3.dp
        )
    }
}