package com.example.classmanagerandroid.Screens.Register


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.R
import com.example.classmanagerandroid.Screens.Register.Items.LabelledCheckbox
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.DefaultTopBar
import com.example.classmanagerandroid.Screens.ScreenItems.Dialogs.LoadingDialog
import com.example.classmanagerandroid.Screens.ScreenItems.Inputs.BigOutlineTextFieldWithErrorMessage
import com.example.classmanagerandroid.Screens.ScreenItems.Inputs.BigPasswordInputWithErrorMessage
import com.example.classmanagerandroid.Screens.Utils.CommonErrors
import com.example.classmanagerandroid.Screens.Utils.isValidEmail
import com.example.classmanagerandroid.Screens.Utils.isValidPassword

@Composable
fun MainRegister(
    navController: NavController,
    mainViewModelRegister: MainViewModelRegister
) {
    //Texts
    val (emailText,onValueChangeEmailText) = remember{ mutableStateOf("") }
    val (emailError,emailErrorChange) = remember { mutableStateOf(false) }

    val (passwordText,onValueChangePasswordText) = remember{ mutableStateOf("") }
    val (passwordError,passwordErrorChange) = remember { mutableStateOf(false) }

    val (repeatPasswordText,onValueChangeRepeatPasswordText) = remember{ mutableStateOf("") }
    val (repeatPasswordError,repeatPasswordErrorChange) = remember { mutableStateOf(false) }


    //Help variables
    val context = LocalContext.current
    val (checkedStatePrivacyPolicies,onValueChangeCheckedStatePrivacyPolicies) = remember { mutableStateOf(true) }

    val loading = remember { mutableStateOf(false) }

    if (loading.value){
        LoadingDialog(
            loading = loading,
            informativeText = "Creando usuario"
        )
    }

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Crear cuenta",
                navigationContent = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Go back",
                                tint = Color.White,
                            )
                        }
                    )
                },
                actionsContent = {}
            )
        },
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    content = {
                        item {
                            Image(
                                painter = painterResource(id = R.drawable.logo_dani),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .height(250.dp)
                                    .width(450.dp)
                                    .padding(30.dp)
                            )
                        }
                        item {
                            BigOutlineTextFieldWithErrorMessage(
                                text = "Email",
                                value = emailText,
                                onValueChange = onValueChangeEmailText,
                                validateError = ::isValidEmail,
                                errorMessage = CommonErrors.notValidEmail,
                                changeError = emailErrorChange,
                                error = emailError,
                                mandatory = true,
                                KeyboardType = KeyboardType.Text,
                                enabled = true
                            )
                        }

                        item {
                            BigPasswordInputWithErrorMessage(
                                value = passwordText,
                                onValueChangeValue = onValueChangePasswordText,
                                valueError = passwordError,
                                onValueChangeError = passwordErrorChange,
                                errorMessage = CommonErrors.notValidPassword,
                                validateError = ::isValidPassword,
                                mandatory = true,
                                keyboardType = KeyboardType.Text
                            )
                        }
                        item {
                            BigPasswordInputWithErrorMessage(
                                value = repeatPasswordText,
                                onValueChangeValue = onValueChangeRepeatPasswordText,
                                valueError = repeatPasswordError,
                                onValueChangeError = repeatPasswordErrorChange,
                                errorMessage = CommonErrors.notValidPassword,
                                validateError = ::isValidPassword,
                                mandatory = true,
                                keyboardType = KeyboardType.Text
                            )
                        }

                        item {
                            Row (
                                content = {
                                    LabelledCheckbox(
                                        labelText = "Politicas de Privacidad",
                                        isCheckedValue = checkedStatePrivacyPolicies,
                                        onValueChangeChecked = onValueChangeCheckedStatePrivacyPolicies,
                                        onClickText = {navController.navigate(Destinations.PrivacyPolicies.route)}
                                    )
                                }
                            )

                        }
                        item {
                            Button(
                                content = {
                                    Text(text = "Registrarse")
                                },
                                onClick = {
                                    if (repeatPasswordText == passwordText) {
                                        if (
                                            mainViewModelRegister.checkAllValidations(
                                                textEmail = emailText,
                                                textPassword = passwordText,
                                                checkedStatePrivacyPolicies = checkedStatePrivacyPolicies
                                            )
                                        ) {
                                            loading.value = true
                                            mainViewModelRegister.createUserWithEmailAndPassword(
                                                email = emailText,
                                                password = passwordText,
                                                context = context,
                                                navController = navController,
                                                onFinished = {
                                                    if(!it) {
                                                        Toast.makeText(context,"No se ha podido crear el usuario, pruebe con otra cuenta",Toast.LENGTH_SHORT).show()
                                                    }
                                                    loading.value = false
                                                }
                                            )
                                        }
                                        else {
                                            Toast.makeText(context,"Debes rellenar todos los campos correctamente",Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    else {
                                        Toast.makeText(context,"Las claves deben ser iguales",Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(PaddingValues(start = 40.dp, end = 40.dp))
                            )
                        }
                    }
                )
            }
        },
    )
}

