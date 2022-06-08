package com.example.classmanagerandroid.Screens.SplashScreen

import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.R
import com.example.classmanagerandroid.Screens.Login.MainViewModelLogin
import com.example.classmanagerandroid.data.network.AccessToDataBase
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    mainViewModelLogin: MainViewModelLogin
) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 3000
        )
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2000)
        navController.popBackStack()


        //auth.signOut()
        val user = AccessToDataBase.auth.currentUser
        if (user != null) {
            mainViewModelLogin.saveCurrentUser {
                navController.navigate(Destinations.MainAppView.route)
            }
        } else {
            navController.navigate(Destinations.Login.route)
        }
    }
    Splash(alpha = alphaAnim.value)

}


@Composable
fun Splash(alpha: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_sin_fondo),
            contentDescription = "Logo",
            modifier = Modifier
                .height(300.dp)
                .width(500.dp)
                .padding(20.dp)
                .alpha(alpha = alpha)
        )
    }
}