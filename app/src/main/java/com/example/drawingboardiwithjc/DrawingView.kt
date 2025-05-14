package com.example.drawingboardiwithjc

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.FileProvider
import getProcessedFeatures
import java.io.File
import java.io.FileOutputStream

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

    // Variable to store all coordinates from mPaths
    private val allCoordinates = mutableListOf<List<Triple<Int, Int, Int>>>()

    init {
        setUpDrawing()
    }

    // Function to update the coordinates list
    private fun updateAllCoordinates() {
        allCoordinates.clear()
        allCoordinates.addAll(mPaths.map { it.coordinates })
    }

    // Undo functionality
    fun onClickUndo() {
        if (mPaths.isNotEmpty()) {
            mUndoPaths.add(mPaths.removeAt(mPaths.size - 1))
            mRedoPaths.clear()
            updateAllCoordinates() // Update after undo
            invalidate()
        }
    }

    // Redo functionality
    fun onClickRedo() {
        if (mUndoPaths.isNotEmpty()) {
            mPaths.add(mUndoPaths.removeAt(mUndoPaths.size - 1))
            updateAllCoordinates() // Update after redo
            invalidate()
        }
    }

    // Clear the canvas
    fun clearCanvas() {
        mPaths.clear()
        mUndoPaths.clear()
        mRedoPaths.clear()
        updateAllCoordinates() // Update after clear
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
        val touchX = event?.x?.toInt() ?: return false
        val touchY = event.y.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrushSize
                mDrawPath!!.reset()
                mDrawPath!!.moveTo(touchX.toFloat(), touchY.toFloat())

                mDrawPath!!.coordinates.add(Triple(touchX, touchY, 1))
            }
            MotionEvent.ACTION_MOVE -> {
                mDrawPath!!.lineTo(touchX.toFloat(), touchY.toFloat())
                mDrawPath!!.coordinates.add(Triple(touchX, touchY, 1))
            }
            MotionEvent.ACTION_UP -> {
                mDrawPath!!.coordinates.add(Triple(touchX, touchY, 0))
                mPaths.add(mDrawPath!!)
                updateAllCoordinates() // Update after new path
                Log.d("DrawingView", "Updated allCoordinates: $allCoordinates")
/*
                val features = getProcessedFeatures(this) // Ensure this returns a FloatArray
                Log.d("ProcessedFeatures", "Extracted Features: $features")
                Log.d("ProcessedFeatures","Extracted Features List Size: ${features.size}")

                val classifier = TFLiteClassifier(context)
                try {
                    val predictedClassIndex = classifier.predict(features)
                    val labelClasses = arrayOf(
                        "A/अ/অ","AA/आ/আ","ADA/ढ़/ঢ","AN/ं/ং",
                        "BA/ब/ব","BHA/भ/ভ","BI/ः/ঃ",
                        "C/च/চ","CH/छ/ছ","CN/ँ/ঁ",
                        "DA/द/দ","DDA/ड/ড","DDH/ढ/ঢ","DHA/ध/ধ","DRA/ड़/ড়",
                        "E/ए/এ","EN/ञ/ঞ",
                        "G/ग/গ","GH/घ/ঘ",
                        "HA/ह/হ",
                        "I/इ/ই","II/ई/ঈ",
                        "JA/ज/জ","JH/झ/ঝ",
                        "K/क/ক","KH/ख/খ","KT/त्/ৎ",
                        "LA/ल/ল",
                        "MA/म/ম","MN/ण/ণ","MSA/ष/ষ",
                        "NA/न/ন",
                        "O/ओ/ও","OI/ऐ/ঐ","OU/औ/ঔ",
                        "PA/प/প","PHA/फ/ফ",
                        "RA/र/র","RI/ऋ/ঋ",
                        "S/स/স","SA/श/শ",
                        "T/ट/ট","TA/त/ত","THA/थ/থ","TTA/ठ/ঠ",
                        "U/उ/উ","UN/ङ/ঙ","UU/ऊ/ঊ",
                        "Y/य़/য়","YA/य/য"
                    )
                    val predictedChar = labelClasses[predictedClassIndex]
                    Log.d("Prediction", "Predicted Class Index: $predictedClassIndex")
                    Log.d("Prediction", "Predicted Character: $predictedChar")
                    Toast.makeText(context, "Predicted Character: $predictedChar", Toast.LENGTH_LONG).show()
                } catch (e: IllegalArgumentException) {
                    Log.e("TFLiteClassifier", "Prediction failed: ${e.message}")
                    Toast.makeText(context, "Draw a complete character and try again", Toast.LENGTH_SHORT).show()
                }
                */

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

    fun getAllCoordinates(): List<List<Triple<Int, Int, Int>>> {
        return allCoordinates
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

    fun saveBitmap(context: Context, bitmap: Bitmap): Uri? {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Drawing_${System.currentTimeMillis()}.png")
        return try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
            }
            // Return the Uri for the saved file
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        } catch (e: Exception) {
            Toast.makeText(context, "Error saving drawing: ${e.message}", Toast.LENGTH_SHORT).show()
            null
        }
    }

}
