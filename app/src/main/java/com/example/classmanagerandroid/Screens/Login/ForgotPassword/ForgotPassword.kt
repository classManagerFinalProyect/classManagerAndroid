package com.example.classmanagerandroid.Screens.Login.ForgotPassword


import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classmanagerandroid.R
import com.example.classmanagerandroid.Screens.ScreenItems.Inputs.BigOutlineTextFieldWithErrorMessage
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.DefaultTopBar
import com.example.classmanagerandroid.Screens.Utils.isValidEmail


@Composable
fun MainForgotPassword(
    navController: NavController,
    mainViewModelForgotPassword: MainViewModelForgotPassword
) {
    val (emailText,onValueChangeEmailText) = remember{ mutableStateOf("") }
    val (emailError,emailErrorChange) = remember { mutableStateOf(false) }
    val nameOfEmailError = remember { mutableStateOf("El email no es válido: ejemplo@ejemplo.eje") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Recuperar tu cuenta",
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
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .border(BorderStroke(1.dp, Color.LightGray)),
                content = {
                    Image(
                        painter = painterResource(id = R.drawable.logo_dani),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .height(250.dp)
                            .width(450.dp)
                            .padding(30.dp)
                    )

                    Text(
                        text = "Introduce tu correo electrónico para buscar tu cuenta",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(
                                PaddingValues(
                                    start = 40.dp,
                                    end = 40.dp,
                                    top = 10.dp,
                                    bottom = 3.dp
                                )
                            )
                    )
                    BigOutlineTextFieldWithErrorMessage(
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

                    Spacer(modifier = Modifier.padding(6.dp))
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                        content = {
                            Button(
                                content = {
                                    Text(text = "Enviar correo de verificación")
                                },
                                onClick = {
                                    isValidEmail(emailText)
                                    if(emailError || emailText == ""){
                                        Toast.makeText(context,"El email debe ser válido",Toast.LENGTH_SHORT).show()
                                    }
                                    else {
                                        mainViewModelForgotPassword.sendEmailToChangePassword(
                                            context = context,
                                            emailText = emailText
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(PaddingValues(start = 40.dp, end = 40.dp))
                            )
                            Spacer(modifier = Modifier.fillMaxSize(0.1f))
                        }
                    )
                }
            )
        }
    )


}