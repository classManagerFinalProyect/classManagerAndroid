package com.example.classmanagerandroid.Screens.MainAppActivity.Components

import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.classmanagerandroid.Screens.MainAppActivity.Components.BottomBar.BottomBarNavigation
import com.example.classmanagerandroid.Screens.MainAppActivity.MainViewModelMainAppView

@Composable
fun BottomBar(
    mainAppView: MainViewModelMainAppView
) {
    val items = listOf(
        BottomBarNavigation.MyCourses,
        BottomBarNavigation.MyClasses,
    )

    BottomNavigation(
        backgroundColor = Color.Black,
        contentColor = Color.White,
        content = {
            items.forEach { item ->
                BottomNavigationItem(
                    icon = { Icon(item.icon, contentDescription = item.title) },
                    modifier = Modifier.background(MaterialTheme.colors.background),
                    label = { Text(text = item.title) },
                    selectedContentColor = MaterialTheme.colors.primary,
                    unselectedContentColor = MaterialTheme.colors.primary.copy(0.9f),
                    alwaysShowLabel = true,
                    selected = false,
                    onClick = {
                        mainAppView.updateContentState(item.contentState)
                        /*
                        navController.navigate("") {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                        }
                        */
                    }
                )
            }
        }
    )
}