package com.example.classmanagerandroid.Screens.MainAppActivity


import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.classmanagerandroid.data.local.CurrentUser
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.R
import com.example.classmanagerandroid.Screens.MainAppActivity.Components.MainBody.ContentState
import com.example.classmanagerandroid.Screens.MainAppActivity.Components.MainBody.ShowClasses
import com.example.classmanagerandroid.Screens.MainAppActivity.Components.MainBody.ShowCourses
import com.example.classmanagerandroid.Screens.MainAppActivity.Components.BottomBar
import com.example.classmanagerandroid.Screens.MainAppActivity.Components.CreateNewItem
import com.example.classmanagerandroid.Screens.MainAppActivity.Components.MainAppBar
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.SearchBar.SearchWidgetState
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.auth
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainAppView(
    navController: NavController,
    mainViewModelMainAppView: MainViewModelMainAppView
) {

    //Help variables
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val searchWidgetState by mainViewModelMainAppView.searchWidgetState
    val searchTextState by mainViewModelMainAppView.searchTextState
    val createItem = remember { mutableStateOf(false) }
    val applicativeFilter = remember { mutableStateOf(true) }
    var filter = ""
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val contentState by mainViewModelMainAppView.contentState
    val sendId = remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }

    if(sendId.value) {
        SendId(
            id = auth.currentUser?.uid!!
        )
        sendId.value = false
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(800L)
            isRefreshing = false
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = {
            isRefreshing = true
            mainViewModelMainAppView.clearSearchBar()
        },
        content =  {
            Scaffold (
                scaffoldState = scaffoldState,
                topBar = {
                    MainAppBar(
                        searchWidgetState = searchWidgetState,
                        searchTextState = searchTextState,
                        onTextChange = {
                            mainViewModelMainAppView.updateSearchTextState(newValue = it)
                            applicativeFilter.value = false
                            filter = it.lowercase()
                            applicativeFilter.value = true
                        },
                        onCloseClicked = {
                            mainViewModelMainAppView.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                        },
                        onSearchClicked = {

                        },
                        onSearchTriggered = {
                            mainViewModelMainAppView.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                        },
                        scaffoldState = scaffoldState
                    )
                },
                bottomBar = {
                    BottomBar(mainAppView = mainViewModelMainAppView)
                },
                floatingActionButtonPosition = FabPosition.End,
                isFloatingActionButtonDocked = !createItem.value,
                floatingActionButton = {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center,
                        //modifier = Modifier.background(Color.Black),
                        content = {
                            if (createItem.value) {
                                CreateNewItem(
                                    navController = navController,
                                    onValueChangeCreateItem = { createItem.value = it }
                                )
                            }
                            FloatingActionButton(
                                backgroundColor = MaterialTheme.colors.primary,
                                content = {
                                    Text(text = if (createItem.value) "-" else "+")
                                },
                                onClick = {
                                    createItem.value = !createItem.value
                                }
                            )
                        }
                    )
                },
                drawerContent = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = {
                            LazyColumn(
                                modifier = Modifier
                                    .padding(
                                        PaddingValues(
                                            start = 10.dp,
                                            end = 10.dp
                                        )
                                    ),
                                content = {
                                    item {
                                        Column(
                                            content = {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.Center,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(
                                                            PaddingValues(
                                                                top = 10.dp,
                                                                bottom = 10.dp
                                                            )
                                                        ),
                                                    content = {
                                                        Image(
                                                            painter = rememberAsyncImagePainter(model = CurrentUser.myImg.value),
                                                            contentDescription = "avatar",
                                                            modifier = Modifier
                                                                .size(180.dp)
                                                                .clip(CircleShape)
                                                                .border(
                                                                    2.dp,
                                                                    Color.Gray,
                                                                    CircleShape
                                                                )
                                                                .align(Alignment.CenterVertically),
                                                            contentScale = ContentScale.Crop,

                                                            )
                                                    }
                                                )
                                                Text(text = CurrentUser.currentUser.name.uppercase())
                                                Text(text = CurrentUser.currentUser.email)
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(30.dp),
                                                    content = {
                                                        Row(
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            modifier = Modifier
                                                                .clickable {
                                                                    clipboardManager.setText(
                                                                        AnnotatedString(
                                                                            "${auth.currentUser?.uid}"
                                                                        )
                                                                    )
                                                                    Toast
                                                                        .makeText(
                                                                            context,
                                                                            "Id del usuario copiado",
                                                                            Toast.LENGTH_SHORT
                                                                        )
                                                                        .show()
                                                                },
                                                            content = {
                                                                Text(text = "#${auth.currentUser?.uid}")
                                                                Spacer(modifier = Modifier.padding(4.dp))

                                                                Icon(
                                                                    painter = rememberAsyncImagePainter(
                                                                        model = "https://firebasestorage.googleapis.com/v0/b/class-manager-58dbf.appspot.com/o/appImages%2Fcontent_copy_black.png?alt=media&token=bb9aba8d-74a7-4279-a59f-7adee22142ae"
                                                                    ),
                                                                    contentDescription = "Copiar"
                                                                )
                                                            }
                                                        )
                                                        IconButton(
                                                            onClick = {
                                                                sendId.value = true
                                                            },
                                                            content = {
                                                                Icon(
                                                                    imageVector = Icons.Default.Share,
                                                                    contentDescription = "Send Email"
                                                                )
                                                            }
                                                        )
                                                    }
                                                )


                                            }
                                        )
                                    }

                                    item {
                                        Spacer(modifier = Modifier.padding(4.dp))

                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .border(BorderStroke(1.dp, Color.LightGray)),
                                            content = {
                                                Spacer(modifier = Modifier.padding(1.dp))
                                            }
                                        )


                                        val icon = if (expanded)
                                            Icons.Filled.KeyboardArrowUp
                                        else
                                            Icons.Filled.KeyboardArrowDown

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start,
                                            modifier = Modifier.fillMaxWidth(),
                                            content = {
                                                Text(text = "Cursos:")
                                                IconButton(
                                                    onClick = {
                                                        expanded = !expanded
                                                    },
                                                    content = {
                                                        Icon(
                                                            imageVector =  icon,
                                                            contentDescription = "arrowExpanded",
                                                        )
                                                    }
                                                )
                                            }
                                        )
                                    }
                                    if(expanded) {
                                        itemsIndexed(CurrentUser.myCourses) { _, item ->
                                            TextButton (
                                                onClick = {
                                                    navController.navigate("${Destinations.Course.route}/${item.id}")
                                                    mainViewModelMainAppView.clearSearchBar()
                                                },
                                                content = {
                                                    Text(text = item.name)
                                                }
                                            )
                                        }
                                    }

                                    item {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(30.dp)
                                                .clickable {
                                                    navController.navigate(Destinations.Settings.route)
                                                },
                                            content = {
                                                Icon(
                                                    imageVector = Icons.Default.Settings,
                                                    contentDescription = "Ajustes"
                                                )
                                                Spacer(modifier = Modifier.padding(3.dp))
                                                Text(text = "Ajustes")
                                            }
                                        )
                                    }
                                    item {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(30.dp)
                                                .clickable {
                                                    auth.signOut()
                                                    navController.navigate(Destinations.Login.route) {
                                                        popUpTo(0)
                                                    }
                                                },
                                            content = {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.logout_white),
                                                    contentDescription = "Cerrar sesión"
                                                )
                                                Spacer(modifier = Modifier.padding(3.dp))
                                                Text(text = "Cerrar sesión")
                                            }
                                        )
                                    }
                                }
                            )
                        }
                    )
                },
                content = {
                    Column(
                        modifier = Modifier
                            .alpha(if (createItem.value) 0.2f else 1f)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        if (createItem.value) createItem.value = false
                                    }
                                )
                            }
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = {
                            MainContent(
                                contentState = contentState,
                                mainViewModelMainAppView = mainViewModelMainAppView,
                                filter = filter,
                                navController = navController,
                                applyFilter = applicativeFilter.value,
                                createItem = createItem
                            )
                        }
                    )
                }
            )
        }
    )
}

