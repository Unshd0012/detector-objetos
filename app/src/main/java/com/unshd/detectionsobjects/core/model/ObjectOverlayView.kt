package com.unshd.detectionsobjects.core.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.view.View
import com.unshd.detectionsobjects.R
import com.google.mlkit.vision.objects.DetectedObject

class ObjectOverlayView(context: Context) : View(context) {
    private val boxPaint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 8f
    }
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 50f
        typeface = Typeface.DEFAULT_BOLD
    }

    private var objects: List<DetectedObject> = emptyList()
    private var imageWidth = 1
    private var imageHeight = 1
    private var rotationDegrees = 0

    fun updateObjects(objs: List<DetectedObject>, imgW: Int, imgH: Int, rotation: Int) {
        objects = objs
        imageWidth = imgW
        imageHeight = imgH
        rotationDegrees = rotation
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val scaleX: Float
        val scaleY: Float

        if (rotationDegrees == 90 || rotationDegrees == 270) {
            scaleX = width.toFloat() / imageHeight.toFloat()
            scaleY = height.toFloat() / imageWidth.toFloat()
        } else {
            scaleX = width.toFloat() / imageWidth.toFloat()
            scaleY = height.toFloat() / imageHeight.toFloat()
        }

        for (obj in objects) {
            val rect: Rect = obj.boundingBox
            val left = rect.left * scaleX
            val top = rect.top * scaleY
            val right = rect.right * scaleX
            val bottom = rect.bottom * scaleY
            canvas.drawRect(left, top, right, bottom, boxPaint)
            obj.labels.firstOrNull()?.text?.let { label ->
                canvas.drawText(label, left, top - 10, textPaint)
                canvas.drawText(obj.trackingId.toString(), left, top - 50, textPaint)
                canvas.drawText(obj.boundingBox.toString(), left, top - 100, textPaint)
            }
            canvas.drawText(obj.trackingId.toString(), left, top - 50, textPaint)
            canvas.drawText(obj.boundingBox.toString(), left, top - 100, textPaint)
            val iconBitmap: Bitmap = BitmapFactory.decodeResource(
                context.resources,
                 R.mipmap.camara
            )
            //canvas.drawBitmap(iconBitmap, left - iconBitmap.width / 2, top - iconBitmap.height, null)
        }
    }
}
