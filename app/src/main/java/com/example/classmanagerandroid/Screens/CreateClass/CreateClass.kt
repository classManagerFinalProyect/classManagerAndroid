package com.example.classmanagerandroid.Screens.CreateClass


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
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.Screens.CreateClass.Item.SelectedDropDownMenuCurseItem
import com.example.classmanagerandroid.Screens.ScreenItems.Dialogs.LoadingDialog
import com.example.classmanagerandroid.data.local.CurrentUser
import com.example.classmanagerandroid.Screens.ScreenItems.Inputs.BigOutlineTextFieldWithErrorMessage
import com.example.classmanagerandroid.Screens.Utils.CommonErrors
import com.example.classmanagerandroid.Screens.Utils.isValidDescription
import com.example.classmanagerandroid.Screens.Utils.isValidName
import com.example.classmanagerandroid.data.remote.Course

@Composable
fun MainCreateClass(
    navController: NavController,
    mainViewModelCreateClass: MainViewModelCreateClass
) {
    //Texts
    var textNameOfClass by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }


    var textDescription by remember { mutableStateOf("") }
    var descriptionError by remember { mutableStateOf(false) }


    val (itemSelectedCurse,onValueChangeItemSelectedCurse) = remember { mutableStateOf(
        Course(arrayListOf(), arrayListOf(), arrayListOf(),"Sin asignar","","Sin asignar","")
    ) }


    //Variables de ayuda
    val context = LocalContext.current
    val loading = remember { mutableStateOf(false) }


    if (loading.value){
        LoadingDialog(
            loading = loading,
            informativeText = "Creando nueva clase"
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Creación de clase")
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
                                    value = textNameOfClass,
                                    onValueChange = { textNameOfClass = it },
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
                                    value = textDescription,
                                    onValueChange = { textDescription = it },
                                    validateError = ::isValidDescription,
                                    errorMessage = CommonErrors.notValidDescription,
                                    changeError = { descriptionError = it},
                                    error = descriptionError,
                                    mandatory = false,
                                    KeyboardType = KeyboardType.Text,
                                    enabled = true
                                )
                            }
                            item {
                                SelectedDropDownMenuCurseItem(
                                    suggestions = CurrentUser.myCourses,
                                    onValueChangeTextSelectedItem = onValueChangeItemSelectedCurse
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
                                    if(isValidName(text = textNameOfClass) && isValidDescription(text = textDescription)) {
                                        loading.value = true

                                        mainViewModelCreateClass.createClass(
                                            textDescription = textDescription,
                                            textNameOfClass = textNameOfClass,
                                            itemSelectedCurse = itemSelectedCurse,
                                            onFinished = {
                                                Toast.makeText(context,"El curso ha sido creado correctamente", Toast.LENGTH_SHORT).show()
                                                navController.navigate(Destinations.MainAppView.route)
                                                loading.value = false
                                            }
                                        )
                                    }
                                    else
                                        Toast.makeText(context,CommonErrors.incompleteFields,Toast.LENGTH_SHORT).show()
                                },
                                content = {
                                    Text(text = "Crear clase")
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}
