package com.example.drawingboardiwithjc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DrawingViewScreen()
        }
    }

    @Composable
    fun DrawingViewScreen() {
        // Using AndroidView to host the DrawingView inside Jetpack Compose
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                DrawingView(context, null).apply {
                    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
                        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                        insets
                    }
                }
            }
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun DrawingViewPreview() {
        // A simple placeholder for preview purposes
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray) // Background color just for the preview
        ) {
            // You can preview any Compose UI here if needed
        }
    }
}