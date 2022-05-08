package com.example.classmanagerandroid.Screens.ScreenItems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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


@Composable
fun bigDropDownMenuWithAction(
    initialValue: String,
    suggestions: MutableList<String>,
    onClick: () -> Unit
) {

    Spacer(modifier = Modifier.padding(4.dp))
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(initialValue) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var editItem = remember{ mutableStateOf(false) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

        Column(
            modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(start = 40.dp, end = 40.dp)),
            content = {
                OutlinedTextField(
                    value = selectedText,
                    onValueChange = { selectedText = it },
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            textFieldSize = coordinates.size.toSize()
                        },
                    trailingIcon = {
                        Icon(
                            imageVector =  icon,
                            contentDescription = "arrowExpanded",
                            modifier = Modifier
                                .clickable { expanded = !expanded }
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit ${initialValue}",
                            modifier = Modifier
                                .clickable {
                                    editItem.value = true
                                    onClick()
                                }
                        )
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current) { textFieldSize.width.toDp() }),
                    content = {
                        suggestions.forEach { label ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedText = label
                                    expanded = false
                                },
                                content = {
                                    Text(text = label)

                                }
                            )
                        }
                    }
                )
            }
        )
}