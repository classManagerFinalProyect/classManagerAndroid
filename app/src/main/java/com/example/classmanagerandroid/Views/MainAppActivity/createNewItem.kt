package me.saine.android.Views.MainAppActivity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.R
@Composable
fun createNewItem(
    navController: NavController,
    onValueChangeCreateItem: (Boolean) -> Unit
) {
    val miniFabSize = 40.dp

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        content = {
            Text(
                text = "Crear curso",
                modifier = Modifier
                    .clickable { navController.navigate(Destinations.CreateCourse.route) }
            )
            Spacer(modifier = Modifier.padding(5.dp))
            FloatingActionButton(
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier.size(miniFabSize),
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.school_white),
                        contentDescription = "Class",
                        tint = Color.White,
                        modifier = Modifier.size(25.dp)
                    )
                },
                onClick = {
                    onValueChangeCreateItem(false)
                    navController.navigate(Destinations.CreateCourse.route)
                }
            )
        }
    )

    Spacer(modifier = Modifier.padding(10.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        content = {
            Text(
                text = "Crear clase",
                modifier = Modifier
                    .clickable { navController.navigate(Destinations.CreateClass.route) }
            )
            Spacer(modifier = Modifier.padding(5.dp))
            FloatingActionButton(
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier.size(miniFabSize),
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.class_white),
                        contentDescription = "Class",
                        tint = Color.White,
                        modifier = Modifier.size(25.dp)
                    )


                },
                onClick = {
                    navController.navigate(Destinations.CreateClass.route)
                    onValueChangeCreateItem(false)
                }
            )
        }
    )
    Spacer(modifier = Modifier.padding(10.dp))
}