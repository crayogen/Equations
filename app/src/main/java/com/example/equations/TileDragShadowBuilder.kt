package com.example.equations

import android.graphics.*
import android.view.View
import android.widget.TextView

class TileDragShadowBuilder(tile: TextView) : View.DragShadowBuilder(tile) {
    private val backgroundPaint = Paint().apply { color = TileTheme.DRAG_SHADOW_COLOR }
    private val backgroundBounds: RectF

    private val textPaint: Paint = tile.paint
    private val text = tile.text.toString()
    private val textPoint: PointF

    init {
        val borderWidth = getTileBorderWidth(tile.context).toFloat()
        backgroundBounds = RectF(
            borderWidth,
            borderWidth,
            tile.width - borderWidth,
            tile.height - borderWidth
        )

        val textBounds = Rect()
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        textPoint = PointF(
            backgroundBounds.centerX() - textBounds.exactCenterX(),
            backgroundBounds.centerY() - textBounds.exactCenterY()
        )
    }

    override fun onDrawShadow(canvas: Canvas) {
        canvas.drawRect(backgroundBounds, backgroundPaint)
        canvas.drawText(text, textPoint.x, textPoint.y, textPaint)
    }
}
