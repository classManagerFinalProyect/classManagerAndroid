package com.example.classmanagerandroid.Screens.MainAppActivity.Components.MainBody

import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.classmanagerandroid.data.local.CurrentUser
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.R
import com.example.classmanagerandroid.Screens.ScreenItems.Cards.LongHorizontalCard
import com.example.classmanagerandroid.data.network.AccessToDataBase
import com.example.classmanagerandroid.Screens.MainAppActivity.MainViewModelMainAppView

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowCourses(
    mainViewModelMainAppView: MainViewModelMainAppView,
    size: Float,
    navController: NavController,
    applyFilter: Boolean,
    filter: String,
    contentState: ContentState,
    createItem: MutableState<Boolean>
){
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(size)
            .border(BorderStroke(2.dp, MaterialTheme.colors.onSurface)),
        content = {
            stickyHeader {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.background)
                        .clickable {
                            if (createItem.value) {
                                createItem.value = false
                            } else {
                                if (contentState == ContentState.ALL)
                                    mainViewModelMainAppView.updateContentState(
                                        newValue = ContentState.COURSES
                                    )
                                else
                                    mainViewModelMainAppView.updateContentState(
                                        newValue = ContentState.ALL
                                    )
                            }
                        }
                        .padding(16.dp),
                    content = {
                        Text(
                            text = "Mis cursos",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            style = MaterialTheme.typography.caption,
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        if (contentState == ContentState.ALL)
                            Text(text = "+", fontSize = 18.sp, color = MaterialTheme.colors.primary)
                        else
                            Text(text = "-", fontSize = 18.sp, color = MaterialTheme.colors.primary)
                    }
                )
            }

            item {
                if(CurrentUser.myCourses.size == 0) {
                    Column(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        content = {
                            TextButton(
                                onClick = {
                                    navController.navigate(Destinations.CreateCourse.route)
                                },
                                content = {
                                    Text(text = "Agregar nuevo curso", fontSize = 20.sp)
                                }
                            )
                        }
                    )
                }
            }

            itemsIndexed(CurrentUser.myCourses) { _, item ->
                val gsReference = AccessToDataBase.storageInstance.getReferenceFromUrl(item.img)
                val courseImg = remember { mutableStateOf<Uri?>(null)}
                gsReference.downloadUrl.addOnSuccessListener{ courseImg.value = it }

                if (applyFilter) {
                    if (item.name.lowercase().contains(filter)) {
                        LongHorizontalCard(
                            title = item.name,
                            subtitle = "${item.classes.size}",
                            onClick = {
                                if (createItem.value){
                                    createItem.value = false
                                }
                                else{
                                    navController.navigate("${Destinations.Course.route}/${item.id}")
                                    mainViewModelMainAppView.clearSearchBar()
                                }
                            },
                            subtitleIconPainter = painterResource(id = R.drawable.class_white),
                            imagePainter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(courseImg.value)
                                    .scale(Scale.FILL)
                                    .transformations(CircleCropTransformation())
                                    .build()
                            )
                        )
                    }
                }
            }
        }
    )
}