package com.helic.heybooks.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.helic.heybooks.ui.theme.RedColor

@Composable
fun AddCategoryTag(category: String, modifier: Modifier, onDelete: (category: String) -> Unit) {
    val color = RedColor
    AddChipView(
        category = category,
        color = color,
        modifier = modifier,
        onDelete = { onDelete(category) })
}

@Composable
fun AddChipView(
    category: String,
    color: Color,
    modifier: Modifier,
    onDelete: (category: String) -> Unit
) {
    Box(
        modifier = modifier
            .wrapContentWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(.08f))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = category, modifier = Modifier.padding(12.dp, 6.dp, 12.dp, 6.dp),
                style = MaterialTheme.typography.button,
                color = color
            )
            IconButton(onClick = { onDelete(category) }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "",
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
        }


    }
}