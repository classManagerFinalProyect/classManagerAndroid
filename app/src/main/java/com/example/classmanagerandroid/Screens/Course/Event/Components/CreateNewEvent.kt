package com.example.classmanagerandroid.Screens.Course.Event.Components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.classmanagerandroid.Screens.Course.Event.MainViewModelEvent
import com.example.classmanagerandroid.Screens.Course.Event.BigSelectedDropDownMenuClassItem
import com.example.classmanagerandroid.Screens.ScreenItems.Inputs.ShowTimePicker
import com.example.classmanagerandroid.Screens.ScreenItems.Inputs.ShowDatePicker
import com.example.classmanagerandroid.Screens.ScreenItems.Inputs.BigTextFieldWithErrorMessage
import com.example.classmanagerandroid.Screens.Utils.CommonErrors
import com.example.classmanagerandroid.Screens.Utils.isValidName
import com.example.classmanagerandroid.data.remote.Class

@Composable
fun CreateNewEvent(
    onValueChangeCreateEvent: (Boolean) -> Unit,
    mainViewModelEvent: MainViewModelEvent,
    loading: MutableState<Boolean>
) {
    val context = LocalContext.current
    val (textDate,onValueChangeDate) = remember { mutableStateOf("") }
    val (textStartTime,onValueChangeStartTime) = remember { mutableStateOf("") }
    val (textFinalTime,onValueChangeFinalTime) = remember { mutableStateOf("") }
    val nameOfEvent = remember { mutableStateOf("") }
    val errorOfNameEvent = remember { mutableStateOf(false) }
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
                    .background(MaterialTheme.colors.background),
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
                                    BigTextFieldWithErrorMessage(
                                        value = nameOfEvent,
                                        KeyboardType = KeyboardType.Text,
                                        enabled = true,
                                        validateError = { isValidName(it) },
                                        error = errorOfNameEvent,
                                        text = "Nombre del evento",
                                        mandatory = true,
                                        errorMessage = CommonErrors.notValidName
                                    )
                                }
                            )
                            BigSelectedDropDownMenuClassItem(
                                label = "Clase asignada",
                                suggestions = mainViewModelEvent.selectedClasses,
                                onValueChangeTextSelectedItem =  onValueChangeSelectedClass
                            )

                            ShowDatePicker(
                                context = context,
                                textDate = textDate,
                                onValueChangeTextDate = onValueChangeDate,
                                label = "Fecha del evento",
                                placeholder = "Fecha del evento",
                                enabled = true,
                                icon = Icons.Default.DateRange
                            )

                            ShowTimePicker(
                                context = context,
                                label = "Hora inicial",
                                placeholder = "Hora inicial del evento",
                                textTime = textStartTime,
                                onValueChangeTextTime = onValueChangeStartTime,
                                icon = Icons.Default.Edit,
                                enabled = true
                            )

                            ShowTimePicker(
                                context = context,
                                label = "Hora Final",
                                placeholder = "Hora final del evento",
                                textTime = textFinalTime,
                                onValueChangeTextTime = onValueChangeFinalTime,
                                icon = Icons.Default.Edit,
                                enabled = true
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
                                            if(isValidName(nameOfEvent.value)) {
                                                loading.value = true

                                                mainViewModelEvent.createNewEvent(
                                                    nameOfEvent = nameOfEvent.value,
                                                    initialTime = textStartTime,
                                                    finalTime = textFinalTime,
                                                    nameOfClass = selectedClass.name,
                                                    date = textDate,
                                                    onFinished = {
                                                        loading.value = false
                                                        Toast.makeText(context,"El evento ha sido creado con exito",Toast.LENGTH_SHORT).show()
                                                    }
                                                )
                                                onValueChangeCreateEvent(false)
                                                Toast.makeText(context,"El evento ha sido creado correctamente", Toast.LENGTH_SHORT).show()
                                            }
                                            else
                                                Toast.makeText(context,CommonErrors.incompleteFields,Toast.LENGTH_SHORT).show()
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