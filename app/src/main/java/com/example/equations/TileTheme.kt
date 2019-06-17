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
private const val BACKGROUND_COLOR_REQUIRED = Colors.PINK
private const val BACKGROUND_COLOR_HOVERED_VALID = Colors.GREEN
private const val BACKGROUND_COLOR_HOVERED_INVALID = Colors.GRAY
private const val BACKGROUND_COLOR_INVALID_RESULT = Colors.RED
private const val TEXT_COLOR_DEFAULT = Colors.DARK_GRAY

object TileTheme {
    const val DRAG_SHADOW_COLOR = Colors.LIGHT_GRAY
}

fun themeTile(view: TextView) {
    val borderWidth = getTileBorderWidth(view.context)
    view.setPadding(borderWidth, borderWidth, borderWidth, borderWidth)
    val item = (view.tag as DragData?)?.item
    themeTile(
        view,
        if ((item as? Item.Number)?.equation == null) BORDER_COLOR_DEFAULT else BORDER_COLOR_CALCULATED_NUMBER,
        if (item == null || !item.isRequired) BACKGROUND_COLOR_DEFAULT else BACKGROUND_COLOR_REQUIRED,
        TEXT_COLOR_DEFAULT
    )
}

fun themeHoveredEquationTile(view: TextView, isValid: Boolean) {
    themeTile(
        view,
        BORDER_COLOR_DEFAULT,
        if (isValid) BACKGROUND_COLOR_HOVERED_VALID else BACKGROUND_COLOR_HOVERED_INVALID,
        TEXT_COLOR_DEFAULT
    )
}

fun themeInvalidResultTile(resultView: TextView) {
    themeTile(
        resultView,
        BORDER_COLOR_DEFAULT,
        BACKGROUND_COLOR_INVALID_RESULT,
        TEXT_COLOR_DEFAULT
    )
}

fun themeTile(view: TextView, borderColor: Int, backgroundColor: Int, textColor: Int) {
    view.background = createBackground(view.context, borderColor, backgroundColor)
    view.setTextColor(textColor)
}

fun getTileBorderWidth(context: Context) = convertToDip(context, BORDER_WIDTH_DIPS)

private fun createBackground(context: Context, borderColor: Int, backgroundColor: Int): Drawable {
    val drawable = GradientDrawable(
        GradientDrawable.Orientation.TOP_BOTTOM,
        intArrayOf(backgroundColor, backgroundColor)
    )
    drawable.shape = RECTANGLE
    drawable.setStroke(getTileBorderWidth(context), borderColor)
    return drawable
}

private fun convertToDip(context: Context, pixels: Int) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels.toFloat(), context.resources.displayMetrics).toInt()
