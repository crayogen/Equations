package com.example.equations

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.equations.databinding.TileBinding

class EquationsAdapter(val items: Array<Item?>) : RecyclerView.Adapter<EquationsViewHolder>() {
    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EquationsViewHolder(TileBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    @Suppress("MoveLambdaOutsideParentheses", "NAME_SHADOWING", "RedundantLambdaArrow")
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
        themeTile(holder.tileText)
    }

    operator fun get(index: Int) = items[index]

    operator fun set(index: Int, item: Item?) {
        items[index] = item
        notifyItemChanged(index)
    }
}
