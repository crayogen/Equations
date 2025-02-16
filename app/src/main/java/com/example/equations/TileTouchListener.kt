package com.example.equations

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat

class TileTouchListener : View.OnTouchListener {
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        @Suppress("LiftReturnOrAssignment")
        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
            ViewCompat.startDragAndDrop(
                view,
                null,
                TileDragShadowBuilder(view as TextView),
                view.tag as DragData,
                0
            )
            return true
        } else {
            return false
        }
    }
}
