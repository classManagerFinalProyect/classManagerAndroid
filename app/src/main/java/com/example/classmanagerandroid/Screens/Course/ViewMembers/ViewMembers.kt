package com.example.classmanagerandroid.Screens.Course.ViewMembers

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classmanagerandroid.data.local.CurrentUser
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.SearchBar.SearchWidgetState
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.SearchAppBar
import com.example.classmanagerandroid.data.remote.AppUser
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainViewMembers(
    navController: NavController,
    mainViewModelViewMembers: MainViewModelViewMembers,
    idCourse: String
) {
    val (selectedUser, onValueChangeSelectedUser) = remember { mutableStateOf(AppUser("","","", arrayListOf(), arrayListOf(),"","",""))}
    val (changeRol, onValueChangeRol) = remember { mutableStateOf(false)}
    val searchWidgetState by mainViewModelViewMembers.searchWidgetState
    val searchTextState by mainViewModelViewMembers.searchTextState
    var getCourse by remember { mutableStateOf(true) }
    val applicativeFilter = remember { mutableStateOf(true) }
    var filter = ""
    var (isRefreshing, onValueChangeIsRefreshing) = remember { mutableStateOf(false) }
    val sendEmail = remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }

    if(sendEmail.value) {
        sendEmail(
            selectedUser = selectedUser
        )
        sendEmail.value = false
    }

    LaunchedEffect(getCourse) {
        if (getCourse) {
            mainViewModelViewMembers.getSelectedCourse(
                idCourse = idCourse,
                onFinish = {
                    showContent = true
                }
            )
            getCourse = false
        }
    }

    if(changeRol) {
        changeRol(
            mainViewModelViewMembers = mainViewModelViewMembers,
            onValueChangeRol = onValueChangeRol,
            selectedUser = selectedUser,
            onValueChangeIsRefreshing = onValueChangeIsRefreshing
        )
    }


    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(800L)
            onValueChangeIsRefreshing(false)
        }
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
                            mainViewModelViewMembers.updateSearchTextState(newValue = it)
                            applicativeFilter.value = false
                            filter = it.lowercase()
                            applicativeFilter.value = true
                        },
                        onCloseClicked = {
                            mainViewModelViewMembers.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                        },
                        onSearchClicked = {

                        },
                        onSearchTriggered = {
                            mainViewModelViewMembers.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                        },
                        navController = navController,
                    )
                },
                content = {
                    if(showContent) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth(),
                            content = {
                                val allUserAppUser: MutableList<AppUser> = arrayListOf()
                                mainViewModelViewMembers.selectedUsers.forEach{
                                    allUserAppUser.add(it)
                                }
                                val grouped = allUserAppUser.groupBy { it.name.substring(0, 1) }.toSortedMap()

                                if(applicativeFilter.value) {
                                    grouped.forEach { (header, items) ->
                                        var writeHeader = true
                                        items.forEach {

                                            if (it.name.lowercase().contains(filter)) {
                                                if(writeHeader) {
                                                    stickyHeader(
                                                        content = {
                                                            Text(
                                                                text = header,
                                                                style = MaterialTheme.typography.caption,
                                                                modifier = Modifier
                                                                    .background(Color.LightGray)
                                                                    .padding(16.dp)
                                                                    .fillMaxWidth()
                                                            )
                                                        }
                                                    )
                                                    itemsIndexed(items) { index: Int, item ->
                                                        Row(
                                                            horizontalArrangement = Arrangement.Start,
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .clickable {
                                                                    onValueChangeSelectedUser(item)
                                                                    onValueChangeRol(true)
                                                                },
                                                            content = {
                                                                Text(
                                                                    text = item.name,
                                                                    modifier = Modifier
                                                                        .weight(1.5f)
                                                                        .padding(16.dp)
                                                                        .wrapContentWidth(Alignment.Start),
                                                                    style = MaterialTheme.typography.subtitle1
                                                                )
                                                                Text(
                                                                    text = mainViewModelViewMembers.getRolOfUserById(item.id).rol,
                                                                    fontSize = 12.sp,
                                                                    modifier = Modifier
                                                                        .weight(0.5f)
                                                                        .padding(16.dp)
                                                                        .wrapContentWidth(Alignment.End),
                                                                    style = MaterialTheme.typography.subtitle1
                                                                )
                                                                IconButton(
                                                                    onClick = {
                                                                        onValueChangeSelectedUser(item)
                                                                        sendEmail.value = true
                                                                    },
                                                                    content = {
                                                                        Icon(
                                                                            imageVector = Icons.Default.Email,
                                                                            contentDescription = "Send Email"
                                                                        )
                                                                    }
                                                                )
                                                            }
                                                        )
                                                    }
                                                    writeHeader = false
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        )
                    }
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
) {
    when (searchWidgetState) {
        SearchWidgetState.CLOSED -> {
            defaultAppBar(
                onSearchClicked = onSearchTriggered,
                navController = navController,
            )
        }
        SearchWidgetState.OPENED -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = onTextChange,
                onCloseClicked = onCloseClicked,
                onSearchClicked = onSearchClicked
            )
        }
    }
}

@Composable
private fun sendEmail(
    selectedUser: AppUser
) {
    val context = LocalContext.current

    val email = Intent(Intent.ACTION_SEND, Uri.parse(CurrentUser.currentUser.email))
    email.setData(Uri.parse(CurrentUser.currentUser.email))
    email.setType("text/plain")
    email.putExtra(Intent.EXTRA_EMAIL,"Email test")
    email.putExtra(Intent.EXTRA_SUBJECT,"Question email")
    email.putExtra(Intent.EXTRA_TEXT,"Write your email to ${selectedUser.email}")

    context.startActivity(email)
}



