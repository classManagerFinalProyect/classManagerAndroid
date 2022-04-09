package com.example.classmanagerandroid.Views.Class



import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.Views.ViewsItems.confirmAlertDialog
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay

import java.util.*

//Preguntar sobre como cargar los datos

@Composable
fun MainClass(
    navController: NavController,
    mainViewModelClass: MainViewModelClass,
    classId: String
){
    val context = LocalContext.current
    val searchWidgetState by mainViewModelClass.searchWidgetState
    val searchTextState by mainViewModelClass.searchTextState
    val aplicateFilter = remember { mutableStateOf(true) }
    var filter: String = ""
    var (deleteItem,onValueChangeDeleteItem) = remember { mutableStateOf(false)}
    val getClass = remember { mutableStateOf(true) }
    val (addNewUser,onValueChangeAddNewUser) = remember { mutableStateOf(false) }
    val (IdOfUser,onValueChangeIdOfUser) = remember { mutableStateOf("") }
    val (textSelectedItem,onValueChangeTextSelectedItem) = remember { mutableStateOf("") }
    var isRefreshing by remember { mutableStateOf(false) }
    var selectedRolUser by remember { mutableStateOf(mainViewModelClass.rolOfSelectedUserInCurrentClass.rol) }
    var startView by remember { mutableStateOf(true) }//false


    if (getClass.value) {
        isRefreshing = true
        mainViewModelClass.getSelectedClass(
            idClass = classId,
            onValueFinish = {
                isRefreshing = false
                //startView = true
            }
        )
        getClass.value = false
    }

    if (addNewUser) {
        addNewUser(
            onValueCloseDialog = onValueChangeAddNewUser,
            onValueChangeIdOfUser = onValueChangeIdOfUser,
            onValueChangeTextSelectedItem = onValueChangeTextSelectedItem,
            value = IdOfUser,
            label = "Id del usuario",
            placeholder = "Añadir la Id de un usuario",
            onClickSave = {
                mainViewModelClass.addNewUser(IdOfUser, context, textSelectedItem)
                onValueChangeAddNewUser(false)
            },
            rol = selectedRolUser
        )
    }


    if (deleteItem) {
        var title = "¿Seguro que desea eliminar la clase seleccionado?"
        var subtitle = "Perderas todas las prácticas creadas. "

        confirmAlertDialog(
            title = title,
            subtitle = subtitle,
            onValueChangeGoBack = onValueChangeDeleteItem,
            onFinishAlertDialog = {
                if (it) {
                    mainViewModelClass.deleteClass(
                        context = context,
                        navController = navController
                    )
                }
                onValueChangeDeleteItem(false)
            }
        )
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(800L)
            isRefreshing = false
        }
    }
    if (startView) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
            onRefresh = { isRefreshing = true },
            content =  {
                Scaffold(
                    topBar = {
                        MainAppBar(
                            searchWidgetState = searchWidgetState,
                            searchTextState = searchTextState,
                            onTextChange = {
                                mainViewModelClass.updateSearchTextState(newValue = it)
                                aplicateFilter.value = false
                                filter = it.lowercase()
                                aplicateFilter.value = true
                            },
                            onCloseClicked = {
                                mainViewModelClass.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                            },
                            onSearchClicked = {

                            },
                            onSearchTriggered = {
                                mainViewModelClass.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                            },
                            navController = navController,
                            mainViewModelClass = mainViewModelClass,
                            onValueChangeDeleteItem = onValueChangeDeleteItem,
                            onValueChangeAddNewUser = onValueChangeAddNewUser
                        )
                    },
                    content = {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            content = {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    content = {
                                        if (aplicateFilter.value) {
                                            itemsIndexed(mainViewModelClass.selectedPractices) { index: Int, item ->
                                                if (item.name.lowercase().contains(filter)) {
                                                    itemPractice(
                                                        course = item.name,
                                                        onClick = {navController.navigate("${Destinations.Practice.route}/${item.id}")}
                                                    )
                                                }
                                            }
                                        }
                                        if (selectedRolUser.equals("admin")) {
                                            item {
                                                var newActivity = remember{ mutableStateOf("") }
                                                val newPractice = remember { mutableStateOf(false) }
                                                Card(
                                                    modifier = Modifier
                                                        .padding(8.dp, 4.dp)
                                                        .fillMaxWidth()
                                                        .height(70.dp)
                                                        .clickable {
                                                            //añadir
                                                        },
                                                    shape = RoundedCornerShape(8.dp),
                                                    elevation = 4.dp,
                                                    content = {
                                                        Row(
                                                            modifier = Modifier
                                                                .padding(4.dp)
                                                                .fillMaxSize(),
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            content = {
                                                                if (newPractice.value) {
                                                                    OutlinedTextField(
                                                                        value = newActivity.value,
                                                                        modifier = Modifier.fillMaxWidth(),
                                                                        onValueChange = {
                                                                            newActivity.value = it
                                                                        },
                                                                        placeholder = {
                                                                            Text(text = "Escribe el nombre")
                                                                        },
                                                                        trailingIcon = {
                                                                            Row(
                                                                                content = {
                                                                                    IconButton(
                                                                                        onClick = {
                                                                                            mainViewModelClass.createNewPractice(
                                                                                                name = newActivity.value,
                                                                                                navController = navController
                                                                                            )
                                                                                        },
                                                                                        content = {
                                                                                            Icon(
                                                                                                imageVector = Icons.Default.Check,
                                                                                                contentDescription = "Create Practice",
                                                                                            )
                                                                                        }
                                                                                    )
                                                                                    IconButton(
                                                                                        onClick = {
                                                                                            newPractice.value = false
                                                                                        },
                                                                                        content = {
                                                                                            Icon(
                                                                                                imageVector = Icons.Default.Close,
                                                                                                contentDescription = "Create Practice",
                                                                                            )
                                                                                        }
                                                                                    )
                                                                                }
                                                                            )
                                                                        }
                                                                    )
                                                                } else {
                                                                    TextButton(
                                                                        onClick = {
                                                                            newPractice.value = true
                                                                        },
                                                                        content = {
                                                                            Text(
                                                                                text = "Añadir práctica nueva",
                                                                                fontSize = 18.sp
                                                                            )
                                                                        }
                                                                    )
                                                                }
                                                            }
                                                        )
                                                    }
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                        )
                    }
                )
            }
        )
    }
}


@Composable
private fun MainAppBar(
    searchWidgetState: SearchWidgetState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit,
    navController: NavController,
    mainViewModelClass: MainViewModelClass,
    onValueChangeDeleteItem: (Boolean) -> Unit,
    onValueChangeAddNewUser: (Boolean) -> Unit
) {
    when (searchWidgetState) {
        SearchWidgetState.CLOSED -> {
            defaultAppBar(
                onSearchClicked = onSearchTriggered,
                navController = navController,
                mainViewModelClass = mainViewModelClass,
                onValueChangeDeleteItem = onValueChangeDeleteItem,
                onValueChangeAddNewUser = onValueChangeAddNewUser
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