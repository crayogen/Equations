package com.example.equations

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_fullscreen.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates.notNull

class FullscreenActivity : AppCompatActivity() {
    var adapter: EquationsAdapter by notNull()
    var goal: Int by notNull()

    @Suppress("MoveLambdaOutsideParentheses")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)
        makeFullScreen()
        recycler_view.addItemDecoration(DividerItemDecoration(this, RecyclerView.HORIZONTAL))
        recycler_view.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        recycler_view.layoutManager = GridLayoutManager(this, 4, RecyclerView.VERTICAL, false)
        replay()
    }

    private fun replay() {
        generateGameData()
        recycler_view.adapter = adapter
        clearTextAndSetDragListener(first_number_view, Item::isNumber, ::onEquationDropComplete)
        clearTextAndSetDragListener(second_number_view, Item::isNumber, ::onEquationDropComplete)
        clearTextAndSetDragListener(operator_view, Item::isOperator, ::onEquationDropComplete)
        clearResult()
        themeTile(first_number_view)
        themeTile(second_number_view)
        themeTile(operator_view)
        themeTile(result_view)

        goal_view.text = HtmlCompat.fromHtml(
            "Your goal is <font color=#00FF00>$goal</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun generateGameData() {
        goal = 30
        adapter = EquationsAdapter(arrayOf(
            Item.Number(4),
            Item.Number(5),
            Item.Number(0),
            Item.Number(1),
            Item.Operator.Mod(),
            Item.Operator.Multiply(),
            Item.Operator.Plus(),
            Item.Operator.Power(),
            Item.Number(5, isNecessary = true),
            Item.Number(6, isNecessary = true),
            Item.Number(8, isNecessary = true),
            Item.Operator.Multiply(isNecessary = true),
            Item.Operator.Minus(isNecessary = true)
        ))
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clearText(view: TextView) {
        view.text = ""
        view.tag = null
        themeTile(view)
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
        themeTile(view)
        view.setOnTouchListener(TileTouchListener())
        view.setOnDragListener(null)
        result_view.setOnDragListener(null)
        onDropComplete(item)
    }

    private fun isValidDragForResultView(item: Item) = (item as? Item.Number)?.equation != null

    @Suppress("MoveLambdaOutsideParentheses", "UNUSED_PARAMETER", "NAME_SHADOWING")
    private fun onEquationDropComplete(item: Item) {
        val firstNumber = (first_number_view.tag as DragData?)?.item as Item.Number?
        val secondNumber = (second_number_view.tag as DragData?)?.item as Item.Number?
        val operator = (operator_view.tag as DragData?)?.item as Item.Operator?
        if (firstNumber != null && secondNumber != null && operator != null) {
            val result = operator.operate(firstNumber, secondNumber)
            if (result == null) {
                themeInvalidResultTile(result_view)
            } else {
                result_view.text = result.toString()
                result_view.tag = DragData(
                    result,
                    {
                        clearResult()
                        clearEquation()
                    }
                )
                themeTile(result_view)
                result_view.setOnTouchListener(TileTouchListener())
                result_view.setOnDragListener(null)
                if (result.number == goal && adapter.items.none { item -> item?.isNecessary == true }) {
                    win()
                }
            }
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

    private fun win() {
        AlertDialog.Builder(this)
            .setTitle("Congratulations!")
            .setMessage("You won!!!!!!")
            .setNegativeButton("Quit") { _, _ -> finish() }
            .setPositiveButton("Replay") { _, _ ->  replay()}
            .show()
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
