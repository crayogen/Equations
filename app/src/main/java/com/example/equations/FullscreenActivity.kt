package com.example.equations

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_fullscreen.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

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
            Item.Number(1),
            Item.Number(2),
            Item.Operator.Multiply,
            Item.Number(7),
            Item.Number(9),
            Item.Operator.Plus,
            Item.Number(3),
            Item.Number(6)
        ))
        setDragListener(first_number_view, Item::isNumber, ::onEquationDropComplete)
        setDragListener(second_number_view, Item::isNumber, ::onEquationDropComplete)
        setDragListener(operator_view, Item::isOperator, ::onEquationDropComplete)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clearText(view: TextView) {
        view.text = ""
        view.tag = null
        view.setOnTouchListener(null)
        if (first_number_view.tag == null &&
            second_number_view.tag == null &&
            operator_view.tag == null &&
            result_view.tag == null
        ) {
            setDragListener(result_view, ::isValidDragForResultView, ::onResultDropComplete)
        }
    }

    private fun clearTextAndSetDragListener(
        view: TextView,
        isValid: (Item) -> Boolean,
        onDropComplete: (Item) -> Unit
    ) {
        clearText(view)
        setDragListener(view, isValid, onDropComplete)
    }

    @Suppress("MoveLambdaOutsideParentheses")
    private fun clearResult() {
        clearText(result_view)
    }

    private fun clearEquation() {
        clearTextAndSetDragListener(first_number_view, Item::isNumber, ::onEquationDropComplete)
        clearTextAndSetDragListener(second_number_view, Item::isNumber, ::onEquationDropComplete)
        clearTextAndSetDragListener(operator_view, Item::isOperator, ::onEquationDropComplete)
    }

    @Suppress("MoveLambdaOutsideParentheses")
    private fun setDragListener(
        view: TextView,
        isValid: (Item) -> Boolean,
        onDropComplete: (Item) -> Unit
    ) {
        view.setOnDragListener(DragListener(
            isValid,
            { item -> populateTile(view, item, isValid, onDropComplete) }
        ))
    }

    @SuppressLint("ClickableViewAccessibility")
    @Suppress("MoveLambdaOutsideParentheses")
    private fun populateTile(
        view: TextView,
        item: Item,
        isValid: (Item) -> Boolean,
        onDropComplete: (Item) -> Unit
    ) {
        view.text = item.toString()
        view.tag = DragData(
            item,
            {
                if (view != result_view) {
                    clearTextAndSetDragListener(view, isValid, onDropComplete)
                }
                clearResult()
            }
        )
        view.setOnTouchListener(TileTouchListener())
        view.setOnDragListener(null)
        result_view.setOnDragListener(null)
        onDropComplete(item)
    }

    private fun isValidDragForResultView(item: Item) = (item as? Item.Number)?.equation != null

    @Suppress("MoveLambdaOutsideParentheses", "UNUSED_PARAMETER")
    private fun onEquationDropComplete(item: Item) {
        val firstNumber = (first_number_view.tag as DragData?)?.item as Item.Number?
        val secondNumber = (second_number_view.tag as DragData?)?.item as Item.Number?
        val operator = (operator_view.tag as DragData?)?.item as Item.Operator?
        if (firstNumber != null && secondNumber != null && operator != null) {
            val result = operator.operate(firstNumber, secondNumber)
            result_view.text = result.toString()
            result_view.tag = DragData(
                result,
                {
                    clearResult()
                    clearEquation()
                }
            )
            result_view.setOnTouchListener(TileTouchListener())
            result_view.setOnDragListener(null)
        }
    }

    @Suppress("NAME_SHADOWING")
    private fun onResultDropComplete(item: Item) {
        val equation = (item as Item.Number).equation!!
        populateTile(
            first_number_view,
            equation.firstNumber,
            Item::isNumber,
            ::onEquationDropComplete
        )
        populateTile(
            second_number_view,
            equation.secondNumber,
            Item::isNumber,
            ::onEquationDropComplete
        )
        populateTile(
            operator_view,
            equation.operator,
            Item::isOperator,
            ::onEquationDropComplete
        )
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
