package com.example.drawingboardiwithjc

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

@Composable
fun DrawingViewScreen() {
    var selectedColor by remember { mutableStateOf(Color.Black) }
    var currentColorIndex by remember { mutableIntStateOf(0) }
    var currentBrushSize by remember { mutableFloatStateOf(3f) }
    var backgroundColor by remember { mutableStateOf(Color.White) } // State for background color
    var currentBackgroundColorIndex by remember { mutableIntStateOf(0) }

    // List of Colors from Brush Color Resources
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

    // List of Colors from Background Color Resources
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

    // Create and remember the DrawingView instance
    val drawingView = remember { mutableStateOf<DrawingView?>(null) }

    // Function to change brush size
    fun changeBrushSize() {
        currentBrushSize = when (currentBrushSize) {
            3f -> 5f
            5f -> 8f
            else -> 3f
        }
        drawingView.value?.setSizeForBrush(currentBrushSize)
    }

    // Function to change brush Color
    fun changeBrushColor() {
        currentColorIndex = (currentColorIndex + 1) % colors.size // Cycle through colors
        selectedColor = colors[currentColorIndex]
        drawingView.value?.setColor(String.format("#%06X", 0xFFFFFF and selectedColor.toArgb()))
    }

    // Function to change Background Color of Board
    fun changeBackgroundColor() {
        currentBackgroundColorIndex = (currentBackgroundColorIndex + 1) % backgroundColors.size // Change background color to the next color in the list
        backgroundColor = backgroundColors[currentBackgroundColorIndex]
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(14f)
            .border(2.dp, color = Color.DarkGray)) {

            // Background Layer (without causing a recomposition of the DrawingView)
            Box(modifier = Modifier
                .matchParentSize()
                .background(backgroundColor) // Use the backgroundColor state
            )

            // Drawing View Layer
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    DrawingView(context, null).apply {
                        drawingView.value = this
                        setColor(String.format("#%06X", 0xFFFFFF and selectedColor.toArgb())) // Set the initial color
                        ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
                            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                            insets
                        }
                    }
                },
                update = { view ->
                    // Update the brush color whenever selectedColor changes
                    view.setColor(String.format("#%06X", 0xFFFFFF and selectedColor.toArgb()))
                }
            )
        }

        // Bottom Bar with Brush and Color Picker
        CustomBottomBar(
            onUndo = { drawingView.value?.onClickUndo() },
            onRedo = { drawingView.value?.onClickRedo() },
            onBrushSizeChange = { changeBrushSize() /* Call the changeBrushSize function */ },
            onClearCanvas = { drawingView.value?.clearCanvas() },
            onColorChange = { changeBrushColor() },
            onBackgroundColorChange = { changeBackgroundColor() },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}
