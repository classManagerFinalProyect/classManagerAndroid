package com.example.classmanagerandroid.Screens.Course.Event

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.classmanagerandroid.Screens.Course.Event.Components.createNewEvent
import com.example.classmanagerandroid.Screens.Course.Event.Components.mainAppBar
import com.example.classmanagerandroid.Screens.Course.Event.Components.seeEvent
import com.example.classmanagerandroid.Screens.Practice.showDatePicker
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.SearchBar.SearchWidgetState
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.defaultTopBar
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.searchAppBar
import com.example.classmanagerandroid.Screens.ScreenItems.Dialogs.loadingDialog
import com.example.classmanagerandroid.Screens.ScreenItems.confirmAlertDialog
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
    val loading = remember { mutableStateOf(false) }

    var (selectedEvent,onValueChangeSelectedEvent) = remember { mutableStateOf(Event("","","","","","",""))}


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
        loadingDialog(
            loading = loading,
            informativeText = "Creando evento..."
        )
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
            modifierEvent(
                onValueChangeModifierEvent = onValueChangeModifierItem,
                mainViewModelEvent = mainViewModelEvent,
                navController = navController,
                event = selectedEvent,
                onValueChangeSelectedEvent = onValueChangeSelectedEvent,
                onValueChangeIsRefreshing = onValueChangeIsRefreshing,
            )
        }
        else {
            seeEvent(
                onValueChangeModifierEvent = onValueChangeModifierItem,
                event = selectedEvent,
            )
        }
    }

    if (createEvent) {
        createNewEvent(
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
                    mainAppBar(
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
                                if (aplicateFilter.value) {
                                    if(!loading.value) {
                                        itemsIndexed(mainViewModelEvent.selectedEvents) { index: Int, item ->
                                            if (item.name.lowercase().contains(filter)) {
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
                                }
                            }
                        }
                    )
                }
            )
        }
    )
}




