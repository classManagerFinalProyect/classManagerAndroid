package com.example.classmanagerandroid.Screens.Login


import android.app.Activity
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.R
import com.example.classmanagerandroid.Screens.Register.bigPasswordInputWithErrorMessage
import com.example.classmanagerandroid.Screens.Register.bigTextFieldWithErrorMessage
import com.example.classmanagerandroid.Screens.ScreenItems.Dialogs.loadingDialog
import com.example.classmanagerandroid.Screens.Utils.isValidPassword
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.socialMetaTagParameters
import com.google.firebase.ktx.Firebase
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
        loadingDialog(
            loading = loading,
            informativeText = "Iniciando sesión"
        )
    }


    if (loginWithGoogle.value){
        signInWithGoogle(
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
                    val emailText = remember{ mutableStateOf("test@gmail.com") }
                    val (passwordText,onValueChangePasswordText) = remember{ mutableStateOf("11111111") }
                    var passwordError by  remember { mutableStateOf(false) }
                    val passwordTextErrorMessage by remember { mutableStateOf("La contraseña no puede ser inferior a 8 caracteres ni contener caracteres especiales") }

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
                                                .clickable {
                                                    crateDynamicLink()
                                                }
                                        )
                                    }

                                    item {
                                        OutlinedTextField(
                                            value = emailText.value,
                                            onValueChange = {
                                                emailText.value = it
                                            },
                                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                                focusedBorderColor = Color.Gray,
                                                unfocusedBorderColor = Color.LightGray
                                            ),
                                            placeholder = { Text("Email") },
                                            singleLine = true,
                                            label = { Text(text = "Email") },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(PaddingValues(start = 40.dp, end = 40.dp))
                                        )
                                    }

                                    item {
                                        bigPasswordInputWithErrorMessage(
                                            value = passwordText,
                                            onValueChangeValue = onValueChangePasswordText,
                                            valueError = passwordError,
                                            onValueChangeError = { passwordError = it },
                                            errorMessage = passwordTextErrorMessage,
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
                                                //Comprobar inicio de sesión con google
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

fun generateContentLink(): Uri {
    val baseUrl = Uri.parse("https://classManager.page.link")
    val domain = "https://classManager.page.link"

    val link = FirebaseDynamicLinks
        .getInstance()
        .createDynamicLink()
        .setLink(baseUrl)
        .setDomainUriPrefix(domain)
        .setIosParameters(DynamicLink.IosParameters.Builder("com.your.bundleid").build())
        .setAndroidParameters(DynamicLink.AndroidParameters.Builder("me.saine.android.Views.Register").build())
        .buildDynamicLink()

    return link.uri
}

/*
private fun onShareClicked() {
    val link = DynamicLinksUtil.generateContentLink()

    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, link.toString())

    startActivity(Intent.createChooser(intent, "Share Link"))
}
*/

fun crateDynamicLink() {
    val dynamicLink = Firebase.dynamicLinks.dynamicLink {
        link = Uri.parse("https://www.classManager.com/")
        domainUriPrefix = "https://classManager.page.link"
        // Open links with this app on Android
        androidParameters("me.saine.android.Views.Register") {
            minimumVersion = 125
        }
        socialMetaTagParameters {
            title = "Example of a Dynamic Link"
            description = "This link works whether the app is installed or not!"
        }
    }

    val dynamicLinkUri = dynamicLink.uri
}











@Composable
fun signInWithGoogle(
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


