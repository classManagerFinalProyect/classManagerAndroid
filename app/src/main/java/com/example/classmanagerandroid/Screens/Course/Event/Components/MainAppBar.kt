package com.example.classmanagerandroid.Screens.Course.Event.Components

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.classmanagerandroid.Screens.Course.Event.MainViewModelEvent
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
    mainViewModelEvent: MainViewModelEvent,
    onValueChangeDeleteItems: (Boolean) -> Unit,
    onValueChangeAddNewUser: (Boolean) -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }

    when (searchWidgetState) {
        SearchWidgetState.CLOSED -> {
            defaultTopBar(
                title = "List of events",
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
                    Box (
                        modifier = Modifier
                            .wrapContentSize(),
                        content = {
                            IconButton(
                                onClick = { expanded.value = true },
                                content = {
                                    Icon(
                                        Icons.Filled.MoreVert,
                                        contentDescription = "Localized description",
                                        tint = Color.White
                                    )
                                }
                            )

                            DropdownMenu(
                                expanded = expanded.value,
                                onDismissRequest = { expanded.value = false },
                                content = {
                                    DropdownMenuItem(
                                        onClick = {
                                            expanded.value = false
                                            onValueChangeDeleteItems(true)
                                        },
                                        content = {
                                            Text(
                                                text = "Eliminar todos los eventos",
                                                color = Color.Red
                                            )
                                        }
                                    )
                                }
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