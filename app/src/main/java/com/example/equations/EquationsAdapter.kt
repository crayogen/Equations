package com.example.equations

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EquationsAdapter(private val items: Array<Item?>) : RecyclerView.Adapter<EquationsViewHolder>() {
    override fun getItemCount() = items.size

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquationsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EquationsViewHolder(inflater.inflate(R.layout.tile, parent, false))
    }

    @Suppress("MoveLambdaOutsideParentheses")
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: EquationsViewHolder, position: Int) {
        val item = items[position]
        @Suppress("IfThenToElvis")
        holder.tileText.text = if (item == null) "" else item.toString()
        if (item == null) {
            holder.tileText.tag = null
            holder.tileText.setOnTouchListener(null)
            holder.tileText.setOnDragListener(DragListener({ _ -> true }, { item -> set(position, item) }))
        } else {
            holder.tileText.tag = DragData(item, { set(position, null )})
            holder.tileText.setOnTouchListener(TileTouchListener())
            holder.tileText.setOnDragListener(null)
        }
    }

    operator fun get(index: Int) = items[index]

    operator fun set(index: Int, item: Item?) {
        items[index] = item
        notifyItemChanged(index)
    }
}
