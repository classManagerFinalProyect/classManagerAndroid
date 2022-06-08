package com.example.classmanagerandroid.Screens.Course.Event

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classmanagerandroid.Screens.Course.Event.Components.CreateNewEvent
import com.example.classmanagerandroid.Screens.Course.Event.Components.MainAppBar
import com.example.classmanagerandroid.Screens.Course.Event.Components.SeeEvent
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.SearchBar.SearchWidgetState
import com.example.classmanagerandroid.Screens.ScreenItems.Dialogs.LoadingDialog
import com.example.classmanagerandroid.Screens.ScreenItems.Dialogs.ConfirmAlertDialog
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
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
    val applicativeFilter = remember { mutableStateOf(true) }
    var filter = ""
    val (deleteItems,onValueChangeDeleteItems) = remember { mutableStateOf(false) }
    val (isRefreshing, onValueChangeIsRefreshing) = remember { mutableStateOf(false) }
    val (createEvent, onValueChangeCreateEvent) = remember { mutableStateOf(false) }
    val (modifierItem,onValueChangeModifierItem) = remember { mutableStateOf(false) }
    val loading = remember { mutableStateOf(false) }

    val (selectedEvent,onValueChangeSelectedEvent) = remember { mutableStateOf(Event("","","","","","",""))}


    var getCourse by remember { mutableStateOf(true) }

    LaunchedEffect(getCourse) {
        if (getCourse) {
            mainViewModelEvent.clearVariables()
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

    if (loading.value){
        LoadingDialog(
            loading = loading,
            informativeText = "Creando evento..."
        )
    }

    if (deleteItems) {
        val title = "¿Seguro que desea eliminar todos los eventos?"
        val subtitle = "No podrás volver a recuperarlos."

        ConfirmAlertDialog(
            title = title,
            subtitle = subtitle,
            onValueChangeGoBack = onValueChangeDeleteItems,
            onFinishAlertDialog = {
                if (it) {
                    mainViewModelEvent.deleteAllEvents(
                        onFinished = {
                            onValueChangeIsRefreshing(true)
                        }
                    )
                    Toast.makeText(context,"Se han eliminado todos los eventos",Toast.LENGTH_SHORT).show()
                }
                onValueChangeDeleteItems(false)
            }
        )
    }
    if(modifierItem) {
        if(mainViewModelEvent.rolOfSelectedUserInCurrentCourse.rol == "admin") {
            ModifierEvent(
                onValueChangeModifierEvent = onValueChangeModifierItem,
                mainViewModelEvent = mainViewModelEvent,
                event = selectedEvent,
                onValueChangeSelectedEvent = onValueChangeSelectedEvent,
                onValueChangeIsRefreshing = onValueChangeIsRefreshing,
            )
        }
        else {
            SeeEvent(
                onValueChangeModifierEvent = onValueChangeModifierItem,
                event = selectedEvent,
            )
        }
    }

    if (createEvent) {
        CreateNewEvent(
            onValueChangeCreateEvent = onValueChangeCreateEvent,
            mainViewModelEvent = mainViewModelEvent,
            loading = loading
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
                            applicativeFilter.value = false
                            filter = it.lowercase()
                            applicativeFilter.value = true
                        },
                        onCloseClicked = {
                            mainViewModelEvent.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                        },
                        onSearchClicked = {

                        },
                        onSearchTriggered = {
                            mainViewModelEvent.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                        },
                        navController = navController,
                        mainViewModelEvent = mainViewModelEvent,
                        onValueChangeDeleteItems = onValueChangeDeleteItems,
                    )
                },
                floatingActionButton = {
                    if(mainViewModelEvent.rolOfSelectedUserInCurrentCourse.rol == "admin" || mainViewModelEvent.rolOfSelectedUserInCurrentCourse.rol == "profesor") {
                        FloatingActionButton(
                            backgroundColor = MaterialTheme.colors.primary,
                            content = {
                                Text(text = "+")
                            },
                            onClick = {
                                onValueChangeCreateEvent(true)
                            }
                        )
                    }
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
                                if (applicativeFilter.value) {
                                    if(!loading.value) {
                                        itemsIndexed(mainViewModelEvent.selectedEvents) { _: Int, item ->
                                            if (item.name.lowercase().contains(filter)) {
                                                LongItemEvent(
                                                    event = item,
                                                    onClick = {
                                                        onValueChangeSelectedEvent(item)
                                                        onValueChangeModifierItem(true)

                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            )
        }
    )
}




