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
    private var adapter: EquationsAdapter by notNull()
    private var goal: Int by notNull()

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

        goal_view.text = HtmlCompat.fromHtml(
            "Your goal is <font color=#00FF00>$goal</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        recycler_view.adapter = adapter

        clearEquation()

        themeTile(first_number_view)
        themeTile(second_number_view)
        themeTile(operator_view)
        themeTile(result_view)
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

    private fun clearEquation() {
        clearTextAndSetDragListener(first_number_view, Item::isNumber, ::onEquationDropComplete)
        clearTextAndSetDragListener(second_number_view, Item::isNumber, ::onEquationDropComplete)
        clearTextAndSetDragListener(operator_view, Item::isOperator, ::onEquationDropComplete)
        clearText(result_view)
    }

    private fun clearTile(
        view: TextView,
        isValid: (Item) -> Boolean,
        onDropComplete: (Item) -> Unit
    ) {
        if (view == result_view) {
            clearEquation()
        } else {
            clearTextAndSetDragListener(view, isValid, onDropComplete)
            clearText(result_view)
        }
    }

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
        view.tag = DragData(item, { clearTile(view, isValid, onDropComplete) })
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
                /* Don't send onResultDropComplete() because that would cause recursion when this is
                 * called as a result of a populateTile() call from onResultDropComplete() itself.
                 * Since equation is already populated at this point, we don't need that callback
                 * immediately, and the clearTile callback that populateTile() sets in the DragData tag
                 * has special logic for handling and setting the result view callback, and doesn't use
                 * the one provided as the argument for the result view.
                 */
                populateTile(result_view, result, ::isValidDragForResultView, onDropComplete = {})
                if (result.number == goal && adapter.items.none { item -> item?.isNecessary == true }) {
                    win()
                }
            }
        }
    }

    @Suppress("MoveLambdaOutsideParentheses")
    private fun onResultDropComplete(item: Item) {
        val equation = (item as Item.Number).equation!!
        populateTile(first_number_view, equation.firstNumber, Item::isNumber, ::onEquationDropComplete)
        populateTile(second_number_view, equation.secondNumber, Item::isNumber, ::onEquationDropComplete)
        populateTile(operator_view, equation.operator, Item::isOperator, ::onEquationDropComplete)
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
