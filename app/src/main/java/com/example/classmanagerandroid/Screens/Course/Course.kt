package com.example.classmanagerandroid.Screens.Course


import android.net.Uri
import android.widget.Toast
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
import androidx.compose.ui.text.style.TextAlign
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
import com.example.classmanagerandroid.Screens.Utils.CommonErrors
import com.example.classmanagerandroid.Screens.Utils.isValidName
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
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = getCourse) {
        if (getCourse.value) {
            isRefreshing = true
            mainViewModelCourse.clearVariables()
            mainViewModelCourse.getSelectedCourse(
                idCourse = courseId,
                onFinishResult = {
                    loading.value = false
                    isRefreshing = false
                }
            )
            getCourse.value = false
        }
    }

    val (getInformation, onValueChangeGetInformation) = remember { mutableStateOf(false) }
    val leaveCourse= remember { mutableStateOf(false) }

    val (IdOfUser,onValueChangeIdOfUser) = remember { mutableStateOf("") }
    val (addNewUser,onValueChangeAddNewUser) = remember { mutableStateOf(false) }
    val (textSelectedItem,onValueChangeTextSelectedItem) = remember { mutableStateOf("Sin asignar") }
    val (viewMembers,onValueChangeViewMembers) = remember { mutableStateOf(false) }


    if (leaveCourse.value) {
        var title = "¿Seguro que desea dejar este curso?"
        var subtitle = "El administrador deberá de darle nuevamente el acceso"

        confirmAlertDialog(
            title = title,
            subtitle = subtitle,
            onValueChangeGoBack = { leaveCourse.value = it },
            onFinishAlertDialog = {
                if (it) {
                    mainViewModelCourse.leaveCourse(
                        onFinishResult = {
                            navController.navigate(Destinations.MainAppView.route) {
                                popUpTo(0)
                                Toast.makeText(context, "Ha abandonado el curso correctamente",Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
                leaveCourse.value = false
            }
        )
    }
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
            textSelectedItem = textSelectedItem
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

    LaunchedEffect(isRefreshing) {
        if (isRefreshing && !loading.value) {
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
                        onValueChangeGetInformation = onValueChangeGetInformation,
                        leaveCourse = leaveCourse
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
                                        itemsIndexed(
                                            mainViewModelCourse.selectedClasses
                                        ) { index: Int, item ->

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

                                    if(mainViewModelCourse.rolOfSelectedUserInCurrentCourse.rol == "admin") {
                                        item {
                                            var newClass = remember{ mutableStateOf("") }
                                            var newClassError = remember{ mutableStateOf(false) }
                                            val newItem = remember { mutableStateOf(false) }
                                            Card(
                                                modifier = Modifier
                                                    .padding(8.dp, 4.dp)
                                                    .fillMaxWidth()
                                                    .height(90.dp),
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
                                                                Column(
                                                                    content = {
                                                                        OutlinedTextField(
                                                                            value = newClass.value,
                                                                            modifier = Modifier.fillMaxWidth(),
                                                                            isError = newClassError.value,
                                                                            onValueChange = {
                                                                                newClass.value = it
                                                                                newClassError.value = (!isValidName(it))
                                                                            },
                                                                            placeholder = {
                                                                                Text(text = "Escribe el nombre")
                                                                            },
                                                                            trailingIcon = {
                                                                                Row(
                                                                                    content = {
                                                                                        IconButton(
                                                                                            onClick = {
                                                                                                if(isValidName(newClass.value)) {
                                                                                                    mainViewModelCourse.createNewClass(
                                                                                                        navController = navController,
                                                                                                        context = context,
                                                                                                        textDescription = "",
                                                                                                        textNameOfClass = newClass.value,
                                                                                                        itemSelectedCurse = mainViewModelCourse.selectedCourse
                                                                                                    )
                                                                                                }
                                                                                                else
                                                                                                    Toast.makeText(context, CommonErrors.incompleteFields,Toast.LENGTH_SHORT).show()

                                                                                            },
                                                                                            content = {
                                                                                                Icon(
                                                                                                    imageVector = Icons.Default.Check,
                                                                                                    contentDescription = "Create Practice",
                                                                                                    tint = MaterialTheme.colors.secondary
                                                                                                )
                                                                                            }
                                                                                        )
                                                                                        IconButton(
                                                                                            onClick = {
                                                                                                newItem.value = false
                                                                                                newClass.value = ""
                                                                                            },
                                                                                            content = {
                                                                                                Icon(
                                                                                                    imageVector = Icons.Default.Close,
                                                                                                    contentDescription = "Create Practice",
                                                                                                    tint = MaterialTheme.colors.secondary
                                                                                                )
                                                                                            }
                                                                                        )
                                                                                    }
                                                                                )
                                                                            }
                                                                        )
                                                                        val assistiveElementText = if (newClassError.value) CommonErrors.notValidName else ""
                                                                        val assistiveElementColor = if (newClassError.value)
                                                                            MaterialTheme.colors.error
                                                                        else
                                                                            MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)

                                                                        Text(
                                                                            text = assistiveElementText,
                                                                            color = assistiveElementColor,
                                                                            style = MaterialTheme.typography.caption,
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
                                                                            fontSize = 18.sp,
                                                                            modifier = Modifier.fillMaxWidth(),
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


