package com.example.equations

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_fullscreen.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.equations.Item.Number

class FullscreenActivity : AppCompatActivity() {
    @Suppress("MoveLambdaOutsideParentheses")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)
        makeFullScreen()
        recycler_view.addItemDecoration(DividerItemDecoration(this, RecyclerView.HORIZONTAL))
        recycler_view.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        recycler_view.layoutManager = GridLayoutManager(this, 4, RecyclerView.VERTICAL, false)
        recycler_view.adapter = EquationsAdapter(arrayOf(
            Number(1),
            Number(2),
            Item.Operator.Multiply,
            Number(7),
            Number(9),
            Item.Operator.Plus,
            Number(3),
            Number(6)
        ))
        setDragListener(first_number, {item -> item is Number})
        setDragListener(second_number, {item -> item is Number})
        setDragListener(operator, {item -> item is Item.Operator })
    }

    @Suppress("MoveLambdaOutsideParentheses")
    fun setDragListener(view: TextView, isValid: (Item) -> Boolean) {
        view.setOnDragListener(DragListener(
            isValid,
            { item ->
                view.text = item.toString()
                view.tag = DragData(
                    item,
                    {
                        view.text = ""
                        setDragListener(view, isValid)
                    }
                )
                view.setOnTouchListener(TileTouchListener())
                view.setOnDragListener(null)
            }
        ))
    }

    private fun makeFullScreen() {
        // Remove status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        root.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION    }
}
