package com.example.classmanagerandroid.Screens.Practice


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.classmanagerandroid.data.local.CurrentUser
import com.example.classmanagerandroid.Screens.Practice.Components.bottomAppBar
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.defaultTopBar
import com.example.classmanagerandroid.Screens.ScreenItems.confirmAlertDialog
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.auth
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainPractice(
    navController: NavController,
    mainViewModelPractice: MainViewModelPractice,
    idPractice: String
) {
    //Help variables
    val expanded = remember { mutableStateOf(false) }
    var commentsIsSelected by remember { mutableStateOf(true) }
    val context = LocalContext.current
    var (deleteItem,onValueChangeDeleteItem) = remember { mutableStateOf(false)}
    var refresh = remember { mutableStateOf(false)}
    val getPractice = remember { mutableStateOf(true) }


    //Texts
    val (textDescription, onValueChangeTextDescription) = remember { mutableStateOf("") }
    val (textDate,onValueChangeTextDate) = remember { mutableStateOf("") }
    val (textAnnotation,onValueChangeTextAnnotation) = remember { mutableStateOf("") }
    val textMessage = remember { mutableStateOf("") }

    if(getPractice.value) {
        mainViewModelPractice.getPractice(
            idPractice = idPractice,
            onFinished = {
                onValueChangeTextDescription(mainViewModelPractice.selectedPractice.description)
                onValueChangeTextDate(mainViewModelPractice.selectedPractice.deliveryDate)
                onValueChangeTextAnnotation(mainViewModelPractice.selectedPractice.teacherAnnotation)
                getPractice.value = false
            }
        )
    }


    if (deleteItem) {
        var title = "¿Seguro que desea eliminar la prácica seleccionada?"
        var subtitle = "No podrás volver a recuperarla"

        confirmAlertDialog(
            title = title,
            subtitle = subtitle,
            onValueChangeGoBack = onValueChangeDeleteItem,
            onFinishAlertDialog = {
                if (it) {
                    mainViewModelPractice.deletePractice(
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
                    defaultTopBar(
                        title = mainViewModelPractice.selectedPractice.name,
                        navigationContent = {
                            IconButton(
                                onClick = {
                                    navController.popBackStack()
                                },
                                content = {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = "Ir atras",
                                        tint = Color.White
                                    )
                                }
                            )
                        },
                        actionsContent = {
                            if(mainViewModelPractice.rolOfSelectedUserInCurrentPractice.rol == "admin" || mainViewModelPractice.rolOfSelectedUserInCurrentPractice.rol == "profesor") {
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
                                                        onValueChangeDeleteItem(true)
                                                    },
                                                    content = {
                                                        Text(
                                                            text = "Eliminar actividad",
                                                            color = Color.Red
                                                        )
                                                    }
                                                )
                                            }
                                        )
                                    }
                                )
                            }
                        }
                    )
                },
                bottomBar = {
                    if(mainViewModelPractice.rolOfSelectedUserInCurrentPractice.rol != "padre") {
                        bottomAppBar(
                            value = textMessage,
                            refresh = refresh ,
                            mainViewModelPractice = mainViewModelPractice
                        )
                    }
                },
                content = {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        content = {
                            if (commentsIsSelected) {
                                item {
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    showDatePicker(
                                        context = context,
                                        textDate = textDate,
                                        onValueChangeTextDate = onValueChangeTextDate,
                                        label = "Fecha de entrega",
                                        placeholder = "Fecha de entrega",
                                        enabled = false,
                                        icon = Icons.Default.DateRange
                                    )
                                }
                                item {
                                    bigTextField(
                                        text = "Descripción",
                                        value = textDescription,
                                        onValueChange = onValueChangeTextDescription,
                                        KeyboardType = KeyboardType.Text,
                                        enabled = false
                                    )
                                }

                                item {
                                    if (mainViewModelPractice.rolOfSelectedUserInCurrentPractice.rol == "profesor" || mainViewModelPractice.rolOfSelectedUserInCurrentPractice.rol == "admin"){
                                        bigTextField(
                                            text = "Annotation",
                                            value = textAnnotation,
                                            onValueChange = onValueChangeTextAnnotation,
                                            KeyboardType = KeyboardType.Text,
                                            enabled = false
                                        )
                                    }
                                }
                            }


                            item {
                                Spacer(modifier = Modifier.padding(10.dp))
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(BorderStroke(1.dp, Color.LightGray)),
                                    content = {
                                        Spacer(modifier = Modifier.padding(1.dp))
                                    }
                                )
                                Row (
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier
                                        .clickable {
                                            commentsIsSelected = !commentsIsSelected
                                        },
                                    content = {
                                        Text(text = "Comments")
                                    }
                                )

                                Spacer(modifier = Modifier.padding(8.dp))
                            }

                            if (!refresh.value) {
                                itemsIndexed(mainViewModelPractice.chat.conversation){ index, item ->
                                    Row(
                                        horizontalArrangement = if (auth.currentUser?.uid.toString().equals(item.sentBy.id)) Arrangement.End else Arrangement.Start,
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        content = {

                                            Surface(
                                                modifier = Modifier
                                                    .padding(8.dp, 4.dp)
                                                    .fillMaxWidth(0.8f),
                                                shape = RoundedCornerShape(8.dp),
                                                elevation = 2.dp,
                                                content = {
                                                    Row(
                                                        modifier = Modifier
                                                            .padding(4.dp)
                                                            .fillMaxSize(),
                                                        content = {
                                                            Column(
                                                                verticalArrangement = Arrangement.Top,
                                                                content = {
                                                                    Image(
                                                                        painter = rememberAsyncImagePainter(model = CurrentUser.myImg.value),
                                                                        contentDescription = "avatar",
                                                                        modifier = Modifier
                                                                            .size(40.dp)
                                                                            .clip(CircleShape)
                                                                            .border(
                                                                                1.dp,
                                                                                Color.Gray,
                                                                                CircleShape
                                                                            ),
                                                                        contentScale = ContentScale.Crop
                                                                    )
                                                                }
                                                            )



                                                            Spacer(modifier = Modifier.padding(5.dp))
                                                            Column(
                                                                modifier = Modifier
                                                                    .padding(4.dp)
                                                                    .fillMaxHeight()
                                                                    .fillMaxWidth(0.8f),
                                                                verticalArrangement = Arrangement.Center,
                                                                content = {
                                                                    Text(
                                                                        text = item.message,
                                                                        style = MaterialTheme.typography.subtitle1,
                                                                        fontWeight = FontWeight.Bold,
                                                                    )
                                                                }
                                                            )
                                                            Text(
                                                                text = "${item.sentOn}",
                                                                style = MaterialTheme.typography.caption,
                                                                modifier = Modifier
                                                                    .padding(4.dp),
                                                            )
                                                        }
                                                    )
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

