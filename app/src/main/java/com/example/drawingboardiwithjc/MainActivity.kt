package com.example.drawingboardiwithjc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
        Column(modifier = Modifier.fillMaxSize()) {

            // Use a variable to hold the DrawingView instance
            var drawingView: DrawingView? = null

            // Drawing view takes 4/5 of the screen
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(14f)
                    .border(2.dp, color = Color.DarkGray)
                , // 12 out of 13 parts of the screen
                factory = { context ->
                    DrawingView(context, null).apply {
                        drawingView = this
                        // Handling insets for system bars if needed
                        ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
                            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                            insets
                        }
                    }
                }
            )


            CustomBottomBar(
                onUndo = { drawingView?.onClickUndo() },
                onRedo = { drawingView?.onClickRedo() },  // **New Redo action linked**
                onBrushSizeChange = { drawingView?.setSizeForBrush(5f) },
                onClearCanvas = { drawingView?.clearCanvas() },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // 1 out of 13 parts of the screen
            )
        }
    }

    @Composable
    fun CustomBottomBar(
        onUndo: () -> Unit,
        onRedo: () -> Unit,  // **New Redo callback added**
        onBrushSizeChange: () -> Unit,
        onClearCanvas: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Row(
            modifier = modifier
                .background(colorResource(id = R.color.pastelPeach))
                .padding(16.dp), // Padding around the bar
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onUndo,Modifier.size(30.dp)) {
                Icon(
                    painter = painterResource(R.drawable.undo),
                    contentDescription = "Undo",
                    tint = Color.Unspecified // Prevent tinting
                )
            }

            // **Redo Button**
            IconButton(onClick = onRedo, Modifier.size(30.dp)) {
                Icon(
                    painter = painterResource(R.drawable.undo), // **Use a redo icon**
                    contentDescription = "Redo",
                    tint = Color.Unspecified ,// Prevent tinting
                    modifier = Modifier.graphicsLayer(rotationY = 180f) // Rotate the icon 180 degrees

                )
            }

            IconButton(onClick = onBrushSizeChange,Modifier.size(30.dp)) {
                Icon(
                    painter = painterResource(R.drawable.brush),
                    contentDescription = "Brush Size",
                    tint = Color.Unspecified // Prevent tinting
                )
            }
            IconButton(onClick = onClearCanvas,Modifier.size(30.dp)) {
                Icon(
                    painter = painterResource(R.drawable.clear),
                    contentDescription = "Clear",
                    tint = Color.Unspecified // Prevent tinting
                )
            }
            IconButton(onClick = onClearCanvas,Modifier.size(30.dp)) {
                Icon(
                    painter = painterResource(R.drawable.color),
                    contentDescription = "Clear",
                    tint = Color.Unspecified // Prevent tinting
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


