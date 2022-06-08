package com.example.classmanagerandroid.Screens.Login


import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.R
import com.example.classmanagerandroid.Screens.ScreenItems.Inputs.BigPasswordInputWithErrorMessage
import com.example.classmanagerandroid.Screens.ScreenItems.Inputs.BigOutlineTextFieldWithErrorMessage
import com.example.classmanagerandroid.Screens.ScreenItems.Dialogs.LoadingDialog
import com.example.classmanagerandroid.Screens.Utils.CommonErrors
import com.example.classmanagerandroid.Screens.Utils.isValidEmail
import com.example.classmanagerandroid.Screens.Utils.isValidPassword
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.delay


@Composable
fun MainLogin(
    navController: NavController,
    mainViewModelLogin: MainViewModelLogin
) {

    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(800L)
            isRefreshing = false
        }
    }


    //Help variables
    val context = LocalContext.current
    val activity = context as Activity
    val loginWithGoogle = remember { mutableStateOf(false) }
    val loading = remember { mutableStateOf(false) }

    if (loading.value){
        LoadingDialog(
            loading = loading,
            informativeText = "Iniciando sesión"
        )
    }


    if (loginWithGoogle.value){
        SignInWithGoogle(
            activity = activity
        )
        loginWithGoogle.value = false
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = { isRefreshing = true },
        content =  {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "Login")
                        },
                    )
                },
                content = {

                    //Texts
                    val emailText = remember{ mutableStateOf("") }
                    var emailError by remember{ mutableStateOf(false) }
                    val (passwordText,onValueChangePasswordText) = remember{ mutableStateOf("") }
                    var passwordError by  remember { mutableStateOf(false) }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize(),
                        content = {

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
                                            value = emailText.value,
                                            onValueChange = { emailText.value = it },
                                            validateError = ::isValidEmail,
                                            errorMessage = CommonErrors.notValidEmail,
                                            changeError = { emailError  = it},
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
                                            onValueChangeError = { passwordError = it },
                                            errorMessage = CommonErrors.notValidPassword,
                                            validateError = ::isValidPassword,
                                            mandatory = false,
                                            keyboardType = KeyboardType.Text
                                        )
                                    }
                                    item {
                                        Button(
                                            content = {
                                                Text(text = "Iniciar sesión")
                                            },
                                            onClick = {

                                                if(emailText.value != "" || passwordText != "") {
                                                    loading.value = true
                                                    mainViewModelLogin.signIn(
                                                        email = emailText.value,
                                                        password =passwordText,
                                                        mainViewModelLogin = mainViewModelLogin,
                                                        context = context,
                                                        navController = navController,
                                                        onFinished = {
                                                            loading.value = false
                                                        }
                                                    )
                                                }
                                                else{
                                                    Toast.makeText(context,"Debes de rellenar todos los campos",Toast.LENGTH_SHORT).show()
                                                }

                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(PaddingValues(start = 40.dp, end = 40.dp))
                                        )
                                    }
                                    item {
                                        Text(
                                            text = "O bien",
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    PaddingValues(
                                                        start = 40.dp,
                                                        end = 40.dp,
                                                        top = 5.dp,
                                                        bottom = 5.dp
                                                    )
                                                )
                                        )
                                    }
                                    item {
                                        Button(
                                            content = {
                                                Text(text = "Iniciar sesión con Google")
                                            },
                                            onClick = {
                                                loginWithGoogle.value = true

                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(PaddingValues(start = 40.dp, end = 40.dp))
                                        )
                                    }
                                    item {
                                        Spacer(modifier = Modifier.padding(6.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceAround,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(PaddingValues(start = 40.dp, end = 40.dp))
                                        ) {
                                            Text(
                                                text = "He olvidado la contraseña",
                                                color = MaterialTheme.colors.primary,
                                                modifier = Modifier
                                                    .clickable {
                                                        navController.navigate(Destinations.ForgotPassword.route)
                                                    }
                                            )
                                            Text(
                                                text = "Crear usuario",
                                                color = MaterialTheme.colors.primary,
                                                modifier = Modifier
                                                    .clickable {
                                                        navController.navigate(Destinations.Register.route)
                                                    }
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    )

                },
            )

        }
    )
}

@Composable
fun SignInWithGoogle(
    activity: Activity
){
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(activity.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(activity, gso)
    googleSignInClient.signOut()

    signInGoogle(
        googleSignInClient = googleSignInClient,
        activity = activity
    )
}

private fun signInGoogle(
    googleSignInClient: GoogleSignInClient,
    activity: Activity
) {
    val signInIntent = googleSignInClient.signInIntent
    activity.startActivityForResult(signInIntent, 1)
}


