package com.example.classmanagerandroid.Views.Class.ViewMembersClass

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classmanagerandroid.Views.Course.ViewMembers.*
import com.example.classmanagerandroid.Views.Course.ViewMembers.SearchWidgetState
import com.example.classmanagerandroid.data.remote.appUser
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
    val (selectedUser, onValueChangeSelectedUser) = remember { mutableStateOf(appUser("","","", arrayListOf(), arrayListOf(),"",""))}
    var getClass by remember { mutableStateOf(true) }
    val (changeRol, onValueChangeRol) = remember { mutableStateOf(false)}
    var (isRefreshing, onValueChangeIsRefreshing) = remember { mutableStateOf(false) }
    val searchWidgetState by mainViewModelViewMembersClass.searchWidgetState
    val searchTextState by mainViewModelViewMembersClass.searchTextState
    val aplicateFilter = remember { mutableStateOf(true) }
    var filter = ""

    LaunchedEffect(getClass) {
        if (getClass) {
            mainViewModelViewMembersClass.getSelectedClass(idOfClass)
            getClass = false
        }
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
                    MainAppBar(
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
                        mainViewModelViewMembersClass = mainViewModelViewMembersClass,
                    )
                },
                content = {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(),
                        content = {
                            var allUserAppUser: MutableList<appUser> = arrayListOf()
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
private fun MainAppBar(
    searchWidgetState: SearchWidgetState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit,
    navController: NavController,
    mainViewModelViewMembersClass: MainViewModelViewMembersClass,

    ) {
    when (searchWidgetState) {
        SearchWidgetState.CLOSED -> {
            defaultAppBar(
                onSearchClicked = onSearchTriggered,
                navController = navController,
                mainViewModelViewMembersClass = mainViewModelViewMembersClass,
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
