package com.example.classmanagerandroid.Screens.Class.ViewMembersClass.Components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.SearchBar.SearchWidgetState
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.defaultTopBar
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.searchAppBar

@Composable
fun mainAppBar(
    searchWidgetState: SearchWidgetState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit,
    navController: NavController,
) {
    when (searchWidgetState) {
        SearchWidgetState.CLOSED -> {
            defaultTopBar(
                title = "Lista de usuarios",
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
                actionsContent = {
                    IconButton(
                        onClick = { onSearchTriggered() },
                        content = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search Icon",
                                tint = Color.White
                            )
                        }
                    )
                }
            )
        }
        SearchWidgetState.OPENED -> {
            searchAppBar(
                text = searchTextState,
                onTextChange = onTextChange,
                onCloseClicked = onCloseClicked,
                onSearchClicked = onSearchClicked
            )
        }
    }
}