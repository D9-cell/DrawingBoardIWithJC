package com.example.drawingboardiwithjc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomBottomBar(
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onBrushSizeChange: () -> Unit,
    onClearCanvas: () -> Unit,
    onColorChange: () -> Unit, // Callback for color picker
    onBackgroundColorChange: () -> Unit, // Callback for changing background color
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(colorResource(id = R.color.pastelPeach))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // FloatingActionButton for Undo
        FloatingActionButton(
            onClick = onUndo,
            modifier = Modifier.size(40.dp), // Default FAB size
            containerColor = Color.Unspecified
        ) {
            Icon(
                painter = painterResource(R.drawable.undo),
                contentDescription = "Undo",
                tint = Color.Unspecified
            )
        }

        // FloatingActionButton for Redo
        FloatingActionButton(
            onClick = onRedo,
            modifier = Modifier.size(40.dp), // Default FAB size
            containerColor = Color.Unspecified
        ) {
            Icon(
                painter = painterResource(R.drawable.undo),
                contentDescription = "Redo",
                tint = Color.Unspecified,
                modifier = Modifier.graphicsLayer(rotationY = 180f)
            )
        }

        // FloatingActionButton for Brush Size Change
        FloatingActionButton(
            onClick = onBrushSizeChange,
            modifier = Modifier.size(40.dp), // Default FAB size
            containerColor = Color.Unspecified
        ) {
            Icon(
                painter = painterResource(R.drawable.brush),
                contentDescription = "Brush Size",
                tint = Color.Unspecified
            )
        }

        // FloatingActionButton for Clear Canvas
        FloatingActionButton(
            onClick = onClearCanvas,
            modifier = Modifier.size(40.dp), // Default FAB size
            containerColor = Color.Unspecified
        ) {
            Icon(
                painter = painterResource(R.drawable.clear),
                contentDescription = "Clear",
                tint = Color.Unspecified
            )
        }

        // FloatingActionButton for Color Change
        FloatingActionButton(
            onClick = onColorChange,
            modifier = Modifier.size(40.dp), // Default FAB size
            containerColor = Color.Unspecified
        ) {
            Icon(
                painter = painterResource(R.drawable.color),
                contentDescription = "Choose Color",
                tint = Color.Unspecified
            )
        }

        // FloatingActionButton for Background Color Change
        FloatingActionButton(
            onClick = onBackgroundColorChange,
            modifier = Modifier.size(40.dp), // Default FAB size
            containerColor = Color.Unspecified
        ) {
            Icon(
                painter = painterResource(R.drawable.background), // You can use a different icon
                contentDescription = "Change Background Color",
                tint = Color.Unspecified
            )
        }
    }
}

// Preview Function
@Preview(showBackground = true)
@Composable
fun CustomBottomBarPreview() {
    CustomBottomBar(
        onUndo = {},
        onRedo = {},
        onBrushSizeChange = {},
        onClearCanvas = {},
        onColorChange = {},
        onBackgroundColorChange = {},
        modifier = Modifier.fillMaxWidth()
    )
}
