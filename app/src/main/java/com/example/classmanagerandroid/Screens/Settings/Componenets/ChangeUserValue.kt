package com.example.classmanagerandroid.Screens.Settings.MyAccount

import android.os.Message
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.classmanagerandroid.Screens.ScreenItems.Inputs.bigTextFieldWithErrorMessage
import com.example.classmanagerandroid.Screens.Utils.CommonErrors
import com.example.classmanagerandroid.Screens.Utils.isValidName

@Composable
fun changeUserValue(
    onValueChangeChangeName: (Boolean) -> Unit,
    value: MutableState<String>,
    label: String,
    onClickSave: () -> Unit,
    validateError: (String) -> Boolean,
    errorMessage: String
) {
    var errorValue = remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = {
            onValueChangeChangeName(false)
        },
        properties = DialogProperties(

        ),
        content = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .background(MaterialTheme.colors.background),
                content = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize(),
                        content = {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxHeight(0.7f),
                                content = {
                                    bigTextFieldWithErrorMessage(
                                        enabled = true,
                                        text = "${label}",
                                        error = errorValue,
                                        errorMessage = errorMessage,
                                        KeyboardType = KeyboardType.Text,
                                        mandatory = false,
                                        validateError = validateError,
                                        value = value
                                    )
                               }
                            )



                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth(),
                                content = {
                                    TextButton(
                                        onClick = {
                                            onValueChangeChangeName(false)
                                        },
                                        content = {
                                            Text(text = "Cancelar")
                                        }
                                    )
                                    TextButton(
                                        onClick = {
                                            onClickSave()

                                        },
                                        content = {
                                            Text(text = "Save")
                                        }
                                    )
                                }
                            )

                        }
                    )
                }
            )
        }
    )
}

