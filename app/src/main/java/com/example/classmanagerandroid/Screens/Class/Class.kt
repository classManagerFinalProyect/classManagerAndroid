package com.example.classmanagerandroid.Screens.Class



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.R
import com.example.classmanagerandroid.Screens.Class.Components.AddNewPractice
import com.example.classmanagerandroid.Screens.Class.Components.EditClass
import com.example.classmanagerandroid.Screens.Class.Components.MainAppBar
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.SearchBar.SearchWidgetState
import com.example.classmanagerandroid.Screens.ScreenItems.Cards.LongHorizontalCard
import com.example.classmanagerandroid.Screens.ScreenItems.Dialogs.ConfirmAlertDialog
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay

@Composable
fun MainClass(
    navController: NavController,
    mainViewModelClass: MainViewModelClass,
    classId: String
){
    val context = LocalContext.current
    val searchWidgetState by mainViewModelClass.searchWidgetState
    val searchTextState by mainViewModelClass.searchTextState
    val applicativeFilter = remember { mutableStateOf(true) }
    var filter = ""
    val (deleteItem,onValueChangeDeleteItem) = remember { mutableStateOf(false)}
    val getClass = remember { mutableStateOf(true) }
    var addNewPractice by remember { mutableStateOf(false) }
    val (addNewUser,onValueChangeAddNewUser) = remember { mutableStateOf(false) }
    val (IdOfUser,onValueChangeIdOfUser) = remember { mutableStateOf("") }
    val (textSelectedItem,onValueChangeTextSelectedItem) = remember { mutableStateOf("") }
    var isRefreshing = remember { mutableStateOf(false) }
    val startView by remember { mutableStateOf(true) }//false
    var showAddPractice by remember { mutableStateOf(false)}
    val editClass = remember { mutableStateOf(false)}
    val loading = remember { mutableStateOf(true) }
    val leaveClass = remember { mutableStateOf(false) }

    if(editClass.value) {
        EditClass(
            editClass = editClass,
            mainViewModelClass = mainViewModelClass
        )
    }

    if (getClass.value) {
        isRefreshing.value = true
        mainViewModelClass.clearVariables()
        mainViewModelClass.getSelectedClass(
            idClass = classId,
            onValueFinish = {
                loading.value = false
                isRefreshing.value = false
                if (mainViewModelClass.selectedClass.idPractices.size == 0) showAddPractice = true
            }
        )
        getClass.value = false
    }

    if (addNewUser) {
        AddNewUser(
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
            rol = mainViewModelClass.rolOfSelectedUserInCurrentClass.rol
        )
    }


    if (deleteItem) {
        val title = "¿Seguro que desea eliminar la clase seleccionada?"
        val subtitle = "Perderas todas las prácticas creadas. "

        ConfirmAlertDialog(
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

    if (leaveClass.value) {
        val title = "¿Seguro que desea abandonar la clase seleccionada?"
        val subtitle = "No podrás volver a acceder sin permiso del administrador "

        ConfirmAlertDialog(
            title = title,
            subtitle = subtitle,
            onValueChangeGoBack = { leaveClass.value = it},
            onFinishAlertDialog = {
                if (it) {
                    mainViewModelClass.leaveClass(
                        navController =  navController,
                        context = context,
                        onFinishResult = {
                            navController.navigate(Destinations.MainAppView.route)
                        }
                    )
                }
                leaveClass.value = false
            }
        )
    }

    if(addNewPractice) {
        AddNewPractice(
            onValueCloseDialog = { addNewPractice = it },
            mainViewModelClass = mainViewModelClass,
            navController = navController
        )

    }


    LaunchedEffect(isRefreshing.value) {
        if (isRefreshing.value) {
            delay(800L)
            isRefreshing.value = false
        }
    }

    if (startView) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
            onRefresh = { isRefreshing.value = true },
            content =  {
                Scaffold(
                    floatingActionButton = {
                        if (mainViewModelClass.rolOfSelectedUserInCurrentClass.rol == "admin" || mainViewModelClass.rolOfSelectedUserInCurrentClass.rol == "profesor") {
                            FloatingActionButton(
                                backgroundColor = MaterialTheme.colors.primary,
                                onClick = {
                                    addNewPractice = true
                                },
                                content = {
                                    Text(text = "+")
                                }
                            )
                        }
                    },
                    topBar = {

                        MainAppBar(
                            searchWidgetState = searchWidgetState,
                            searchTextState = searchTextState,
                            onTextChange = {
                                mainViewModelClass.updateSearchTextState(newValue = it)
                                applicativeFilter.value = false
                                filter = it.lowercase()
                                applicativeFilter.value = true
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
                            onValueChangeAddNewUser = onValueChangeAddNewUser,
                            editClass = editClass,
                            loading = loading,
                            leaveClass = leaveClass
                        )
                    },
                    content = {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            content = {
                                if(showAddPractice) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        content = {
                                            if (mainViewModelClass.rolOfSelectedUserInCurrentClass.rol == "admin" || mainViewModelClass.rolOfSelectedUserInCurrentClass.rol == "profesor") {
                                                TextButton(
                                                    onClick = {
                                                        addNewPractice = true
                                                    },
                                                    content = {
                                                        Text(text = "Crear nueva práctica", fontSize = 18.sp)
                                                    }
                                                )
                                            }
                                            else {
                                                 Text(text = "No Hay prácticas creadas")
                                          }
                                        }
                                    )
                                }
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    content = {
                                        if (applicativeFilter.value) {
                                            itemsIndexed(mainViewModelClass.selectedPractices) { index: Int, item ->


                                                if (item.name.lowercase().contains(filter)) {
                                                    LongHorizontalCard(
                                                        title = item.name,
                                                        subtitle = item.deliveryDate,
                                                        onClick = {
                                                            navController.navigate("${Destinations.Practice.route}/${item.id}")
                                                            mainViewModelClass.clearSearchBar()
                                                        },
                                                        subtitleIconPainter = painterResource(id = R.drawable.date_range),
                                                        imagePainter = rememberAsyncImagePainter(
                                                            model = ImageRequest.Builder(LocalContext.current)
                                                                .data(mainViewModelClass.classImg.value)
                                                                .scale(Scale.FILL)
                                                                .transformations(CircleCropTransformation())
                                                                .build()
                                                        )
                                                    )
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
        )
    }
}


