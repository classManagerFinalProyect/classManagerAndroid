package com.example.classmanagerandroid.Screens.ScreenItems.Inputs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun bigTextFieldWithErrorMessage(
    value: MutableState<String>,
    KeyboardType: KeyboardType,
    enabled: Boolean,
    validateError: (String) -> Boolean,
    error: MutableState<Boolean>,
    text: String,
    mandatory: Boolean,
    errorMessage: String
) {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(start = 20.dp, end = 20.dp)),
        content = {
            TextField(
                value = value.value,
                enabled = enabled,
                onValueChange = {
                    value.value = it
                    error.value = !validateError(it)
                },
                placeholder = { Text(text = text) },
                label = { Text(text = text) },
                singleLine = true,
                isError = error.value,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.primary,
                    unfocusedBorderColor = MaterialTheme.colors.onSurface,
                    textColor = MaterialTheme.colors.secondary,
                    placeholderColor = MaterialTheme.colors.secondary,
                    unfocusedLabelColor = MaterialTheme.colors.secondary,
                ),
                modifier = Modifier.fillMaxWidth()
            )

            val assistiveElementText = if (error.value) errorMessage else if (mandatory) "*Obligatorio" else ""
            val assistiveElementColor = if (error.value)
                MaterialTheme.colors.error
            else
                MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)

            Text(
                text = assistiveElementText,
                color = assistiveElementColor,
                style = MaterialTheme.typography.caption,
            )
        }
    )
}