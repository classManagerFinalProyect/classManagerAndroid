package com.example.classmanagerandroid.Screens.Class.ViewMembersClass

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import com.example.classmanagerandroid.Screens.Course.ViewMembers.bigSelectedDropDownMenuMembers
import com.example.classmanagerandroid.data.remote.appUser

@Composable
fun changeRolClass(
    mainViewModelViewMembersClass: MainViewModelViewMembersClass ,
    onValueChangeRol: (Boolean) -> Unit,
    selectedUser: appUser,
    onValueChangeIsRefreshing: (Boolean) -> Unit
) {
    val selectedRolUser = mainViewModelViewMembersClass.getRolOfUserById(selectedUser.id)
    val suggestion: MutableList<String> = mutableListOf("admin","profesor","padre","alumno")
    val (textSelectedItem, onValueChangeTextSelectedItem) = remember{ mutableStateOf(selectedRolUser.rol)}


    Dialog(
        onDismissRequest = {
            onValueChangeRol(false)
        },
        properties = DialogProperties(

        ),
        content = {

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.70f)
                    .background(Color.White),
                shape = RoundedCornerShape(16.dp),
                content = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize(),
                        content = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        PaddingValues(
                                            top = 5.dp,
                                            bottom = 10.dp
                                        )
                                    ),
                                content = {
                                    Image(
                                        painter = rememberAsyncImagePainter(
                                            model = Uri.parse(selectedUser.imgPath)
                                        ),
                                        contentDescription = "avatar",
                                        modifier = Modifier
                                            .size(150.dp)
                                            .clip(CircleShape)
                                            .border(
                                                2.dp,
                                                Color.Gray,
                                                CircleShape
                                            )
                                            .align(Alignment.CenterVertically),
                                        contentScale = ContentScale.Crop,

                                        )
                                }
                            )
                            Text(
                                text = "${selectedUser.name.uppercase()}",
                                modifier = Modifier
                                    .padding(PaddingValues(start = 20.dp, end = 20.dp))
                            )
                            Text(
                                text = selectedUser.email,
                                modifier = Modifier
                                    .padding(PaddingValues(start = 20.dp, end = 20.dp))
                            )

                            if(mainViewModelViewMembersClass.currentdRolUser.rol.equals("admin")) {
                                bigSelectedDropDownMenuMembers (
                                    suggestions = suggestion,
                                    onValueChangeTextSelectedItem = onValueChangeTextSelectedItem,
                                    textStartItem = textSelectedItem
                                )
                            }
                            else {
                                Text(
                                    text = "Rol: ${textSelectedItem}",
                                    modifier = Modifier
                                        .padding(PaddingValues(start = 20.dp, end = 20.dp))
                                )
                            }

                            Spacer(modifier = Modifier.padding(7.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        PaddingValues(
                                            end = 20.dp
                                        )
                                    ),
                                content = {
                                    if (mainViewModelViewMembersClass.currentdRolUser.rol.equals("admin")) {
                                        TextButton(
                                            onClick = {
                                                onValueChangeRol(false)
                                                mainViewModelViewMembersClass.deleteRolUser(
                                                    selectedUser = selectedUser,
                                                    onFinish = {
                                                        onValueChangeIsRefreshing(true)
                                                    }
                                                )
                                            },
                                            content = {
                                                Text(text = "Eliminar", color = Color.Red)
                                            }
                                        )
                                        TextButton(
                                            onClick = {
                                                mainViewModelViewMembersClass.updateRol(
                                                    idOfuser = selectedUser.id,
                                                    newRol = textSelectedItem,
                                                    onFinish = {
                                                        onValueChangeIsRefreshing(true)
                                                    }
                                                )
                                                onValueChangeRol(false)
                                            },
                                            content = {
                                                Text(text = "Save")
                                            }
                                        )
                                    }
                                    else {
                                        TextButton(
                                            onClick = {
                                                onValueChangeRol(false)
                                            },
                                            content = {
                                                Text(text = "Aceptar")
                                            }
                                        )
                                    }

                                }
                            )

                        }
                    )
                }
            )
        }
    )
}