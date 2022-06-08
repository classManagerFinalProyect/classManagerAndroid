package com.example.classmanagerandroid.Screens.Class.Components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
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
import com.example.classmanagerandroid.Screens.ScreenItems.Inputs.ShowDatePicker
import com.example.classmanagerandroid.Screens.ScreenItems.Inputs.BigOutlineTextFieldWithErrorMessage
import com.example.classmanagerandroid.Screens.Utils.CommonErrors
import com.example.classmanagerandroid.Screens.Utils.isAlphanumeric
import com.example.classmanagerandroid.Screens.Utils.isValidDescription
import com.example.classmanagerandroid.Screens.Utils.isValidName
import com.example.classmanagerandroid.data.remote.Practice

@Composable
fun AddNewPractice(
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

            var textDeliveryDate by remember{ mutableStateOf("") }

            var textDescription by remember{ mutableStateOf("") }
            var descriptionError by remember { mutableStateOf(false) }

            var textAnnotation by remember{ mutableStateOf("") }
            var annotationError by remember { mutableStateOf(false) }


            //Help variables
            val context = LocalContext.current

            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .width(400.dp)
                    .background(Color.White),
                content = {
                    Spacer(modifier = Modifier.padding(10.dp))

                    BigOutlineTextFieldWithErrorMessage(
                        text = "Nombre de la práctica",
                        value = textName,
                        onValueChange = {  textName = it },
                        validateError = { isValidName(it) },
                        errorMessage = CommonErrors.notValidName,
                        changeError = { nameError = it },
                        error = nameError,
                        mandatory = true,
                        KeyboardType = KeyboardType.Text,
                        enabled = true
                    )

                   ShowDatePicker(
                       context = LocalContext.current,
                       textDate = textDeliveryDate,
                       onValueChangeTextDate = { textDeliveryDate = it },
                       label = "Fecha de entrega",
                       placeholder = "Fecha de entrega",
                       enabled = true,
                       icon = Icons.Default.DateRange
                   )

                    BigOutlineTextFieldWithErrorMessage(
                        text = "Descripción de la práctica",
                        value = textDescription,
                        onValueChange = {  textDescription = it },
                        validateError = { isValidDescription(it) },
                        errorMessage = CommonErrors.notValidDescription,
                        changeError = { descriptionError = it },
                        error = descriptionError,
                        mandatory = false,
                        KeyboardType = KeyboardType.Text,
                        enabled = true
                    )

                    BigOutlineTextFieldWithErrorMessage(
                        text = "Anotación del profesor",
                        value = textAnnotation,
                        onValueChange = {  textAnnotation = it },
                        validateError = ::isAlphanumeric,
                        errorMessage = "Solo se admiten caracteres alfanuméricos",
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
                            if(isValidName(text = textName) && isValidDescription(text = textDescription) && isAlphanumeric(text = textAnnotation)) {
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
                            }
                            else
                                Toast.makeText(context,CommonErrors.incompleteFields, Toast.LENGTH_SHORT).show()


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