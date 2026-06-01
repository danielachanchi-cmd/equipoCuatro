package com.example.equipocuatro.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.equipocuatro.databinding.ItemRetoBinding
import com.example.equipocuatro.model.Reto

class RetoViewHolder(binding: ItemRetoBinding) : RecyclerView.ViewHolder(binding.root) {
    val binding = binding

    fun bind(
        reto: Reto,
        onEditar: (Reto) -> Unit,
        onEliminar: (Reto) -> Unit
    ) {
        binding.tvDescription.text = reto.descripcion

        binding.ivEdit.setOnClickListener {
            onEditar(reto)
        }

        binding.ivDelete.setOnClickListener {
            onEliminar(reto)
        }
    }
}
