package com.example.classmanagerandroid.Views.Course.Event

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.classmanagerandroid.Views.ViewsItems.confirmAlertDialog
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.example.classmanagerandroid.data.remote.Class
import com.example.classmanagerandroid.data.remote.Event
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainEvent(
    navController: NavController,
    mainViewModelEvent: MainViewModelEvent,
    idCourse: String
) {

    val context = LocalContext.current
    val searchWidgetState by mainViewModelEvent.searchWidgetState
    val searchTextState by mainViewModelEvent.searchTextState
    val aplicateFilter = remember { mutableStateOf(true) }
    var filter: String = ""
    var (deleteItems,onValueChangeDeleteItems) = remember { mutableStateOf(false) }
    val getClass = remember { mutableStateOf(true) }
    val (addNewUser,onValueChangeAddNewUser) = remember { mutableStateOf(false) }
    var (isRefreshing, onValueChangeIsRefreshing) = remember { mutableStateOf(false) }
    var startView by remember { mutableStateOf(true) }//false
    var (createEvent, onValueChangeCreateEvent) = remember { mutableStateOf(false) }
    var (modifierItem,onValueChangeModifierItem) = remember { mutableStateOf(false) }

    var (selectedEvent,onValueChangeSelectedEvent) = remember { mutableStateOf(Event("","","","","","",""))}


    var getCourse by remember { mutableStateOf(true) }

    LaunchedEffect(getCourse) {
        if (getCourse) {
            mainViewModelEvent.getSelectedCourse(idCourse)
            getCourse = false
        }
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(800L)
            onValueChangeIsRefreshing(false)
        }
    }


    if (deleteItems) {
        val title = "¿Seguro que desea eliminar todos los eventos?"
        val subtitle = "No podrás volver a recuperarlos."

        confirmAlertDialog(
            title = title,
            subtitle = subtitle,
            onValueChangeGoBack = onValueChangeDeleteItems,
            onFinishAlertDialog = {
                if (it) {
                    mainViewModelEvent.deleteAllEvents()
                    onValueChangeIsRefreshing(true)
                    Toast.makeText(context,"Se han eliminado todos los eventos",Toast.LENGTH_SHORT).show()
                }
                onValueChangeDeleteItems(false)
            }
        )
    }
    if(modifierItem) {
        modifierEvent(
            onValueChangeModifierEvent = onValueChangeModifierItem,
            mainViewModelEvent = mainViewModelEvent,
            navController = navController,
            event = selectedEvent,
            onValueChangeSelectedEvent = onValueChangeSelectedEvent,
            onValueChangeIsRefreshing = onValueChangeIsRefreshing
        )

    }
    if (createEvent) {
        createNewEvent(
            onValueChangeCreateEvent = onValueChangeCreateEvent,
            mainViewModelEvent = mainViewModelEvent,
            navController = navController,
            onValueChangeIsRefreshing = onValueChangeIsRefreshing
        )
    }
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = { onValueChangeIsRefreshing(true) },
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
                        onValueChangeDeleteItems = onValueChangeDeleteItems,
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
                        modifier = Modifier.fillMaxSize(),
                        content = {
                            LazyVerticalGrid(
                                cells = GridCells.Adaptive(minSize = 128.dp),
                                contentPadding = PaddingValues(start = 30.dp, end = 30.dp)
                            ) {
                                itemsIndexed(mainViewModelEvent.selectedEvents) { index: Int, item ->
                                    longItemEvent(
                                        event = item,
                                        onClick = {
                                            onValueChangeSelectedEvent(item)
                                            onValueChangeModifierItem(true)

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
    onValueChangeDeleteItems: (Boolean) -> Unit,
    onValueChangeAddNewUser: (Boolean) -> Unit,
) {
    when (searchWidgetState) {
        SearchWidgetState.CLOSED -> {
            defaultAppBar(
                onSearchClicked = onSearchTriggered,
                navController = navController,
                mainViewModelEvent = mainViewModelEvent,
                onValueChangeDeleteItems = onValueChangeDeleteItems,
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
    onValueChangeCreateEvent: (Boolean) -> Unit,
    mainViewModelEvent: MainViewModelEvent,
    navController: NavController,
    onValueChangeIsRefreshing: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val (textDate,onValueChangeDate) = remember { mutableStateOf("") }
    val (textStartTime,onValueChangeStartTime) = remember { mutableStateOf("") }
    val (textFinalTime,onValueChangeFinalTime) = remember { mutableStateOf("") }
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
                                suggestions = mainViewModelEvent.selectedClasses,
                                onValueChangeTextSelectedItem =  onValueChangeSelectedClass
                            )

                            showDatePicker(
                                context = context,
                                textDate = textDate,
                                onValueChangeTextDate = onValueChangeDate,
                                label = "Fecha del evento",
                                placeholder = "Fecha del evento"
                            )
                            showTimePicker(
                                context = context,
                                label = "Hora inicial",
                                placeholder = "Hora inicial del evento",
                                textTime = textStartTime,
                                onValueChangeTextTime = onValueChangeStartTime,
                            )
                            showTimePicker(
                                context = context,
                                label = "Hora Final",
                                placeholder = "Hora final del evento",
                                textTime = textFinalTime,
                                onValueChangeTextTime = onValueChangeFinalTime,
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
                                                initialTime = textStartTime,
                                                finalTime = textFinalTime,
                                                nameOfClass = selectedClass.name,
                                                date = textDate,
                                                context = context
                                            )
                                            onValueChangeCreateEvent(false)
                                            onValueChangeIsRefreshing(true)

                                            Toast.makeText(context,"El evento ha sido creado correctamente",Toast.LENGTH_SHORT).show()
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