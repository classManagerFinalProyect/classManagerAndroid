package com.example.classmanagerandroid.Views.Course

import android.content.Context
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
import com.example.classmanagerandroid.Navigation.Destinations


@Composable
fun defaultAppBar(
    onSearchClicked: () -> Unit,
    navController: NavController,
    mainViewModelCourse: MainViewModelCourse,
    onValueChangeDeleteItem: (Boolean) -> Unit,
    onValueChangeAddNewUser: (Boolean) -> Unit,
    onValueChangeGetInformation: (Boolean) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    TopAppBar(
        title = {
            Text(text = "${mainViewModelCourse.selectedCourse.name}")
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
                                    onValueChangeGetInformation(true)
                                },
                                content = {
                                    Text(text = "Ver info")
                                }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    expanded.value = false
                                    navController.navigate("${Destinations.Events.route}/${mainViewModelCourse.selectedCourse.id}")
                                },
                                content = {
                                    Text(text = "Lista de eventos")
                                }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    expanded.value = false
                                    navController.navigate("${Destinations.ViewMembers.route}/${mainViewModelCourse.selectedCourse.id}")
                                },
                                content = {
                                    Text(text = "Ver miembros")
                                }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    expanded.value = false
                                    onValueChangeAddNewUser(true)

                                },
                                content = {
                                    Text(text = "Añadir usuario")
                                }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    expanded.value = false
                                    onValueChangeDeleteItem(true)
                                },
                                content = {
                                    Text(
                                        text = "Eliminar curso",
                                        color = Color.Red
                                    )
                                }
                            )
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
