package com.example.classmanagerandroid.Screens.CreateCourse.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.classmanagerandroid.Screens.CreateCourse.MainViewModelCreateCourse

@Composable
fun AddCourse(
    mainViewModelCreateCourse: MainViewModelCreateCourse,
    onValueChangeAddClasses: (Boolean) -> Unit
) {
    Dialog(

        onDismissRequest = {
            onValueChangeAddClasses(false)
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .background(Color.White),
                content = {
                    itemsIndexed(mainViewModelCreateCourse.allListItems) { _, item ->
                        val isSelectedItem = remember { mutableStateOf(item.isSelected) }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if(item.isSelected) {
                                        item.isSelected = false
                                        isSelectedItem.value = false
                                    }
                                    else{
                                        item.isSelected = true
                                        isSelectedItem.value = true
                                    }
                                }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = item.title)
                            if(isSelectedItem.value) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.Green,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    item {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            content = {
                                Text(text = "Aceptar")
                            },
                            onClick = {
                                onValueChangeAddClasses(false)
                            }
                        )
                    }
                }
            )
        }
    )
}