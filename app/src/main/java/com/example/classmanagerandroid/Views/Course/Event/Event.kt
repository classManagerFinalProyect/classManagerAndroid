package com.example.classmanagerandroid.Views.Course.Event

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.example.classmanagerandroid.data.remote.Class

@Composable
fun MainEvent(
    navController: NavController,
    mainViewModelEvent: MainViewModelEvent
) {

    val context = LocalContext.current
    val searchWidgetState by mainViewModelEvent.searchWidgetState
    val searchTextState by mainViewModelEvent.searchTextState
    val aplicateFilter = remember { mutableStateOf(true) }
    var filter: String = ""
    var (deleteItem,onValueChangeDeleteItem) = remember { mutableStateOf(false) }
    val getClass = remember { mutableStateOf(true) }
    val (addNewUser,onValueChangeAddNewUser) = remember { mutableStateOf(false) }
    val (IdOfUser,onValueChangeIdOfUser) = remember { mutableStateOf("") }
    val (textSelectedItem,onValueChangeTextSelectedItem) = remember { mutableStateOf("") }
    var isRefreshing by remember { mutableStateOf(false) }
    // var selectedRolUser by remember { mutableStateOf(mainViewModelClass.rolOfSelectedUserInCurrentClass.rol) }
    var startView by remember { mutableStateOf(true) }//false
    var (createEvent, onValueChangeCreateEvent) = remember { mutableStateOf(false) }


    val (nameOfCourse,onValueChangeNameOfCourse) = remember { mutableStateOf("") }

    if (createEvent) {
        createNewEvent(
            onValueChangeCreateEvent = onValueChangeCreateEvent,
            mainViewModelEvent = mainViewModelEvent
        )
    }
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
                            mainViewModelEvent.updateSearchTextState(newValue = it)
                            aplicateFilter.value = false
                            filter = it.lowercase()
                            aplicateFilter.value = true
                        },
                        onCloseClicked = {
                            mainViewModelEvent.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                        },
                        onSearchClicked = {
                            /*aplicateFilter.value = false
                            filter = it.lowercase()
                            aplicateFilter.value = true*/
                        },
                        onSearchTriggered = {
                            mainViewModelEvent.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                        },
                        navController = navController,
                        mainViewModelEvent = mainViewModelEvent,
                        onValueChangeDeleteItem = onValueChangeDeleteItem,
                        onValueChangeAddNewUser = onValueChangeAddNewUser,
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        backgroundColor = MaterialTheme.colors.primary,
                        content = {
                            Text(text = "+")
                        },
                        onClick = {
                            onValueChangeCreateEvent(true)
                        }
                    )
                },
                content = {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                content = {
                                    if (aplicateFilter.value) {
                                        /*
                                        itemsIndexed(mainViewModelEvent.selectedClasses) { index: Int, item ->
                                            if (item.name.lowercase().contains(filter)) {
                                                itemCourse(
                                                    course = item.name,
                                                    onClick = {navController.navigate("${Destinations.Class.route}/${item.id}")}
                                                )
                                            }
                                        }*/
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


@Composable
private fun MainAppBar(
    searchWidgetState: SearchWidgetState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit,
    navController: NavController,
    mainViewModelEvent: MainViewModelEvent,
    onValueChangeDeleteItem: (Boolean) -> Unit,
    onValueChangeAddNewUser: (Boolean) -> Unit,
) {
    when (searchWidgetState) {
        SearchWidgetState.CLOSED -> {
            defaultAppBar(
                onSearchClicked = onSearchTriggered,
                navController = navController,
                mainViewModelEvent = mainViewModelEvent,
                onValueChangeDeleteItem = onValueChangeDeleteItem,
                onValueChangeAddNewUser = onValueChangeAddNewUser,
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

@Composable
fun createNewEvent(
    onValueChangeCreateEvent : (Boolean) -> Unit,
    mainViewModelEvent: MainViewModelEvent
) {
    val context = LocalContext.current
    val (textStartDate,onValueChangeStartDate) = remember { mutableStateOf("") }
    val (textFinalDate,onValueChangeFinalDate) = remember { mutableStateOf("") }
    val (nameOfEvent,onValueChangeNameOfEvent) = remember { mutableStateOf("") }
    val (selectedClass,onValueChangeSelectedClass) = remember { mutableStateOf(Class("","","", arrayListOf(), arrayListOf(),"")) }

    Dialog(
        onDismissRequest = {
            onValueChangeCreateEvent(false)
        },
        properties = DialogProperties(

        ),
        content = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                    .background(Color.White),
                content = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize(),
                        content = {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxHeight(0.2f),
                                content = {
                                    TextField(
                                        value = nameOfEvent,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(PaddingValues(start = 30.dp, end = 30.dp)),
                                        label = {
                                            Text(text = "Nombre del evento")
                                        },
                                        onValueChange = {
                                            onValueChangeNameOfEvent(it)
                                        }
                                    )
                                }
                            )
                            bigSelectedDropDownMenuClassItem(
                                label = "Clase asignada",
                                suggestions = mutableListOf(),
                                onValueChangeTextSelectedItem =  onValueChangeSelectedClass
                            )

                            showDatePicker(
                                context = context,
                                textDate = textStartDate,
                                onValueChangeTextDate = onValueChangeStartDate,
                                label = "Fecha inicial",
                                placeholder = "Fecha inicial del evento"
                            )
                            showTimePicker(context)
                            showDatePicker(
                                context = context,
                                textDate = textFinalDate,
                                onValueChangeTextDate = onValueChangeFinalDate,
                                label = "Fecha final",
                                placeholder = "Fecha final del evento"
                            )
                            Spacer(modifier = Modifier.padding(3.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth(),
                                content = {
                                    TextButton(
                                        onClick = {
                                            onValueChangeCreateEvent(false)
                                        },
                                        content = {
                                            Text(text = "Cancelar")
                                        }
                                    )
                                    TextButton(
                                        onClick = {
                                            mainViewModelEvent.createNewEvent(
                                                nameOfEvent = nameOfEvent,
                                                initialDate = textStartDate,
                                                finalDate = textFinalDate,
                                                nameOfClass = selectedClass.name
                                            )
                                        },
                                        content = {
                                            Text(text = "Añadir evento")
                                        }
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