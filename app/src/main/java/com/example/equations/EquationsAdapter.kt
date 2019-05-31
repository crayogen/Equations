package com.example.equations

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EquationsAdapter(private val items: List<Char>) : RecyclerView.Adapter<EquationsViewHolder>() {
    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquationsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EquationsViewHolder(inflater.inflate(R.layout.tile, parent, false))
    }

    override fun onBindViewHolder(holder: EquationsViewHolder, position: Int) {
        holder.tileText.text = items[position].toString()
    }
}
