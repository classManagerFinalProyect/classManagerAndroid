package com.example.classmanagerandroid.Screens.ScreenItems.Inputs

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import java.util.*


@Composable
fun ShowDatePicker(
    context: Context,
    textDate: String,
    onValueChangeTextDate: (String) -> Unit,
    label: String,
    placeholder: String,
    enabled: Boolean,
    icon: ImageVector
){

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()


    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            onValueChangeTextDate("$dayOfMonth/$month/$year")
        }, year, month, day
    )

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(start = 40.dp, end = 40.dp)),
        content = {
            OutlinedTextField(
                value = textDate,
                onValueChange = {},
                placeholder = { Text(text = placeholder) },
                label = { Text(text = label) },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.primary,
                    unfocusedBorderColor = MaterialTheme.colors.onSurface,
                    textColor = MaterialTheme.colors.secondary,
                    placeholderColor = MaterialTheme.colors.secondary,
                    unfocusedLabelColor = MaterialTheme.colors.secondary,
                ),
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                trailingIcon = {
                    if (enabled) {
                        IconButton(
                            onClick = {
                                datePickerDialog.show()
                            },
                            content = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = "Fecha de entrega",
                                    tint = MaterialTheme.colors.secondary
                                )
                            }
                        )
                    }
                }

            )
        }
    )
}