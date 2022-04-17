package com.example.classmanagerandroid.Views.Class.ViewMembersClass

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController


@Composable
fun defaultAppBar(
    onSearchClicked: () -> Unit,
    navController: NavController,
    mainViewModelViewMembersClass: MainViewModelViewMembersClass,

) {
    TopAppBar(
        title = {
            Text(text = "Lista de usuarios")
        },
        actions = {
            IconButton(
                onClick = { onSearchClicked() },
                content = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Icon",
                        tint = Color.White
                    )
                }
            )
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
}
