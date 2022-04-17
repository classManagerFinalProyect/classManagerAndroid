package com.example.classmanagerandroid.Views.Course.Event

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.data.remote.Class
import com.example.classmanagerandroid.data.remote.Event


@Composable
fun modifierEvent(
    onValueChangeModifierEvent: (Boolean) -> Unit,
    mainViewModelEvent: MainViewModelEvent,
    navController: NavController,
    onValueChangeSelectedEvent: (Event) -> Unit,
    event: Event,
    onValueChangeIsRefreshing: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val (textDate,onValueChangeDate) = remember { mutableStateOf(event.date) }
    val (textStartTime,onValueChangeStartTime) = remember { mutableStateOf(event.initialTime) }
    val (textFinalTime,onValueChangeFinalTime) = remember { mutableStateOf(event.finalTime) }
    val (nameOfEvent,onValueChangeNameOfEvent) = remember { mutableStateOf(event.name) }
    val (selectedClass,onValueChangeSelectedClass) = remember { mutableStateOf(Class("","","", arrayListOf(), arrayListOf(),"")) }
    val nameOfClass = remember { mutableStateOf(event.nameOfClass)}

    Dialog(
        onDismissRequest = {
            onValueChangeModifierEvent(false)
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

                            OutlinedTextField(
                                value = nameOfClass.value,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(PaddingValues(start = 30.dp, end = 30.dp)),
                                label = {
                                    Text(text = "Clase asignada")
                                },
                                enabled = false,
                                onValueChange = {
                                    nameOfClass.value = it
                                }
                            )

                            showDatePicker(
                                context = context,
                                textDate = textDate,
                                onValueChangeTextDate = onValueChangeDate,
                                label = "Fecha del evento",
                                placeholder = "Fecha del evento"
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
                                            onValueChangeModifierEvent(false)
                                            mainViewModelEvent.deleteEvent(idOfEvent = event.id)
                                        },
                                        content = {
                                            Text(text = "Eliminar")
                                        }
                                    )
                                    TextButton(
                                        onClick = {
                                            val updateEvent = Event(
                                                id = event.id,
                                                idOfCourse = event.idOfCourse,
                                                name = nameOfEvent,
                                                nameOfClass = nameOfClass.value,
                                                finalTime = textFinalTime,
                                                date = textDate,
                                                initialTime = textStartTime
                                            )
                                            mainViewModelEvent.selectedEvents.remove(event)
                                            mainViewModelEvent.selectedEvents.add(updateEvent)
                                            mainViewModelEvent.updateEvents(
                                               event = updateEvent,
                                               idOfEvent = event.id,
                                               context = context
                                           )
                                            onValueChangeModifierEvent(false)
                                            onValueChangeSelectedEvent(updateEvent)
                                          //  onValueChangeIsRefreshing(true)
                                        },
                                        content = {
                                            Text(text = "Actualizar")
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