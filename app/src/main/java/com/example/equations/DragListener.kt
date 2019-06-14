package com.example.equations

import android.graphics.Color
import android.view.DragEvent
import android.view.View
import android.widget.TextView

class DragListener(private val isValid: (Item) -> Boolean, private val populateTargetView: (Item) -> Unit) : View.OnDragListener {
    override fun onDrag(targetView: View, event: DragEvent): Boolean {
        val dragData = event.localState as DragData
        val item = dragData.item
        val isValidResult = (targetView as TextView).text.isEmpty() && isValid(item)
        when (event.action) {
            DragEvent.ACTION_DRAG_ENTERED ->
                targetView.setBackgroundColor(if (isValidResult) Color.GREEN else Color.GRAY)
            DragEvent.ACTION_DRAG_EXITED ->
                targetView.setBackgroundColor(Color.BLACK)
            DragEvent.ACTION_DROP -> {
                targetView.setBackgroundColor(Color.BLACK)
                if (!isValidResult) return false
                dragData.clearTile()
                populateTargetView(item)
            }
        }
        return true
    }
}
