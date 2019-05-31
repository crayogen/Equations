package com.example.equations

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EquationsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tileText = view.findViewById<TextView>(R.id.tile_text)
}
