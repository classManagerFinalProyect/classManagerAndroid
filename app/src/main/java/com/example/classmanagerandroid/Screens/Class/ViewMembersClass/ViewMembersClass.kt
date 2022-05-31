package com.example.classmanagerandroid.Screens.Class.ViewMembersClass

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
import com.example.classmanagerandroid.Screens.Class.ViewMembersClass.Components.mainAppBar
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.SearchBar.SearchWidgetState
import com.example.classmanagerandroid.data.remote.AppUser
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainViewMembersClass(
    mainViewModelViewMembersClass: MainViewModelViewMembersClass,
    navController: NavController,
    idOfClass: String
) {
    val (selectedUser, onValueChangeSelectedUser) = remember { mutableStateOf(AppUser("","","", arrayListOf(), arrayListOf(),"","",""))}
    var getClass by remember { mutableStateOf(true) }
    val (changeRol, onValueChangeRol) = remember { mutableStateOf(false)}
    var (isRefreshing, onValueChangeIsRefreshing) = remember { mutableStateOf(false) }
    val searchWidgetState by mainViewModelViewMembersClass.searchWidgetState
    val searchTextState by mainViewModelViewMembersClass.searchTextState
    val aplicateFilter = remember { mutableStateOf(true) }
    var filter = ""
    var sendEmail = remember { mutableStateOf(false) }

    LaunchedEffect(getClass) {
        if (getClass) {
            mainViewModelViewMembersClass.getSelectedClass(idOfClass)
            getClass = false
        }
    }

    if(sendEmail.value) {
        sendEmail(
            selectedUser = selectedUser
        )
        sendEmail.value = false
    }

    if(changeRol) {
        changeRolClass(
            mainViewModelViewMembersClass = mainViewModelViewMembersClass,
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
                    mainAppBar(
                        searchWidgetState = searchWidgetState,
                        searchTextState = searchTextState,
                        onTextChange = {
                            mainViewModelViewMembersClass.updateSearchTextState(newValue = it)
                            aplicateFilter.value = false
                            filter = it.lowercase()
                            aplicateFilter.value = true
                        },
                        onCloseClicked = {
                            mainViewModelViewMembersClass.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                        },
                        onSearchClicked = {
                            /*aplicateFilter.value = false
                            filter = it.lowercase()
                            aplicateFilter.value = true*/
                        },
                        onSearchTriggered = {
                            mainViewModelViewMembersClass.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                        },
                        navController = navController,
                    )
                },
                content = {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(),
                        content = {
                            var allUserAppUser: MutableList<AppUser> = arrayListOf()
                            mainViewModelViewMembersClass.selectedUsers.forEach{
                                allUserAppUser.add(it)
                            }
                            var grouped = allUserAppUser.groupBy { it.name.substring(0, 1) }.toSortedMap()

                            if(aplicateFilter.value) {
                                grouped.forEach { header, items ->
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
                                                                text = mainViewModelViewMembersClass.getRolOfUserById(item.id).rol,
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
            )
        }
    )
}

@Composable
private fun sendEmail(
    selectedUser: AppUser
) {
    val context = LocalContext.current

    var email: Intent = Intent(Intent.ACTION_SEND, Uri.parse(CurrentUser.currentUser.email))
    email.setData(Uri.parse(CurrentUser.currentUser.email))
    email.setType("text/plain")
    email.putExtra(Intent.EXTRA_EMAIL,"Email test")
    email.putExtra(Intent.EXTRA_SUBJECT,"Question email")
    email.putExtra(Intent.EXTRA_TEXT,"Write your email to ${selectedUser.email}")

    context.startActivity(email)
}



