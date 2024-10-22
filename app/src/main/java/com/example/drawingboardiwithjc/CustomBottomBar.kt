package com.example.drawingboardiwithjc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.background(colorResource(id = R.color.pastelPeach)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // IconButton for Brush and Background Change
        IconButton(
            onClick = onBrushSizeChange,
            modifier = Modifier.size(56.dp) // Set the size for the IconButton
        ) {
            Icon(
                painter = painterResource(R.drawable.brush),
                contentDescription = "Brush Size",
                tint = Color.Unspecified,
                modifier = Modifier.size(32.dp) // Increase icon size
            )
        }

        // IconButton for Undo
        IconButton(
            onClick = onUndo,
            modifier = Modifier.size(56.dp) // Set the size for the IconButton
        ) {
            Icon(
                painter = painterResource(R.drawable.undo),
                contentDescription = "Undo",
                tint = Color.Unspecified,
                modifier = Modifier.size(32.dp) // Increase icon size
            )
        }

        // IconButton for Redo
        IconButton(
            onClick = onRedo,
            modifier = Modifier.size(56.dp) // Set the size for the IconButton
        ) {
            Icon(
                painter = painterResource(R.drawable.undo),
                contentDescription = "Redo",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(32.dp) // Increase icon size
                    .graphicsLayer(rotationY = 180f) // Rotate for redo
            )
        }

        // IconButton for Clear Canvas
        IconButton(
            onClick = onClearCanvas,
            modifier = Modifier.size(56.dp) // Set the size for the IconButton
        ) {
            Icon(
                painter = painterResource(R.drawable.clear),
                contentDescription = "Clear",
                tint = Color.Unspecified,
                modifier = Modifier.size(32.dp) // Increase icon size
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
        modifier = Modifier.fillMaxWidth()
    )
}
