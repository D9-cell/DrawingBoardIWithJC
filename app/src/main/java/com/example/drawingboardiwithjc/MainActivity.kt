package com.example.drawingboardiwithjc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
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

    @Preview(showBackground = true)
    @Composable
    fun DrawingViewPreview() {
        DrawingViewScreen()
    }
}