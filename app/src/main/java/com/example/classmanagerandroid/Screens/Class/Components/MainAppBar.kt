package com.example.classmanagerandroid.Screens.Class.Components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.Screens.Class.MainViewModelClass
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
    mainViewModelClass: MainViewModelClass,
    onValueChangeDeleteItem: (Boolean) -> Unit,
    onValueChangeAddNewUser: (Boolean) -> Unit,
    editClass: MutableState<Boolean>
) {
    var selectedRolUser by remember { mutableStateOf(mainViewModelClass.rolOfSelectedUserInCurrentClass.rol) }
    val expanded = remember { mutableStateOf(false) }

    when (searchWidgetState) {
        SearchWidgetState.CLOSED -> {
            defaultTopBar(
                title = "${mainViewModelClass.selectedClass.name}",
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
                                            editClass.value = true
                                        },
                                        content = {
                                            Text(text = "Editar clase")
                                        }
                                    )
                                    DropdownMenuItem(
                                        onClick = {
                                            expanded.value = false
                                        },
                                        content = {
                                            Text(text = "Ver ayuda")
                                        }
                                    )
                                    if (selectedRolUser.equals("admin") || selectedRolUser.equals("profesor")) {
                                        DropdownMenuItem(
                                            onClick = {
                                                onValueChangeAddNewUser(true)
                                                expanded.value = false
                                            },
                                            content = {
                                                Text(text = "Añadir usuario")
                                            }
                                        )
                                    }

                                    DropdownMenuItem(
                                        onClick = {
                                            expanded.value = false
                                            navController.navigate("${Destinations.ViewMembersClass.route}/${mainViewModelClass.selectedClass.id}")

                                        },
                                        content = {
                                            Text(text = "Ver miembros")
                                        }
                                    )
                                    if (selectedRolUser.equals("admin")) {
                                        DropdownMenuItem(
                                            onClick = {
                                                expanded.value = false
                                                onValueChangeDeleteItem(true)
                                            },
                                            content = {
                                                Text(
                                                    text = "Eliminar clase",
                                                    color = Color.Red
                                                )
                                            }
                                        )
                                    }
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