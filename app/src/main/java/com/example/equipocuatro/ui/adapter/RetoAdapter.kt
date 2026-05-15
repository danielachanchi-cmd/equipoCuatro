package com.example.equipocuatro.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.equipocuatro.databinding.ItemRetoBinding
import com.example.equipocuatro.viewmodel.Reto

// Adapter encargado de mostrar la lista de retos en el RecyclerView.
class RetoAdapter(
    private val retos: MutableList<Reto>,
    private val onEditarClick: (Reto) -> Unit = {},
    private val onEliminarClick: (Reto) -> Unit = {}
) : RecyclerView.Adapter<RetoAdapter.RetoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RetoViewHolder {
        // Une el layout item_reto.xml con el ViewHolder.
        val binding = ItemRetoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RetoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RetoViewHolder, position: Int) {
        holder.bind(retos[position])
    }

    override fun getItemCount(): Int = retos.size

    fun actualizarRetos(nuevosRetos: List<Reto>) {
        // Reemplaza la lista actual por una nueva lista de retos.
        retos.clear()
        retos.addAll(nuevosRetos)
        notifyDataSetChanged()
    }

    inner class RetoViewHolder(
        private val binding: ItemRetoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(reto: Reto) {
            // Muestra la descripcion del reto y conecta los botones de accion.
            binding.tvDescription.text = reto.descripcion

            binding.ivEdit.setOnClickListener {
                onEditarClick(reto)
            }

            binding.ivDelete.setOnClickListener {
                onEliminarClick(reto)
            }
        }
    }
}
