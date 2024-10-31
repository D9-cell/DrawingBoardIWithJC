package com.example.drawingboardiwithjc

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlin.math.roundToInt


@Composable
fun DrawingViewScreen() {
    var selectedColor by remember { mutableStateOf(Color.Black) }
    var currentColorIndex by remember { mutableIntStateOf(0) }
    var currentBrushSize by remember { mutableFloatStateOf(3f) }
    var backgroundColor by remember { mutableStateOf(Color.White) }
    var currentBackgroundColorIndex by remember { mutableIntStateOf(0) }
    var showBrushBottomSheet by remember { mutableStateOf(false) }

    // Brush and background colors list
    val colors = listOf(
        colorResource(id = R.color.chalkWhite),colorResource(id = R.color.chalkPastelPink),
        colorResource(id = R.color.chalkPastelBlue),colorResource(id = R.color.chalkPastelGreen),
        colorResource(id = R.color.chalkPastelYellow),colorResource(id = R.color.chalkLavender),
        colorResource(id = R.color.chalkLightCoral),colorResource(id = R.color.chalkPeach),
    )
    val backgroundColors = listOf(
        colorResource(id = R.color.white),colorResource(id = R.color.charcoal),
        colorResource(id = R.color.slateGray),colorResource(id = R.color.darkGreen),
        colorResource(id = R.color.midnightBlue),colorResource(id = R.color.darkOliveGreen),
        colorResource(id = R.color.graphite),colorResource(id = R.color.pineGreen)
    )

    val drawingView = remember { mutableStateOf<DrawingView?>(null) }

    // Functions to change brush size, color, and background color
    fun changeBrushSize(size: Float) {
        currentBrushSize = size
        drawingView.value?.setSizeForBrush(currentBrushSize)
    }

    fun changeBrushColor(color: Color) {
        selectedColor = color
        drawingView.value?.setColor(String.format("#%06X", 0xFFFFFF and selectedColor.toArgb()))
    }

    fun changeBackgroundColor(color: Color) {
        backgroundColor = color
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(14f)
                    .border(2.dp, color = Color.DarkGray)
            ) {
                Box(
                    modifier = Modifier
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
        }
        Box(modifier = Modifier.fillMaxSize()) {
            var offsetX by remember { mutableFloatStateOf(0f) }
            var offsetY by remember { mutableFloatStateOf(0f) }

            FloatingActionButton(
                onClick = { showBrushBottomSheet = true }, // Open brush size changer on click
                modifier = Modifier
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .padding(16.dp)
                    .size(54.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(colorResource(id = R.color.purple_200))
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    }
            ) {
                Icon(
                    painter = painterResource(R.drawable.brush), // Use brush icon
                    contentDescription = "Brush Size",
                    tint = Color.Unspecified,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Brush Bottom Sheet
        if (showBrushBottomSheet) {
            BrushBottomSheet(
                onDismiss = { showBrushBottomSheet = false },
                drawingView = drawingView.value,
                colors = colors,
                backgroundColors = backgroundColors,
                currentBrushSize = currentBrushSize,
                currentColorIndex = currentColorIndex,
                currentBackgroundColorIndex = currentBackgroundColorIndex,
                changeBrushSize = ::changeBrushSize,
                changeBrushColor = ::changeBrushColor,
                changeBackgroundColor = ::changeBackgroundColor,
                updateCurrentColorIndex = { newIndex -> currentColorIndex = newIndex }, // New function
                updateCurrentBackgroundColorIndex = { newIndex -> currentBackgroundColorIndex = newIndex } // New function
            )
        }
    }
}

