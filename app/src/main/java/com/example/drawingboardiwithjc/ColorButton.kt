package com.example.drawingboardiwithjc


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ColorButton(
    buttonNumber: Int,
    isSelected: Boolean,
    colors: List<Color>,
    onColorSelected: (Int) -> Unit
) {
    // Icon changes based on the selection state
    val icon = if (isSelected) R.drawable.baseline_circle_24 else R.drawable.outline_circle_24

    IconButton(onClick = {
        onColorSelected(buttonNumber)
    }) {
        // Use modulo to ensure the index stays within the bounds of the list
        val color = colors[buttonNumber % colors.size]

        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Color Button $buttonNumber",
            tint = color,  // Dynamically get color from list
            modifier = Modifier.padding(8.dp)
        )
    }
}