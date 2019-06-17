package com.example.equations

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.equations.databinding.TileBinding

class EquationsViewHolder(binding: TileBinding) : RecyclerView.ViewHolder(binding.root) {
    val tileText: TextView = binding.tileText
}
