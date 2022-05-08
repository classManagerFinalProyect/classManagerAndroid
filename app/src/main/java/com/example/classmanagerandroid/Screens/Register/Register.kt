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
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.defaultTopBar
import com.example.classmanagerandroid.Screens.ScreenItems.Dialogs.loadingDialog
import com.example.classmanagerandroid.Screens.Utils.isValidEmail
import com.example.classmanagerandroid.Screens.Utils.isValidPassword

@Composable
fun MainRegister(
    navController: NavController,
    mainViewModelRegister: MainViewModelRegister
) {
    //Texts
    var (emailText,onValueChangeEmailText) = remember{ mutableStateOf("test@gmail.com") }
    var (emailError,emailErrorChange) = remember { mutableStateOf(false) }
    val nameOfEmailError = remember { mutableStateOf("El email no es válido: ejemplo@ejemplo.eje") }

    var (passwordText,onValueChangePasswordText) = remember{ mutableStateOf("11111111") }
    var (passwordError,passwordErrorChange) = remember { mutableStateOf(false) }
    val passwordTextErrorMessage = remember { mutableStateOf("La contraseña no puede ser inferior a 8 caracteres ni contener caracteres especiales") }

    var (repeatPasswordText,onValueChangeRepeatPasswordText) = remember{ mutableStateOf("11111111") }
    var (repeatPasswordError,repeatPasswordErrorChange) = remember { mutableStateOf(false) }
    val repeatPasswordTextErrorMesaje = remember { mutableStateOf("La contraseña no puede ser inferior a 8 caracteres ni contener caracteres especiales") }


    //Help variables
    var context = LocalContext.current
    val (checkedStatePrivacyPolicies,onValueChangeCheckedStatePrivacyPolicies) = remember { mutableStateOf(false) }

    val loading = remember { mutableStateOf(false) }

    if (loading.value){
        loadingDialog(
            loading = loading,
            informativeText = "Creando usuario"
        )
    }

    Scaffold(
        topBar = {
            defaultTopBar(
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
                            bigTextFieldWithErrorMessage(
                                text = "Email",
                                value = emailText,
                                onValueChange = onValueChangeEmailText,
                                validateError = ::isValidEmail,
                                errorMessage = nameOfEmailError.value,
                                changeError = emailErrorChange,
                                error = emailError,
                                mandatory = true,
                                KeyboardType = KeyboardType.Text,
                                enabled = true
                            )
                        }

                        item {
                            bigPasswordInputWithErrorMessage(
                                value = passwordText,
                                onValueChangeValue = onValueChangePasswordText,
                                valueError = passwordError,
                                onValueChangeError = passwordErrorChange,
                                errorMessage = passwordTextErrorMessage.value,
                                validateError = ::isValidPassword,
                                mandatory = true,
                                keyboardType = KeyboardType.Text
                            )
                        }
                        item {
                            bigPasswordInputWithErrorMessage(
                                value = repeatPasswordText,
                                onValueChangeValue = onValueChangeRepeatPasswordText,
                                valueError = repeatPasswordError,
                                onValueChangeError = repeatPasswordErrorChange,
                                errorMessage = repeatPasswordTextErrorMesaje.value,
                                validateError = ::isValidPassword,
                                mandatory = true,
                                keyboardType = KeyboardType.Text
                            )
                        }

                        item {
                            Row (
                                content = {
                                    labelledCheckbox(
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

