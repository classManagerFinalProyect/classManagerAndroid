package com.example.classmanagerandroid.Screens.Course.Event


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classmanagerandroid.data.remote.Event

@Composable
fun LongItemEvent(
    event: Event,
    onClick: () -> Unit
) {
    Card(
        backgroundColor = Color.Red,
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .width(50.dp)
            .height(160.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
        content = {
            Button(
                onClick = {
                    onClick()
                },
                contentPadding = PaddingValues(10.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color.Black
                ),
                content = {
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        content = {
                            Box(
                                modifier = Modifier.height(30.dp),
                                content = {
                                    Text(
                                        text = event.name,
                                        fontSize = 13.sp,
                                        modifier = Modifier.fillMaxSize(),
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.padding(2.dp))
                            Box(
                                modifier = Modifier.height(25.dp),
                                content = {
                                    Text(
                                        text = "Día: ${event.date}",
                                        fontSize = 10.sp,
                                        modifier = Modifier.fillMaxSize(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            )
                            Box(
                                modifier = Modifier.height(25.dp),
                                content = {
                                    Text(
                                        text = "Start:${event.initialTime}" ,
                                        fontSize = 10.sp,
                                        modifier = Modifier.fillMaxSize(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            )
                            Box(
                                modifier = Modifier.height(25.dp),
                                content = {
                                    Text(
                                        text = "Finish:${event.finalTime}",
                                        fontSize = 10.sp,
                                        modifier = Modifier.fillMaxSize(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}