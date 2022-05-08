package com.example.classmanagerandroid.Screens.Class.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.classmanagerandroid.Screens.Class.MainViewModelClass
import com.example.classmanagerandroid.Screens.Practice.MainViewModelPractice
import com.example.classmanagerandroid.Screens.Practice.showDatePicker
import com.example.classmanagerandroid.Screens.Register.bigTextFieldWithErrorMessage
import com.example.classmanagerandroid.Screens.Utils.isAlphanumeric
import com.example.classmanagerandroid.data.remote.Practice

@Composable
fun addNewPractice(
    onValueCloseDialog: (Boolean) -> Unit,
    mainViewModelClass: MainViewModelClass,
    navController: NavController
){
    Dialog(
        onDismissRequest = {
            onValueCloseDialog(false)
        },
        properties = DialogProperties(

        ),
        content = {
            //Texts
            var textName by remember { mutableStateOf("") }
            var nameError by remember { mutableStateOf(false) }
            val messageNameClassError by remember { mutableStateOf("El nombre debe de contener únicamente caracteres alfanuméricos") }

            var textDeliveryDate by remember{ mutableStateOf("") }
            var deliveryDateError by remember { mutableStateOf(false) }
            val messageDeliveryDateError by remember { mutableStateOf("La fecha debe seguir el siguiente formato: dd/mm/yyyy") }

            var textDescription by remember{ mutableStateOf("") }
            var descriptionError by remember { mutableStateOf(false) }
            val messageDescriptionDateError by remember { mutableStateOf("Debes usar caracteres alfanuméricos y nunca más de 30 carácteres.") }

            var textAnnotation by remember{ mutableStateOf("") }
            var annotationError by remember { mutableStateOf(false) }
            val annotationDateError by remember { mutableStateOf("La fecha debe seguir el siguiente formato: dd/mm/yyyy") }


            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .width(400.dp)
                    .background(Color.White),
                content = {
                    Spacer(modifier = Modifier.padding(10.dp))

                    bigTextFieldWithErrorMessage(
                        text = "Nombre de la práctica",
                        value = textName,
                        onValueChange = {  textName = it },
                        validateError = ::isAlphanumeric,
                        errorMessage = messageNameClassError,
                        changeError = { nameError = it },
                        error = nameError,
                        mandatory = true,
                        KeyboardType = KeyboardType.Text,
                        enabled = true
                    )

                   showDatePicker(
                       context = LocalContext.current,
                       textDate = textDeliveryDate,
                       onValueChangeTextDate = { textDeliveryDate = it },
                       label = "Fecha de entrega",
                       placeholder = "Fecha de entrega",
                       enabled = true
                   )

                    bigTextFieldWithErrorMessage(
                        text = "Descripción de la práctica",
                        value = textDescription,
                        onValueChange = {  textDescription = it },
                        validateError = ::isAlphanumeric,
                        errorMessage = messageDescriptionDateError,
                        changeError = { descriptionError = it },
                        error = descriptionError,
                        mandatory = false,
                        KeyboardType = KeyboardType.Text,
                        enabled = true
                    )

                    bigTextFieldWithErrorMessage(
                        text = "Anotación del profesor",
                        value = textAnnotation,
                        onValueChange = {  textAnnotation = it },
                        validateError = ::isAlphanumeric,
                        errorMessage = annotationDateError,
                        changeError = { annotationError = it },
                        error = annotationError,
                        mandatory = false,
                        KeyboardType = KeyboardType.Text,
                        enabled = true
                    )

                    Button(
                        contentPadding = PaddingValues(
                            start = 10.dp,
                            top = 6.dp,
                            end = 10.dp,
                            bottom = 6.dp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(PaddingValues(start = 40.dp, end = 40.dp)),
                        onClick = {

                            val newPractice = Practice(
                                deliveryDate = textDeliveryDate,
                                idOfClass = mainViewModelClass.selectedClass.id,
                                name = textName,
                                description = textDescription,
                                id = "",
                                idOfChat = "",
                                teacherAnnotation = textAnnotation
                            )
                           mainViewModelClass.createNewPractice(
                               practice = newPractice,
                               navController = navController
                           )

                        },
                        content = {
                            Text(text = "Crear práctica")
                        }
                    )
                    Spacer(modifier = Modifier.padding(10.dp))

                }
            )
        }
    )
}