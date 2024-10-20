package com.example.drawingboardiwithjc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.drawingboardiwithjc.ui.theme.DrawingBoardIWithJCTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DrawingBoardIWithJCTheme {
                DrawingViewScreen()
            }
        }
    }

    @Composable
    fun DrawingViewScreen() {
        var drawingView: DrawingView? = null
        var selectedColor by remember { mutableStateOf(Color.Black) }
        var currentColorIndex by remember { mutableStateOf(0) }
        var currentBrushSize by remember { mutableStateOf(3f) }
        var backgroundColor by remember { mutableStateOf(Color.White) } // State for background color
        var currentBackgroundColorIndex by remember { mutableStateOf(0) } // To track current background color index

        // List of colors from color resources
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

        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(14f)
                .border(2.dp, color = Color.DarkGray)) {
                // Background Layer
                Box(modifier = Modifier
                    .matchParentSize()
                    .background(backgroundColor) // Use the backgroundColor state
                )

                // Drawing View Layer
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        DrawingView(context, null).apply {
                            drawingView = this
                            // Set the initial color
                            setColor(String.format("#%06X", 0xFFFFFF and selectedColor.toArgb()))
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
                onUndo = { drawingView?.onClickUndo() },
                onRedo = { drawingView?.onClickRedo() },
                onBrushSizeChange = {
                    // Cycle between 3f, 5f, and 8f
                    currentBrushSize = when (currentBrushSize) {
                        3f -> 5f
                        5f -> 8f
                        else -> 3f
                    }
                    drawingView?.setSizeForBrush(currentBrushSize)
                },
                onClearCanvas = { drawingView?.clearCanvas() },
                onColorChange = {
                    // Cycle through colors
                    currentColorIndex = (currentColorIndex + 1) % colors.size
                    selectedColor = colors[currentColorIndex]
                    drawingView?.setColor(String.format("#%06X", 0xFFFFFF and selectedColor.toArgb()))
                },
                onBackgroundColorChange = {
                    // Change background color to the next color in the list
                    currentBackgroundColorIndex = (currentBackgroundColorIndex + 1) % backgroundColors.size
                    backgroundColor = backgroundColors[currentBackgroundColorIndex]
                    // Set the color again to ensure brush color remains consistent
                    drawingView?.setColor(String.format("#%06X", 0xFFFFFF and selectedColor.toArgb()))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }




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
            IconButton(onClick = onUndo, Modifier.size(30.dp)) {
                Icon(
                    painter = painterResource(R.drawable.undo),
                    contentDescription = "Undo",
                    tint = Color.Unspecified
                )
            }

            IconButton(onClick = onRedo, Modifier.size(30.dp)) {
                Icon(
                    painter = painterResource(R.drawable.undo),
                    contentDescription = "Redo",
                    tint = Color.Unspecified,
                    modifier = Modifier.graphicsLayer(rotationY = 180f)
                )
            }

            IconButton(onClick = onBrushSizeChange, Modifier.size(30.dp)) {
                Icon(
                    painter = painterResource(R.drawable.brush),
                    contentDescription = "Brush Size",
                    tint = Color.Unspecified
                )
            }

            IconButton(onClick = onClearCanvas, Modifier.size(30.dp)) {
                Icon(
                    painter = painterResource(R.drawable.clear),
                    contentDescription = "Clear",
                    tint = Color.Unspecified
                )
            }

            IconButton(onClick = onColorChange, Modifier.size(30.dp)) {
                Icon(
                    painter = painterResource(R.drawable.color),
                    contentDescription = "Choose Color",
                    tint = Color.Unspecified
                )
            }
            // Button to change the background color
            IconButton(onClick = onBackgroundColorChange, Modifier.size(30.dp)) {
                Icon(
                    painter = painterResource(R.drawable.color), // You can use a different icon
                    contentDescription = "Change Background Color",
                    tint = Color.Unspecified
                )
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun DrawingViewPreview() {
        DrawingViewScreen()
    }
}
