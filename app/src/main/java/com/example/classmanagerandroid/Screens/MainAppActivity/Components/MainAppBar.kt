package com.example.classmanagerandroid.Screens.MainAppActivity.Components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.defaultTopBar
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.searchAppBar
import kotlinx.coroutines.launch
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.SearchBar.SearchWidgetState

@Composable
fun mainAppBar(
    searchWidgetState: SearchWidgetState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit,
    scaffoldState: ScaffoldState
) {
    val scope = rememberCoroutineScope()

    when (searchWidgetState) {
        SearchWidgetState.CLOSED -> {
            defaultTopBar(
                title = "Main App activity",
                navigationContent = {
                    IconButton(
                        onClick = {
                            scope.launch { scaffoldState.drawerState.open() }
                        },
                        content = {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    )
                },
                actionsContent = {
                    IconButton(
                        onClick = { onSearchTriggered() },
                        content = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search Icon",
                                tint = Color.White
                            )
                        }
                    )
                }
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