package com.example.equations

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder

class TileTouchListener(
    private val item: Item,
    private val clearTile: () -> Unit,
    private val populateTile: () -> Unit
) : View.OnTouchListener {

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        @Suppress("LiftReturnOrAssignment")
        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
            val shadowBuilder = DragShadowBuilder(view)
            @Suppress("DEPRECATION")
            view.startDrag(null, shadowBuilder, DragData(item, clearTile, populateTile), 0)
            return true
        } else {
            return false
        }
    }
}