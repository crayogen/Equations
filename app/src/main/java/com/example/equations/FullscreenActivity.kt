package com.example.equations

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.equations.databinding.ActivityFullscreenBinding
import java.util.*
import kotlin.properties.Delegates.notNull

private const val KEY_GOAL = "goal"
private const val KEY_RECYCLER_VIEW_ITEMS = "recycler_view_items"
private const val KEY_EQUATION_FIRST_NUMBER = "equation_first_number"
private const val KEY_EQUATION_SECOND_NUMBER = "equation_second_number"
private const val KEY_EQUATION_OPERATOR = "equation_operator"

class FullscreenActivity : AppCompatActivity() {
    private lateinit var views: ActivityFullscreenBinding

    private var goal: Int by notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        views = DataBindingUtil.setContentView(this, R.layout.activity_fullscreen)
        makeFullScreen()
        views.recyclerView.addItemDecoration(DividerItemDecoration(this, RecyclerView.HORIZONTAL))
        views.recyclerView.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        views.recyclerView.layoutManager = GridLayoutManager(this, 4, RecyclerView.VERTICAL, false)
        clearEquation()
        if (savedInstanceState == null) {
            replay()
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupGoal(savedInstanceState.getInt(KEY_GOAL))
        val adapterItemParcelables: Array<Parcelable?> =
            savedInstanceState.getParcelableArray(KEY_RECYCLER_VIEW_ITEMS)!!
        views.recyclerView.adapter = EquationsAdapter(
            Arrays.copyOf(adapterItemParcelables, adapterItemParcelables.size, Array<Item?>::class.java)
        )
        val firstNumber = savedInstanceState.getParcelable(KEY_EQUATION_FIRST_NUMBER) as Item.Number?
        val secondNumber = savedInstanceState.getParcelable(KEY_EQUATION_SECOND_NUMBER) as Item.Number?
        val operator = savedInstanceState.getParcelable(KEY_EQUATION_OPERATOR) as Item.Operator?
        if (firstNumber != null) {
            populateTile(views.firstNumberView, firstNumber, Item::isNumber, ::onEquationDropComplete)
        }
        if (secondNumber != null) {
            populateTile(views.secondNumberView, secondNumber, Item::isNumber, ::onEquationDropComplete)
        }
        if (operator != null) {
            populateTile(views.operatorView, operator, Item::isOperator, ::onEquationDropComplete)
        }
        themeTiles()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_GOAL, goal)
        outState.putParcelableArray(KEY_RECYCLER_VIEW_ITEMS, (views.recyclerView.adapter as EquationsAdapter).items)
        outState.putParcelable(KEY_EQUATION_FIRST_NUMBER, (views.firstNumberView.tag as DragData?)?.item)
        outState.putParcelable(KEY_EQUATION_SECOND_NUMBER, (views.secondNumberView.tag as DragData?)?.item)
        outState.putParcelable(KEY_EQUATION_OPERATOR, (views.operatorView.tag as DragData?)?.item)
    }

    private fun replay() {
        generateGameData()
        clearEquation()
        themeTiles()
    }

    private fun themeTiles() {
        themeTile(views.firstNumberView)
        themeTile(views.secondNumberView)
        themeTile(views.operatorView)
        themeTile(views.resultView)
    }

    private fun setupGoal(goal: Int) {
        this.goal = goal
        views.goalView.text = HtmlCompat.fromHtml(
            "Your goal is <font color=#00FF00>$goal</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun generateGameData() {
        setupGoal(30)
        views.recyclerView.adapter = EquationsAdapter(arrayOf(
            Item.Number(4),
            Item.Number(5),
            Item.Number(0),
            Item.Number(1),
            Item.Operator.Mod(),
            Item.Operator.Multiply(),
            Item.Operator.Plus(),
            Item.Operator.Power(),
            Item.Number(5, isRequired = true),
            Item.Number(6, isRequired = true),
            Item.Number(8, isRequired = true),
            Item.Operator.Multiply(isRequired = true),
            Item.Operator.Minus(isRequired = true)
        ))
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clearText(view: TextView) {
        view.text = ""
        view.tag = null
        themeTile(view)
        view.setOnTouchListener(null)
        if (views.firstNumberView.tag == null &&
            views.secondNumberView.tag == null &&
            views.operatorView.tag == null &&
            views.resultView.tag == null
        ) {
            setDragListener(views.resultView, ::isValidDragForResultView, ::onResultDropComplete)
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
        clearTextAndSetDragListener(views.firstNumberView, Item::isNumber, ::onEquationDropComplete)
        clearTextAndSetDragListener(views.secondNumberView, Item::isNumber, ::onEquationDropComplete)
        clearTextAndSetDragListener(views.operatorView, Item::isOperator, ::onEquationDropComplete)
        clearText(views.resultView)
    }

    private fun clearTile(
        view: TextView,
        isValid: (Item) -> Boolean,
        onDropComplete: (Item) -> Unit
    ) {
        if (view == views.resultView) {
            clearEquation()
        } else {
            clearTextAndSetDragListener(view, isValid, onDropComplete)
            clearText(views.resultView)
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
        views.resultView.setOnDragListener(null)
        onDropComplete(item)
    }

    private fun isValidDragForResultView(item: Item) = (item as? Item.Number)?.equation != null

    @Suppress("MoveLambdaOutsideParentheses", "UNUSED_PARAMETER", "NAME_SHADOWING")
    private fun onEquationDropComplete(item: Item) {
        val firstNumber = (views.firstNumberView.tag as DragData?)?.item as Item.Number?
        val secondNumber = (views.secondNumberView.tag as DragData?)?.item as Item.Number?
        val operator = (views.operatorView.tag as DragData?)?.item as Item.Operator?
        if (firstNumber != null && secondNumber != null && operator != null) {
            val result = operator.operate(firstNumber, secondNumber)
            if (result == null) {
                themeInvalidResultTile(views.resultView)
            } else {
                /* Don't send onResultDropComplete() because that would cause recursion when this is
                 * called as a result of a populateTile() call from onResultDropComplete() itself.
                 * Since equation is already populated at this point, we don't need that callback
                 * immediately, and the clearTile callback that populateTile() sets in the DragData tag
                 * has special logic for handling and setting the result view callback, and doesn't use
                 * the one provided as the argument for the result view.
                 */
                populateTile(views.resultView, result, ::isValidDragForResultView, onDropComplete = {})
                if (result.number == goal &&
                    (views.recyclerView.adapter as EquationsAdapter).items.none { item -> item?.isRequired == true }
                ) {
                    win()
                }
            }
        }
    }

    @Suppress("MoveLambdaOutsideParentheses")
    private fun onResultDropComplete(item: Item) {
        val equation = (item as Item.Number).equation!!
        populateTile(views.firstNumberView, equation.firstNumber, Item::isNumber, ::onEquationDropComplete)
        populateTile(views.secondNumberView, equation.secondNumber, Item::isNumber, ::onEquationDropComplete)
        populateTile(views.operatorView, equation.operator, Item::isOperator, ::onEquationDropComplete)
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
        views.root.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
}
