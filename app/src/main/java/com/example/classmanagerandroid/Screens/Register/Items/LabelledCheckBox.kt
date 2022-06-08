package com.example.classmanagerandroid.Screens.Register.Items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LabelledCheckbox(
    labelText: String,
    isCheckedValue: Boolean,
    onValueChangeChecked: (Boolean) -> Unit,
    onClickText: () -> Unit
) {
    Row(
        modifier = Modifier.padding(start = 40.dp,8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isCheckedValue,
            onCheckedChange = { onValueChangeChecked(it) },
            enabled = true,
            colors = CheckboxDefaults.colors(Color.Blue)
        )
        Spacer(modifier = Modifier.padding(2.dp))
        Text(
            text = labelText,
            modifier = Modifier
                .clickable{
                    onClickText()
                },
            fontSize = 17.sp,
            color = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.padding(25.dp))
    }
}

@Preview
@Composable
fun Preview() {
    LabelledCheckbox(
        labelText = "Text",
        isCheckedValue = false,
        onValueChangeChecked = {},
        onClickText = {}
    )
}