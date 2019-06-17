package com.example.equations

import android.view.DragEvent
import android.view.View
import android.widget.TextView

class DragListener(
    private val isValid: (Item) -> Boolean,
    private val populateTargetView: (Item) -> Unit
) : View.OnDragListener {

    override fun onDrag(targetView: View, event: DragEvent): Boolean {
        val dragData = event.localState as DragData
        val item = dragData.item
        targetView as TextView
        when (event.action) {
            DragEvent.ACTION_DRAG_ENTERED ->
                themeHoveredEquationTile(targetView, isValid(item))
            DragEvent.ACTION_DRAG_EXITED ->
                themeTile(targetView)
            DragEvent.ACTION_DROP -> {
                if (!isValid(item)) {
                    themeTile(targetView)
                    return false
                }
                dragData.clearTile()
                populateTargetView(item)
            }
        }
        return true
    }
}
