package com.example.equations

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.RECTANGLE
import android.widget.TextView
import android.util.TypedValue

private const val BORDER_WIDTH_DIPS = 8

private const val BORDER_COLOR_DEFAULT = Colors.GRAY
private const val BORDER_COLOR_CALCULATED_NUMBER = Colors.BLUE
private const val BACKGROUND_COLOR_DEFAULT = Colors.WHITE
private const val BACKGROUND_COLOR_NECESSARY = Colors.PINK
private const val BACKGROUND_COLOR_INVALID_RESULT = Colors.RED
private const val TEXT_COLOR_DEFAULT = Colors.DARK_GRAY

fun themeTile(view: TextView) {
    val item = (view.tag as DragData?)?.item
    view.background = createBackground(
        view.context,
        if ((item as? Item.Number)?.equation == null) BORDER_COLOR_DEFAULT else BORDER_COLOR_CALCULATED_NUMBER,
        if (item == null || !item.isNecessary) BACKGROUND_COLOR_DEFAULT else BACKGROUND_COLOR_NECESSARY
    )
    view.setTextColor(TEXT_COLOR_DEFAULT)
}

fun themeInvalidResultTile(resultView: TextView) {
    resultView.background = createBackground(
        resultView.context,
        BORDER_COLOR_DEFAULT,
        BACKGROUND_COLOR_INVALID_RESULT
    )
    resultView.setTextColor(TEXT_COLOR_DEFAULT)
}

private fun createBackground(context: Context, borderColor: Int, backgroundColor: Int): Drawable {
    val drawable = GradientDrawable(
        GradientDrawable.Orientation.TOP_BOTTOM,
        intArrayOf(backgroundColor, backgroundColor)
    )
    drawable.shape = RECTANGLE
    drawable.setStroke(convertToDip(context, BORDER_WIDTH_DIPS), borderColor)
    return drawable
}

private fun convertToDip(context: Context, pixels: Int) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels.toFloat(), context.resources.displayMetrics).toInt()
