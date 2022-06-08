package com.example.classmanagerandroid.Screens.ScreenItems.Dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun LoadingDialog(
    loading : MutableState<Boolean>,
    informativeText: String
) {

    if (loading.value) {
        Dialog(
            onDismissRequest = { loading.value = false },
            content = {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = {
                        CircularProgressIndicator(
                            color = Color(0xfffcffff),
                            modifier = Modifier
                            .height(30.dp)
                            .width(30.dp))
                        Text(
                            text = informativeText,
                            fontSize = 16.sp,
                            color = Color(0xfffcffff)
                        )
                    }
                )
            }
        )
    }
}