package com.example.classmanagerandroid.Screens.ScreenComponents.TopBar

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable

@Composable
fun DefaultTopBar(
    title: String,
    navigationContent: @Composable () -> Unit,
    actionsContent: @Composable () -> Unit
){
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = { navigationContent() },
        actions = { actionsContent() }
    )

}