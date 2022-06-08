package com.example.classmanagerandroid.Screens.MainAppActivity.Components.BottomBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.classmanagerandroid.Screens.MainAppActivity.Components.MainBody.ContentState


sealed class BottomBarNavigation(
    val route: String,
    val icon : ImageVector,
    val title: String,
    val contentState: ContentState
) {


    object MyCourses: BottomBarNavigation(route = "courses", icon = Icons.Default.Home, title = "Cursos", contentState = ContentState.COURSES)
    object MyClasses: BottomBarNavigation(route = "clases", icon = Icons.Default.Star , title = "Clases", contentState = ContentState.CLASSES)
}
