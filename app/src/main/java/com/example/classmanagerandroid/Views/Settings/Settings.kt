package com.example.classmanagerandroid.Views.Settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.classmanagerandroid.Classes.CurrentUser
import com.example.classmanagerandroid.Navigation.Destinations


@Composable
fun MainSettings(
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Ajustes")
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
            LazyColumn(
                content ={
                    item {
                        itemSetting(
                            title = CurrentUser.currentUser.name,
                            subtitle = CurrentUser.currentUser.email,
                            urlImage = CurrentUser.currentUser.imgPath,
                            onClick = {
                                navController.navigate(Destinations.MyAccount.route)
                            }
                        )
                    }
                    item {
                        itemSetting(
                            title = "Mi cuenta",
                            subtitle = "Cerrar Sesión, Privacidad...",
                            urlImage = CurrentUser.currentUser.imgPath,
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