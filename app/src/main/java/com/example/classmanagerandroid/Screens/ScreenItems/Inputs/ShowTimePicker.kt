package com.example.classmanagerandroid.Screens.Course.Event

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun showTimePicker(
    context: Context,
    textTime: String,
    onValueChangeTextTime: (String) -> Unit,
    label: String,
    placeholder: String
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
            .padding(PaddingValues(start = 30.dp, end = 30.dp)),
        content = {
            OutlinedTextField(
                value = textTime,
                onValueChange = {},
                placeholder = { placeholder },
                label = { Text(text = "${label}") },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray
                ),
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            timePickerDialog.show()
                        },
                        content = {
                            Icon(
                                imageVector =  Icons.Default.Edit,
                                contentDescription = "Hora",
                            )
                        }
                    )
                }
            )
        }
    )
}