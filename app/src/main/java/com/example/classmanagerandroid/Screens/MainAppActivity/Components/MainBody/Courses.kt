package com.example.classmanagerandroid.Screens.MainAppActivity.Components.MainBody

import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.example.classmanagerandroid.Classes.CurrentUser
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.R
import com.example.classmanagerandroid.Screens.ScreenItems.Cards.longHorizontalCard
import com.example.classmanagerandroid.data.network.AccessToDataBase
import me.saine.android.Views.MainAppActivity.MainViewModelMainAppView

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun showCourses(
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
            .border(BorderStroke(2.dp, Color.LightGray)),
        content = {
            stickyHeader {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (createItem.value){
                                createItem.value = false
                            }
                            else{
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
                        .background(Color.White)
                        .padding(16.dp),
                    content = {
                        Text(
                            text = "Mis cursos",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            color = Color.Black,
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

            itemsIndexed(CurrentUser.myCourses) { index, item ->
                val gsReference = AccessToDataBase.storageInstance.getReferenceFromUrl(item.img)
                val courseImg = remember { mutableStateOf<Uri?>(null)}
                gsReference.downloadUrl.addOnSuccessListener{ courseImg.value = it }

                if (applyFilter) {
                    if (item.name.lowercase().contains(filter)) {
                        longHorizontalCard(
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