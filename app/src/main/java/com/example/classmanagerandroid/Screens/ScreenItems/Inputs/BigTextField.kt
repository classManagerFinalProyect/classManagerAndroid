package com.example.classmanagerandroid.Screens.ScreenItems.Inputs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun BigTextField(
    text: String,
    value: String,
    onValueChange: (String) -> Unit,
    KeyboardType : KeyboardType,
    enabled: Boolean
) {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(start = 40.dp, end = 40.dp)),
        content = {
            OutlinedTextField(
                enabled = enabled,
                value = value,
                onValueChange = {
                    onValueChange(it)
                },
                placeholder = { Text(text) },
                label = { Text(text = text) },
                singleLine = false,
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
        }
    )
}