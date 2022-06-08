package com.example.classmanagerandroid.Screens.ScreenItems.Dialogs


import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable

@Composable
fun ConfirmAlertDialog(
    title: String,
    subtitle: String,
    onValueChangeGoBack: (Boolean) -> Unit,
    onFinishAlertDialog: (correct:Boolean) -> Unit
) {
    MaterialTheme {

        Column {
            AlertDialog(
                onDismissRequest = {
                },
                title = {
                    Text(text = title)
                },
                text = {
                    Text(subtitle)
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onFinishAlertDialog(true)
                        },
                    ) {
                        Text("Aceptar")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            onFinishAlertDialog(false)
                            onValueChangeGoBack(false)
                        },
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}