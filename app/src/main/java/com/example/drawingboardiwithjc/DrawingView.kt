package com.example.drawingboardiwithjc

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.compose.ui.graphics.toArgb

class DrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var mDrawPath: CustomPath? = null
    private var mCanvasBitmap: Bitmap? = null
    private var mDrawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null
    private var mBrushSize: Float = 7.toFloat()
    private var color = Color.BLACK
    private var canvas: Canvas? = null

    // Paths to store drawn lines
    private val mPaths = ArrayList<CustomPath>()
    private val mUndoPaths = ArrayList<CustomPath>()
    private val mRedoPaths = ArrayList<CustomPath>() // New Redo paths list

    init {
        setUpDrawing()
    }

    // Undo functionality
    fun onClickUndo() {
        if (mPaths.isNotEmpty()) {
            mUndoPaths.add(mPaths.removeAt(mPaths.size - 1))
            mRedoPaths.clear() // Clear redo paths on undo
            invalidate()
        }
    }

    // Redo functionality
    fun onClickRedo() {
        if (mUndoPaths.isNotEmpty()) {
            mPaths.add(mUndoPaths.removeAt(mUndoPaths.size - 1))
            invalidate()
        }
    }

    // Clear the canvas
    fun clearCanvas() {
        mPaths.clear()
        mUndoPaths.clear()
        mRedoPaths.clear() // Clear redo paths on canvas clear
        invalidate()
    }

    private fun setUpDrawing() {
        mDrawPaint = Paint()
        mDrawPath = CustomPath(color, mBrushSize)
        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)
        for (path in mPaths) {
            mDrawPaint!!.strokeWidth = path.brushThickness
            mDrawPaint!!.color = path.color
            canvas.drawPath(path, mDrawPaint!!)
        }
        if (!mDrawPath!!.isEmpty) {
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint!!.color = mDrawPaint!!.color
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        // Handle drawing on touch events
        val touchX = event?.x ?: return false
        val touchY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrushSize
                mDrawPath!!.reset()
                mDrawPath!!.moveTo(touchX, touchY)

                // Add the starting point with status 0
                mDrawPath!!.coordinates.add(Triple(touchX, touchY, 0))
            }
            MotionEvent.ACTION_MOVE -> {
                mDrawPath!!.lineTo(touchX, touchY)
                // Add intermediate points with status 1
                mDrawPath!!.coordinates.add(Triple(touchX, touchY, 1))
            }
            MotionEvent.ACTION_UP -> {
                // Add the last point with status 0
                mDrawPath!!.coordinates.add(Triple(touchX, touchY, 0))
                mPaths.add(mDrawPath!!)

                // Print the paths with coordinates and statuses
                Log.d("DrawingView", "Current paths with coordinates and statuses: $mPaths")
                mDrawPath = CustomPath(color, mBrushSize)
            }
        }
        invalidate()
        return true
    }

    fun setSizeForBrush(newSize: Float) {
        mBrushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, resources.displayMetrics)
        mDrawPaint!!.strokeWidth = mBrushSize
    }

    // Set color method
    fun setColor(currentColor: String) {
        color = Color.parseColor(currentColor)
        mDrawPaint!!.color = color // Update your paint object if you have one
        invalidate() // Redraw the view
    }

    fun getBitmap(currentBackgroundColor: androidx.compose.ui.graphics.Color): Bitmap {
        // Create a bitmap with the same size as the drawing view
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // Create a canvas to draw onto the bitmap
        val canvas = Canvas(bitmap)

        // Draw the background color
        canvas.drawColor(currentBackgroundColor.toArgb()) // Use the current background color

        // Draw the current drawing onto the bitmap
        draw(canvas)

        return bitmap
    }

    internal inner class CustomPath(var color: Int, var brushThickness: Float) : Path() {
        val coordinates = mutableListOf<Triple<Float, Float, Int>>()

        override fun toString(): String {
            return "CustomPath(color=$color, brushThickness=$brushThickness, coordinates=$coordinates)"
        }
    }
}
