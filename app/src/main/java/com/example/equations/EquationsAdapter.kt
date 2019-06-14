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
        holder.tileText.tag = item
        holder.tileText.setOnTouchListener(
            if (item == null) {
                null
            } else {
                TileTouchListener(
                    item,
                    { set(position, null )},
                    { set(position, item )}
                )
            }
        )
    }

    operator fun get(index: Int) = items[index]

    operator fun set(index: Int, item: Item?) {
        items[index] = item
        notifyItemChanged(index)
    }
}
