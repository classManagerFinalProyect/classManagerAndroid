package com.example.classmanagerandroid.Views.Class

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



@Composable
fun defaultAppBar(
    onSearchClicked: () -> Unit,
    navController: NavController,
    mainViewModelClass: MainViewModelClass,
    onValueChangeDeleteItem: (Boolean) -> Unit,
    onValueChangeAddNewUser: (Boolean) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var selectedRolUser by remember { mutableStateOf(mainViewModelClass.rolOfSelectedUserInCurrentClass.rol) }

    TopAppBar(
        title = {
            Text(text = "${mainViewModelClass.selectedClass.name}")
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
                                },
                                content = {
                                    Text(text = "Ver info")
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
