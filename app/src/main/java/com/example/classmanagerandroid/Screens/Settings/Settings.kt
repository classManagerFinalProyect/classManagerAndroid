package com.example.classmanagerandroid.Screens.Settings

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.classmanagerandroid.Classes.CurrentUser
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.defaultTopBar


@Composable
fun MainSettings(
    navController: NavController,
    viewModelSettings: ViewModelSettings
) {
    Scaffold(
        topBar = {
            defaultTopBar(
                title = "Ajustes",
                navigationContent = {
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
                },
                actionsContent = {}
            )
        },
        content = {
            LazyColumn(
                content ={
                    item {
                        itemSetting(
                            title = CurrentUser.currentUser.name,
                            subtitle = CurrentUser.currentUser.email,
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(CurrentUser.myImg.value)
                                    .scale(Scale.FILL)
                                    .transformations(CircleCropTransformation())
                                    .build()
                            ),
                            onClick = {
                                navController.navigate(Destinations.MyAccount.route)
                            }
                        )
                    }
                    item {
                        itemSetting(
                            title = "Mi cuenta",
                            subtitle = "Cerrar Sesión, Privacidad...",
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(CurrentUser.myImg.value)
                                    .scale(Scale.FILL)
                                    .transformations(CircleCropTransformation())
                                    .build()
                            ),
                            onClick = {
                                navController.navigate(Destinations.MyAccountOptions.route)
                            }
                        )
                    }
                }
            )
        }
    )


}