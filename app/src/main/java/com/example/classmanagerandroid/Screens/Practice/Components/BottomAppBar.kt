package com.example.classmanagerandroid.Screens.Practice.Components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.classmanagerandroid.data.local.CurrentUser
import com.example.classmanagerandroid.Screens.Practice.MainViewModelPractice
import com.example.classmanagerandroid.data.local.Message
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun bottomAppBar(
    value: MutableState<String>,
    mainViewModelPractice: MainViewModelPractice,
    refresh: MutableState<Boolean>
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.Transparent),
        elevation = AppBarDefaults.BottomAppBarElevation,
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                content = {

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        value = value.value,
                        shape = RoundedCornerShape(30.dp),
                        onValueChange = {
                            value.value = it
                        },
                        placeholder = {
                            Text(
                                modifier = Modifier
                                    .alpha(ContentAlpha.medium),
                                text = "Message",
                                color = Color.Black
                            )
                        },
                        textStyle = TextStyle(
                            fontSize = MaterialTheme.typography.subtitle1.fontSize,
                            color = Color.Black
                        ),
                        singleLine = false,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Send
                        ),
                        keyboardActions = KeyboardActions(
                            //Darle al botón de enviar del teclado
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            cursorColor = Color.Black
                        )
                    )

                    FloatingActionButton(
                        backgroundColor = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .size(50.dp),
                        content = {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Enviar mensaje",
                                tint = Color.White
                            )
                        },
                        onClick = {
                            mainViewModelPractice.chat.conversation.add(
                                Message(
                                    message = value.value,
                                    sentBy = CurrentUser.currentUser,
                                    sentOn = "${LocalDate.now().monthValue}-${LocalDate.now().dayOfMonth}-${LocalDate.now().year}"
                                )

                            )
                            mainViewModelPractice.updateChat(
                                onFinished = {
                                    refresh.value = true
                                    refresh.value = false
                                }
                            )
                            value.value = ""
                        }
                    )
                }
            )
        }
    )
}