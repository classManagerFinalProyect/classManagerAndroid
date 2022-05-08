package com.example.classmanagerandroid.Screens.Course


import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.R
import com.example.classmanagerandroid.Screens.Course.Components.mainAppBar
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.SearchBar.SearchWidgetState
import com.example.classmanagerandroid.Screens.ScreenItems.Cards.longHorizontalCard
import com.example.classmanagerandroid.Screens.ScreenItems.Dialogs.loadingDialog
import com.example.classmanagerandroid.Screens.ScreenItems.confirmAlertDialog
import com.example.classmanagerandroid.data.network.AccessToDataBase
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay


@Composable
fun MainCourse(
    navController: NavController,
    mainViewModelCourse: MainViewModelCourse,
    courseId: String
) {



    //Help variables
    val loading = remember { mutableStateOf(true) }
    val context = LocalContext.current
    var (deleteItem,onValueChangeDeleteItem) = remember { mutableStateOf(false)}
    val searchWidgetState by mainViewModelCourse.searchWidgetState
    val searchTextState by mainViewModelCourse.searchTextState
    val applyFilter = remember { mutableStateOf(true) }
    var filter: String = ""
    val getCourse = remember { mutableStateOf(true) }

    if (loading.value){
        loadingDialog(
            loading = loading,
            informativeText = "Obteniendo datos"
        )
    }

    LaunchedEffect(key1 = getCourse) {
        if (getCourse.value) {
            mainViewModelCourse.clearVariables()
            mainViewModelCourse.getSelectedCourse(
                idCourse = courseId,
                onFinishResult = {
                    loading.value = false
                }
            )
            getCourse.value = false
        }
    }

    val (getInformation, onValueChangeGetInformation) = remember { mutableStateOf(false) }

    val (IdOfUser,onValueChangeIdOfUser) = remember { mutableStateOf("") }
    val (addNewUser,onValueChangeAddNewUser) = remember { mutableStateOf(false) }
    val (textSelectedItem,onValueChangeTextSelectedItem) = remember { mutableStateOf("") }
    val (viewMembers,onValueChangeViewMembers) = remember { mutableStateOf(false) }

    if(getInformation) {
        editCourse(
            onValueChangeGetInformation = onValueChangeGetInformation,
            mainViewModelCourse = mainViewModelCourse
        )
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
                mainViewModelCourse.addNewUser(IdOfUser, context, textSelectedItem)
                onValueChangeAddNewUser(false)
            },
            rol = mainViewModelCourse.rolOfSelectedUserInCurrentCourse.rol
        )
    }


    if (deleteItem) {
        var title: String = "¿Seguro que desea eliminar el Curso seleccionado?"
        var subtitle: String = ""
        if (mainViewModelCourse.selectedClasses.size == 0)
            subtitle = "Este curso no contiene ninguna clase"
        else
            subtitle = "Este curso contiene ${mainViewModelCourse.selectedClasses.size} clases, se eliminarán también. "

        confirmAlertDialog(
            title = title,
            subtitle = subtitle,
            onValueChangeGoBack = onValueChangeDeleteItem,
            onFinishAlertDialog = {
                if (it) {
                    mainViewModelCourse.deleteCurse(
                        context = context,
                        navController = navController
                    )
                }
                onValueChangeDeleteItem(false)
            }
        )
    }
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(800L)
            isRefreshing = false
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = { isRefreshing = true },
        content =  {
            Scaffold(
                topBar = {
                    mainAppBar(
                        searchWidgetState = searchWidgetState,
                        searchTextState = searchTextState,
                        onTextChange = {
                            mainViewModelCourse.updateSearchTextState(newValue = it)
                            applyFilter.value = false
                            filter = it.lowercase()
                            applyFilter.value = true
                        },
                        onCloseClicked = {
                            mainViewModelCourse.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                        },
                        onSearchClicked = {
                            /*aplicateFilter.value = false
                            filter = it.lowercase()
                            aplicateFilter.value = true*/
                        },
                        onSearchTriggered = {
                            mainViewModelCourse.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                        },
                        navController = navController,
                        mainViewModelCourse = mainViewModelCourse,
                        onValueChangeDeleteItem = onValueChangeDeleteItem,
                        onValueChangeAddNewUser = onValueChangeAddNewUser,
                        onValueChangeGetInformation = onValueChangeGetInformation
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
                                    if (applyFilter.value) {
                                        itemsIndexed(mainViewModelCourse.selectedClasses) { index: Int, item ->
                                            val gsReference = AccessToDataBase.storageInstance.getReferenceFromUrl(item.img)
                                            val classImg = remember { mutableStateOf<Uri?>(null) }
                                            gsReference.downloadUrl.addOnSuccessListener { classImg.value = it }

                                            if (item.name.lowercase().contains(filter)) {
                                                longHorizontalCard(
                                                    title = item.name,
                                                    subtitle = "${item.idPractices.size}",
                                                    onClick = {
                                                        navController.navigate("${Destinations.Class.route}/${item.id}")
                                                        mainViewModelCourse.clearSearchBar()
                                                    },
                                                    subtitleIconPainter = painterResource(id = R.drawable.task_black),
                                                    imagePainter = rememberAsyncImagePainter(
                                                        model = ImageRequest.Builder(LocalContext.current)
                                                            .data(classImg.value)
                                                            .scale(Scale.FILL)
                                                            .transformations(CircleCropTransformation())
                                                            .build()
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    item {
                                        var newClass = remember{ mutableStateOf("") }
                                        val newItem = remember { mutableStateOf(false) }
                                        Card(
                                            modifier = Modifier
                                                .padding(8.dp, 4.dp)
                                                .fillMaxWidth()
                                                .height(70.dp)
                                                .clickable {
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
                                                        if (newItem.value) {
                                                            OutlinedTextField(
                                                                value = newClass.value,
                                                                modifier = Modifier.fillMaxWidth(),
                                                                onValueChange = {
                                                                    newClass.value = it
                                                                },
                                                                placeholder = {
                                                                    Text(text = "Escribe el nombre")
                                                                },
                                                                trailingIcon = {
                                                                    Row(
                                                                        content = {
                                                                            IconButton(
                                                                                onClick = {
                                                                                    mainViewModelCourse.createNewClass(
                                                                                        navController = navController,
                                                                                        context = context,
                                                                                        textDescription = "",
                                                                                        textNameOfClass = newClass.value,
                                                                                        itemSelectedCurse = mainViewModelCourse.selectedCourse
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
                                                                                    newItem.value = false
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
                                                                    newItem.value = true
                                                                },
                                                                content = {
                                                                    Text(
                                                                        text = "Crear nueva clase",
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
                            )
                        }
                    )
                }
            )
        }
    )
}


