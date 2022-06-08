package com.example.classmanagerandroid.Screens.CreateCourse


import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classmanagerandroid.Screens.CreateCourse.Components.AddCourse
import com.example.classmanagerandroid.Screens.ScreenItems.Inputs.BigOutlineTextFieldWithErrorMessage
import com.example.classmanagerandroid.Screens.ScreenItems.Dialogs.LoadingDialog
import com.example.classmanagerandroid.Screens.ScreenItems.DropDownMenu.BigDropDownMenuWithAction
import com.example.classmanagerandroid.Screens.Utils.CommonErrors
import com.example.classmanagerandroid.Screens.Utils.isValidDescription
import com.example.classmanagerandroid.Screens.Utils.isValidName

@Composable
fun MainCreateCourse(
    navController: NavController,
    mainViewModelCreateCourse: MainViewModelCreateCourse
) {
    //Texts
    var textNameOfCourse by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }

    var textOfDescription by remember { mutableStateOf("") }
    var descriptionError by remember { mutableStateOf(false) }


    val (addCourse,onValueChangeAddCourse) = remember { mutableStateOf(false) }


    //Help variables
    val context = LocalContext.current
    val loading = remember { mutableStateOf(false) }


    if (loading.value){
        LoadingDialog(
            loading = loading,
            informativeText = "Creando nuevo curso"
        )
    }
    if (addCourse) {
        AddCourse(
            mainViewModelCreateCourse = mainViewModelCreateCourse,
            onValueChangeAddClasses = onValueChangeAddCourse
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Creación de curso")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        content = {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    )
                }
            )
        },
        content = {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize(),
                content = {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.9f),
                        content = {
                            item {
                                Spacer(modifier = Modifier.padding(5.dp))
                            }

                            item {
                                BigOutlineTextFieldWithErrorMessage(
                                    text = "Nombre",
                                    value = textNameOfCourse,
                                    onValueChange = { textNameOfCourse = it },
                                    validateError = ::isValidName,
                                    errorMessage = CommonErrors.notValidName,
                                    changeError = { nameError = it},
                                    error = nameError,
                                    mandatory = true,
                                    KeyboardType = KeyboardType.Text,
                                    enabled = true
                                )

                            }
                            item {
                                BigOutlineTextFieldWithErrorMessage(
                                    text = "Descripción",
                                    value = textOfDescription,
                                    onValueChange = { textOfDescription = it },
                                    validateError = ::isValidDescription,
                                    errorMessage = CommonErrors.notValidDescription,
                                    changeError = { descriptionError = it},
                                    error = descriptionError,
                                    mandatory = true,
                                    KeyboardType = KeyboardType.Text,
                                    enabled = true
                                )
                            }
                            item {
                                BigDropDownMenuWithAction(
                                    initialValue = "Clases",
                                    suggestions=  mainViewModelCreateCourse.getOfListTitle(),
                                    onClick = { onValueChangeAddCourse(true) }
                                )
                            }
                        }
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp),
                        content = {
                            Button(
                                contentPadding = PaddingValues(
                                    start = 10.dp,
                                    top = 6.dp,
                                    end = 10.dp,
                                    bottom = 6.dp
                                ),
                                modifier = Modifier
                                    .fillMaxWidth(0.3f),
                                onClick = {
                                    navController.popBackStack()
                                },
                                content = {
                                    Text(text = "Cancelar")
                                }
                            )
                            Button(
                                contentPadding = PaddingValues(
                                    start = 10.dp,
                                    top = 6.dp,
                                    end = 10.dp,
                                    bottom = 6.dp
                                ),
                                modifier = Modifier
                                    .fillMaxWidth(0.4f),
                                onClick = {

                                    if(isValidName(text = textNameOfCourse) && isValidDescription(text = textOfDescription)) {
                                        loading.value = true

                                        mainViewModelCreateCourse.createCourse(
                                            textOfDescription = textOfDescription,
                                            textNameOfCourse = textNameOfCourse,
                                            onFinished = {
                                                loading.value = false
                                                Toast.makeText(context,"El curso ha sido creado correctamente", Toast.LENGTH_SHORT).show()
                                                navController.popBackStack()
                                            }
                                        )
                                    }
                                    else
                                        Toast.makeText(context,CommonErrors.incompleteFields,Toast.LENGTH_SHORT).show()

                                },
                                content = {
                                    Text(text = "Crear curso")
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}

