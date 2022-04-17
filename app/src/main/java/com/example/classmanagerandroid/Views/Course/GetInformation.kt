package com.example.classmanagerandroid.Views.Course

import android.icu.text.CaseMap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun getInformation(
    onValueChangeGetInformation: (Boolean) -> Unit,
    mainViewModelCourse: MainViewModelCourse
) {
    val selectedCourse = remember { mainViewModelCourse.selectedCourse}

    Dialog(
        onDismissRequest = {
            onValueChangeGetInformation(false)
        },
        properties = DialogProperties(

        ),
        content = {

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                    .background(Color.White),
                shape = RoundedCornerShape(16.dp),
                content = {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        content = {
                            item {
                                Spacer(modifier = Modifier.padding(5.dp))
                                Text(
                                    text = selectedCourse.name,
                                    textAlign = TextAlign.Center,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.padding(2.dp))
                            }
                            item {
                                Text(
                                    text = "Número de miembros: ${selectedCourse.users.size}",
                                    fontSize = 15.sp
                                )
                            }
                            item {

                                Text(
                                    text = "Número de clases: ${selectedCourse.classes.size}",
                                    fontSize = 15.sp

                                )
                            }
                            item {
                                Text(
                                    text = "Descripción ${selectedCourse.description}",
                                    fontSize = 15.sp
                                )
                            }
                        }
                    )
                }
            )
        }
    )
}