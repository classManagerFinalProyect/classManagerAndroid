package com.example.classmanagerandroid.Screens.Course.Event.Components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.classmanagerandroid.Screens.Course.Event.MainViewModelEvent
import com.example.classmanagerandroid.Screens.Course.Event.bigSelectedDropDownMenuClassItem
import com.example.classmanagerandroid.Screens.Course.Event.showTimePicker
import com.example.classmanagerandroid.Screens.Practice.showDatePicker
import com.example.classmanagerandroid.data.remote.Class

@Composable
fun createNewEvent(
    onValueChangeCreateEvent: (Boolean) -> Unit,
    mainViewModelEvent: MainViewModelEvent,
    onValueChangeIsRefreshing: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val (textDate,onValueChangeDate) = remember { mutableStateOf("") }
    val (textStartTime,onValueChangeStartTime) = remember { mutableStateOf("") }
    val (textFinalTime,onValueChangeFinalTime) = remember { mutableStateOf("") }
    val (nameOfEvent,onValueChangeNameOfEvent) = remember { mutableStateOf("") }
    val (selectedClass,onValueChangeSelectedClass) = remember { mutableStateOf(Class("","","", arrayListOf(), arrayListOf(),"","")) }

    Dialog(
        onDismissRequest = {
            onValueChangeCreateEvent(false)
        },
        properties = DialogProperties(

        ),
        content = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                    .background(Color.White),
                content = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize(),
                        content = {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxHeight(0.2f),
                                content = {
                                    TextField(
                                        value = nameOfEvent,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(PaddingValues(start = 30.dp, end = 30.dp)),
                                        label = {
                                            Text(text = "Nombre del evento")
                                        },
                                        onValueChange = {
                                            onValueChangeNameOfEvent(it)
                                        }
                                    )
                                }
                            )
                            bigSelectedDropDownMenuClassItem(
                                label = "Clase asignada",
                                suggestions = mainViewModelEvent.selectedClasses,
                                onValueChangeTextSelectedItem =  onValueChangeSelectedClass
                            )

                            showDatePicker(
                                context = context,
                                textDate = textDate,
                                onValueChangeTextDate = onValueChangeDate,
                                label = "Fecha del evento",
                                placeholder = "Fecha del evento",
                                enabled = true
                            )

                            showTimePicker(
                                context = context,
                                label = "Hora inicial",
                                placeholder = "Hora inicial del evento",
                                textTime = textStartTime,
                                onValueChangeTextTime = onValueChangeStartTime,
                            )
                            showTimePicker(
                                context = context,
                                label = "Hora Final",
                                placeholder = "Hora final del evento",
                                textTime = textFinalTime,
                                onValueChangeTextTime = onValueChangeFinalTime,
                            )

                            Spacer(modifier = Modifier.padding(3.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth(),
                                content = {
                                    TextButton(
                                        onClick = {
                                            onValueChangeCreateEvent(false)
                                        },
                                        content = {
                                            Text(text = "Cancelar")
                                        }
                                    )
                                    TextButton(
                                        onClick = {
                                            mainViewModelEvent.createNewEvent(
                                                nameOfEvent = nameOfEvent,
                                                initialTime = textStartTime,
                                                finalTime = textFinalTime,
                                                nameOfClass = selectedClass.name,
                                                date = textDate,
                                                context = context
                                            )
                                            onValueChangeCreateEvent(false)
                                            onValueChangeIsRefreshing(true)

                                            Toast.makeText(context,"El evento ha sido creado correctamente",
                                                Toast.LENGTH_SHORT).show()
                                        },
                                        content = {
                                            Text(text = "Añadir evento")
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