@Composable
private fun MainContent(
    contentState: ContentState,
    navController: NavController,
    applyFilter: Boolean,
    filter: String,
    mainViewModelMainAppView: MainViewModelMainAppView,
    createItem: MutableState<Boolean>
) {
    when(contentState) {
        ContentState.COURSES -> ShowCourses(
            navController = navController,
            applyFilter = applyFilter,
            filter = filter,
            size = 1f,
            mainViewModelMainAppView = mainViewModelMainAppView,
            contentState = contentState,
            createItem = createItem
        )
        ContentState.ALL -> {
            ShowCourses(
                navController = navController,
                applyFilter = applyFilter,
                filter = filter,
                size = 0.5f,
                mainViewModelMainAppView = mainViewModelMainAppView,
                contentState = contentState,
                createItem = createItem
            )
            ShowClasses(
                applyFilter = applyFilter,
                navController = navController,
                filter = filter,
                mainViewModelMainAppView = mainViewModelMainAppView,
                contentState = contentState,
                createItem = createItem
            )
        }
        ContentState.CLASSES -> ShowClasses(
            applyFilter = applyFilter,
            navController = navController,
            filter = filter,
            mainViewModelMainAppView = mainViewModelMainAppView,
            contentState = contentState,
            createItem = createItem
        )
    }
}


@Composable
private fun SendId(
    id: String
) {
    val context = LocalContext.current

    val senId = Intent(Intent.ACTION_SEND, Uri.parse(id))
    senId.setData(Uri.parse(id))
    senId.setType("text/plain")

    senId.putExtra(Intent.EXTRA_TEXT,id)
    context.startActivity(senId)

}
