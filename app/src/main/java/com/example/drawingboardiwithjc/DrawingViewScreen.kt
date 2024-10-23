package com.example.drawingboardiwithjc

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawingViewScreen() {
    var selectedColor by remember { mutableStateOf(Color.Black) }
    var currentColorIndex by remember { mutableIntStateOf(0) }
    var currentBrushSize by remember { mutableFloatStateOf(3f) }
    var backgroundColor by remember { mutableStateOf(Color.White) }
    var currentBackgroundColorIndex by remember { mutableIntStateOf(0) }
    var showBrushBottomSheet by remember { mutableStateOf(false) } // State to toggle bottom sheet

    // Brush and background colors list
    val colors = listOf(
        colorResource(id = R.color.chalkWhite),
        colorResource(id = R.color.chalkPastelPink),
        colorResource(id = R.color.chalkPastelBlue),
        colorResource(id = R.color.chalkPastelGreen),
        colorResource(id = R.color.chalkPastelYellow),
        colorResource(id = R.color.chalkLavender),
        colorResource(id = R.color.chalkLightCoral),
        colorResource(id = R.color.chalkPeach),
    )
    val backgroundColors = listOf(
        colorResource(id = R.color.white),
        colorResource(id = R.color.charcoal),
        colorResource(id = R.color.slateGray),
        colorResource(id = R.color.darkGreen),
        colorResource(id = R.color.midnightBlue),
        colorResource(id = R.color.darkOliveGreen),
        colorResource(id = R.color.graphite),
        colorResource(id = R.color.pineGreen)
    )

    val drawingView = remember { mutableStateOf<DrawingView?>(null) }

    // Function to change brush size
    fun changeBrushSize(size: Float) {
        currentBrushSize = size
        drawingView.value?.setSizeForBrush(currentBrushSize)
    }

    // Function to change brush Color
    fun changeBrushColor(color: Color) {
        selectedColor = color
        drawingView.value?.setColor(String.format("#%06X", 0xFFFFFF and selectedColor.toArgb()))
    }

    // Function to change Background Color
    fun changeBackgroundColor(color: Color) {
        backgroundColor = color
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(14f)
            .border(2.dp, color = Color.DarkGray)
        ) {

            Box(modifier = Modifier
                .matchParentSize()
                .background(backgroundColor) // Use the backgroundColor state
            )

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    DrawingView(context, null).apply {
                        drawingView.value = this
                        setColor(String.format("#%06X", 0xFFFFFF and selectedColor.toArgb())) // Set initial color
                    }
                },
                update = { view ->
                    view.setColor(String.format("#%06X", 0xFFFFFF and selectedColor.toArgb()))
                }
            )
        }

        CustomBottomBar(
            onBrushSizeChange = { showBrushBottomSheet = true },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }

    // Brush Bottom Sheet
    if (showBrushBottomSheet)   {
        ModalBottomSheet(
            onDismissRequest = { showBrushBottomSheet = false }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Select Brush Size", fontSize = 18.sp)
                Slider(
                    value = currentBrushSize,
                    onValueChange = { size -> changeBrushSize(size) },
                    valueRange = 2f..18f,
                    steps = 16, // This allows the slider to jump by integer values
                )

                //For Brush Color Selection
                Text(text = "Select Brush Color", fontSize = 18.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)) // Apply rounded corners
                        .background(colorResource(id = R.color.lightGray))
                        .horizontalScroll(rememberScrollState())
                ) {
                    colors.forEachIndexed { index, _ ->
                        ColorButton(
                            buttonNumber = index,
                            isSelected = index == currentColorIndex,
                            colors = colors,
                            onColorSelected = { buttonIndex ->
                                currentColorIndex = buttonIndex
                                changeBrushColor(colors[buttonIndex])
                            }
                        )
                    }
                }

                //For Background Color Selection
                Text(text = "Select Background Color", fontSize = 18.sp)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)) // Apply rounded corners
                        .background(colorResource(id = R.color.lightGray))
                        .horizontalScroll(rememberScrollState())
                ) {
                    backgroundColors.forEachIndexed { index, _ ->
                        ColorButton(
                            buttonNumber = index,
                            isSelected = index == currentBackgroundColorIndex,
                            colors = backgroundColors,
                            onColorSelected = { buttonIndex ->
                                currentBackgroundColorIndex = buttonIndex
                                changeBackgroundColor(backgroundColors[buttonIndex])
                            }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), // Optional padding for extra spacing
                    horizontalArrangement = Arrangement.SpaceEvenly // Space buttons evenly
                ) {
                    // IconButton for Undo
                    IconButton(
                        onClick = { drawingView.value?.onClickUndo() },
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
                        onClick = { drawingView.value?.onClickRedo() },
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
                        onClick = { drawingView.value?.clearCanvas() },
                        modifier = Modifier.size(56.dp) // Set the size for the IconButton
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.clear),
                            contentDescription = "Clear",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(32.dp) // Increase icon size
                        )
                    }

                    // IconButton for Brush Size
                    IconButton(
                        onClick = { }, // Function for brush size change
                        modifier = Modifier.size(56.dp) // Set the size for the IconButton
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.share), // Replace with the correct drawable for Brush Size
                            contentDescription = "Brush Size",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(32.dp) // Increase icon size
                        )
                    }

                    // IconButton for Change Background
                    IconButton(
                        onClick = {  }, // Function for background color change
                        modifier = Modifier.size(56.dp) // Set the size for the IconButton
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ai), // Replace with the correct drawable for Background Change
                            contentDescription = "Change Background",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(32.dp) // Increase icon size
                        )
                    }
                }


            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DrawingViewPreview() {
    DrawingViewScreen()
}
