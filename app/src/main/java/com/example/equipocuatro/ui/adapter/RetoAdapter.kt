package com.example.equipocuatro.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.equipocuatro.databinding.ItemRetoBinding
import com.example.equipocuatro.model.Reto
import com.example.equipocuatro.ui.viewholder.RetoViewHolder


class RetoAdapter(
    private val retos: MutableList<Reto>,
    private val onEditarClick: (Reto) -> Unit = {},
    private val onEliminarClick: (Reto) -> Unit = {}
) : RecyclerView.Adapter<RetoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RetoViewHolder {
        val binding = ItemRetoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RetoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RetoViewHolder, position: Int) {
        val reto = retos[position]
        holder.bind(reto, onEditarClick, onEliminarClick)
    }

    override fun getItemCount(): Int = retos.size

    fun actualizarRetos(nuevosRetos: List<Reto>) {
        // Reemplaza la lista actual por una nueva lista de retos.
        retos.clear()
        retos.addAll(nuevosRetos)
        notifyDataSetChanged()
    }

}
