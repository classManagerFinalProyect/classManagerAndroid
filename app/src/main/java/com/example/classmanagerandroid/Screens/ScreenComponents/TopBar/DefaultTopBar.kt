package com.example.classmanagerandroid.Screens.ScreenComponents.TopBar

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun defaultTopBar(
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