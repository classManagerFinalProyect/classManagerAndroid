package com.example.classmanagerandroid.Screens.Course.Event

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.classmanagerandroid.data.remote.Class

@Composable
fun bigSelectedDropDownMenuClassItem(
    label: String,
    suggestions: List<Class>,
    onValueChangeTextSelectedItem: (Class) -> Unit
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
            content = {
                OutlinedTextField(
                    value = selectedText,
                    onValueChange = { selectedText = it },
                    enabled = false,
                    label =  { Text(text = label)},
                    modifier = Modifier
                        .padding(PaddingValues(start = 40.dp, end = 40.dp))
                        .onGloballyPositioned { coordinates ->
                            textfieldSize = coordinates.size.toSize()
                        },
                    trailingIcon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = "arrowExpanded",
                            modifier = Modifier.clickable { expanded = !expanded },
                            tint = MaterialTheme.colors.secondary
                        )
                    }
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