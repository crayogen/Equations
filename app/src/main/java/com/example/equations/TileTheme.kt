package com.example.equations

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.RECTANGLE
import android.widget.TextView
import android.util.TypedValue

private const val BORDER_WIDTH = 8

fun themeTile(view: TextView) {
    val item = (view.tag as DragData?)?.item
    view.background = createBackground(
        view.context,
        if ((item as? Item.Number)?.equation != null) Colors.BLUE else Colors.GRAY,
        if (item != null && item.isNecessary) Colors.PINK else Colors.WHITE
    )
    view.setTextColor(Colors.DARK_GRAY)
}

private fun createBackground(context: Context, strokeColor: Int, color: Int): Drawable {
    val drawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(color, color))
    drawable.shape = RECTANGLE
    drawable.setStroke(convertToDip(context, BORDER_WIDTH), strokeColor)
    return drawable
}

private fun convertToDip(context: Context, pixels: Int) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels.toFloat(), context.resources.displayMetrics).toInt()
