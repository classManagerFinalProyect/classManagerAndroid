package com.example.classmanagerandroid.Screens.Settings.MyAccountOptions


import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classmanagerandroid.data.local.CurrentUser
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.Screens.ScreenItems.Dialogs.ConfirmAlertDialog
import com.example.classmanagerandroid.Screens.Settings.MyAccount.changeUserValue
import com.example.classmanagerandroid.Screens.Settings.ViewModelSettings
import com.example.classmanagerandroid.Screens.Utils.CommonErrors
import com.example.classmanagerandroid.Screens.Utils.isValidEmail

@Composable
fun MainMyAccountOptions(
    navController: NavController,
    viewModelSettings: ViewModelSettings
) {
    val context = LocalContext.current
    var (deleteItem,onValueChangeDeleteItem) = remember { mutableStateOf(false)}
    var changeEmail by remember { mutableStateOf(false) }
    var newEmail = remember { mutableStateOf(CurrentUser.currentUser.email)}

    if (changeEmail) {
        changeUserValue(
            onValueChangeChangeName = { changeEmail = false },
            value = newEmail,
            label = "Escribe tu nuevo correo",
            errorMessage = CommonErrors.notValidEmail,
            validateError = { isValidEmail(it) },
            onClickSave = {
                viewModelSettings.updateEmail(
                    email = "${newEmail}",
                    onFinished = {
                        if (it)
                            Toast.makeText(context,"Se ha actualizado el gmail correctamente",Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(context,"No se ha podido actualizar el emaiL: debes haber iniciado sesión recientemente",Toast.LENGTH_SHORT).show()
                        changeEmail = false
                    }
                )
            }
        )
    }

    if (deleteItem) {
        ConfirmAlertDialog(
            title = "¿Estas seguro que desea eliminar su cuenta?",
            subtitle = "No podrás volver a recuperarla",
            onValueChangeGoBack = onValueChangeDeleteItem,
            onFinishAlertDialog = {
                if (it) {
                    viewModelSettings.deleteUser(
                        context = context,
                        onFinished = {
                            navController.navigate(Destinations.Login.route) {
                                popUpTo(0)
                            }
                        }
                    )
                }
                onValueChangeDeleteItem(false)
            } ,
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Cuenta")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        content = {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    )
                }
            )
        },
        content = {
            LazyColumn(
                content ={
                    item {
                        Row(
                            content = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start ,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .clickable {
                                            navController.navigate(Destinations.PrivacyPolicies.route)
                                        },
                                    content = {
                                        Spacer(modifier = Modifier.padding(5.dp))
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Perfil"
                                        )
                                        Spacer(modifier = Modifier.padding(3.dp))
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth(0.9f),
                                            content = {
                                                Text(text = "Politicas de privacidad")
                                            }
                                        )
                                    }
                                )
                            }
                        )
                    }
                    item {
                        Row(
                            content = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start ,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .clickable {
                                            changeEmail = true
                                        },
                                    content = {
                                        Spacer(modifier = Modifier.padding(5.dp))
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Perfil"
                                        )
                                        Spacer(modifier = Modifier.padding(3.dp))
                                        Column(
                                            modifier = Modifier.fillMaxWidth(0.9f),
                                            content = {
                                                Text(text = CurrentUser.currentUser.email)
                                            }
                                        )
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit user email",
                                        )
                                    }
                                )
                            }
                        )
                    }
                    item {
                        Row(
                            content = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start ,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .clickable {
                                            onValueChangeDeleteItem(true)

                                        },
                                    content = {
                                        Spacer(modifier = Modifier.padding(5.dp))
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Descripción del perfil"
                                        )
                                        Spacer(modifier = Modifier.padding(3.dp))
                                        Column(
                                            modifier = Modifier.fillMaxWidth(0.9f),
                                            content = {
                                                Text(
                                                    text = "Delete my acount"
                                                )
                                            }
                                        )
                                    }
                                )
                            }
                        )
                    }
                }
            )
        }
    )
}