package com.example.drawingboardiwithjc

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorPicker(onColorSelected: (Color) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pick a color", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            // List of available colors
            listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Black).forEach { color ->
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(color, RoundedCornerShape(50))
                        .padding(8.dp)
                        .border(2.dp, Color.Gray, RoundedCornerShape(50))
                        .clickable { onColorSelected(color) }
                )
            }
        }
    }
}
