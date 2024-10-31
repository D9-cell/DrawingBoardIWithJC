package com.example.drawingboardiwithjc

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrushBottomSheet(
    onDismiss: () -> Unit,
    drawingView: DrawingView?,
    colors: List<Color>,
    backgroundColors: List<Color>,
    currentBrushSize: Float,
    currentColorIndex: Int, // Keep track of the current brush color index
    currentBackgroundColorIndex: Int, // Keep track of the current background color index
    changeBrushSize: (Float) -> Unit,
    changeBrushColor: (Color) -> Unit,
    changeBackgroundColor: (Color) -> Unit,
    updateCurrentColorIndex: (Int) -> Unit, // New parameter to update the color index
    updateCurrentBackgroundColorIndex: (Int) -> Unit // New parameter to update the background color index
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Brush Size Selector
            Text(
                text = "Select Brush Size",
                fontSize = 18.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(start = 9.dp, top = 14.dp)
            )
            Slider(
                value = currentBrushSize,
                onValueChange = { size -> changeBrushSize(size) },
                valueRange = 2f..18f,
                steps = 16 // This allows the slider to jump by integer values
            )
            Spacer(modifier = Modifier.height(9.dp))


            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)) // Apply rounded corners
                    .background(colorResource(id = R.color.lightGray))
                    .horizontalScroll(rememberScrollState())
            ) {
                // Brush Color Selection
                Text(
                    text = "Select Brush Color",
                    fontSize = 18.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(start = 9.dp, top = 14.dp)
                )
                colors.forEachIndexed { index, color ->
                    ColorButton(
                        buttonNumber = index,
                        isSelected = index == currentColorIndex,
                        colors = colors,
                        onColorSelected = { buttonIndex ->
                            changeBrushColor(colors[buttonIndex])
                            updateCurrentColorIndex(buttonIndex) // Update current color index
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(9.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)) // Apply rounded corners
                    .background(colorResource(id = R.color.lightGray))
                    .horizontalScroll(rememberScrollState())
            ) {
                // Background Color Selection
                Text(
                    text = "Select Background Color",
                    fontSize = 18.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(start = 9.dp, top = 14.dp)
                )
                backgroundColors.forEachIndexed { index, color ->
                    ColorButton(
                        buttonNumber = index,
                        isSelected = index == currentBackgroundColorIndex,
                        colors = backgroundColors,
                        onColorSelected = { buttonIndex ->
                            changeBackgroundColor(backgroundColors[buttonIndex])
                            updateCurrentBackgroundColorIndex(buttonIndex) // Update current background color index
                        }
                    )
                }
            }

            // Bottom bar with buttons like Undo, Redo, Clear, Brush Size, Change Background
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { drawingView?.onClickUndo() }) {
                    Icon(
                        painter = painterResource(R.drawable.undo),
                        contentDescription = "Undo",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(32.dp)
                    )
                }
                IconButton(onClick = { drawingView?.onClickRedo() }) {
                    Icon(
                        painter = painterResource(R.drawable.undo),
                        contentDescription = "Redo",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(32.dp)
                            .graphicsLayer(rotationY = 180f)
                    )
                }
                IconButton(onClick = { drawingView?.clearCanvas() }) {
                    Icon(
                        painter = painterResource(R.drawable.clear),
                        contentDescription = "Clear",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(32.dp)
                    )
                }
                // IconButton for Share the Image
                IconButton(
                    onClick = { /* Function for Share the image */ },
                    modifier = Modifier.size(56.dp) // Set the size for the IconButton
                ) {
                    Icon(
                        painter = painterResource(R.drawable.share),
                        contentDescription = "Share Drawing",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}
