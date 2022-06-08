package com.example.classmanagerandroid.Screens.ScreenItems.Inputs

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun ShowTimePicker(
    context: Context,
    textTime: String,
    onValueChangeTextTime: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: ImageVector,
    enabled: Boolean
){

    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    val time = remember { mutableStateOf("") }
    val timePickerDialog = TimePickerDialog(
        context,
        {_, hour : Int, minute: Int ->
            onValueChangeTextTime("$hour:$minute")
        }, hour, minute, false
    )

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(start = 40.dp, end = 40.dp)),
        content = {
            OutlinedTextField(
                value = textTime,
                onValueChange = {},
                placeholder = { Text( text = placeholder) },
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
                    if(enabled) {
                        IconButton(
                            onClick = {
                                timePickerDialog.show()
                            },
                            content = {
                                Icon(
                                    imageVector =  icon,
                                    contentDescription = "Hora",
                                    tint =  MaterialTheme.colors.secondary
                                )
                            }
                        )
                    }

                }
            )
        }
    )
}