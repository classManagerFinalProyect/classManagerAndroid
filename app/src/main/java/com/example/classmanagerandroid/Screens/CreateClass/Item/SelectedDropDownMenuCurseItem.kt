package com.example.classmanagerandroid.Screens.CreateClass

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.classmanagerandroid.data.remote.Course


@Composable
fun selectedDropDownMenuCurseItem(
    textOfRow: String,
    suggestions: List<Course>,
    onValueChangeTextSelectedItem: (Course) -> Unit
) {
    Spacer(modifier = Modifier.padding(4.dp))
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Sin asignar")}
    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingValues(start = 40.dp, end = 40.dp)),
            content = {
                OutlinedTextField(
                    value = selectedText,
                    onValueChange = { selectedText = it },
                    enabled = false,
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            textfieldSize = coordinates.size.toSize()
                        }
                        .fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = "arrowExpanded",
                            modifier = Modifier.clickable { expanded = !expanded },
                            tint = MaterialTheme.colors.secondary
                        )
                    },
                    label = { Text(text = "Curso asignado")}
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current) { textfieldSize.width.toDp() }),
                    content = {
                        suggestions.forEach { label ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedText = label.name
                                    onValueChangeTextSelectedItem(label)
                                    expanded = false
                                },
                                content = {
                                    Text(text = label.name)
                                }
                            )
                        }
                    }
                )
            }
        )



}