package com.example.classmanagerandroid.Screens.ScreenItems.Inputs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BigOutlineTextFieldWithErrorMessage(
    text: String,
    value: String,
    onValueChange: (String) -> Unit,
    validateError: (String) -> Boolean,
    errorMessage: String,
    changeError: (Boolean) -> Unit,
    error: Boolean,
    mandatory: Boolean,
    KeyboardType : KeyboardType,
    enabled : Boolean,
) {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(start = 40.dp, end = 40.dp)),
        content = {
            OutlinedTextField(
                value = value,
                enabled = enabled,
                onValueChange = {
                    onValueChange(it)
                    changeError(!validateError(it))
                },
                placeholder = { Text(text = text) },
                label = { Text(text = text) },
                singleLine = true,
                isError = error,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.primary,
                    unfocusedBorderColor = MaterialTheme.colors.onSurface,
                    textColor = MaterialTheme.colors.secondary,
                    placeholderColor = MaterialTheme.colors.secondary.copy(0.6f),
                    unfocusedLabelColor = MaterialTheme.colors.secondary.copy(0.4f),
                ),
                modifier = Modifier.fillMaxWidth()
            )

            val assistiveElementText = if (error) errorMessage else if (mandatory) "*Obligatorio" else ""
            val assistiveElementColor = if (error)
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