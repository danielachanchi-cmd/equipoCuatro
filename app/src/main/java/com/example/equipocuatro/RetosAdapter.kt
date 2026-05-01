package com.example.equipocuatro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RetosAdapter(
    private val listRetos: MutableList<Reto>,
    private val onEditClick: (Reto) -> Unit,
    private val onDeleteClick: (Reto) -> Unit
) : RecyclerView.Adapter<RetosAdapter.RetoViewHolder>() {

    class RetoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val ivEdit: ImageView = view.findViewById(R.id.ivEdit)
        val ivDelete: ImageView = view.findViewById(R.id.ivDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RetoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reto, parent, false)
        return RetoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RetoViewHolder, position: Int) {
        val reto = listRetos[position]
        holder.tvDescription.text = reto.descripcion
        holder.ivEdit.setOnClickListener { onEditClick(reto) }
        holder.ivDelete.setOnClickListener { onDeleteClick(reto) }
    }

    override fun getItemCount(): Int = listRetos.size

    fun updateData(newRetos: List<Reto>? = null) {
        newRetos?.let {
            listRetos.clear()
            listRetos.addAll(it)
        }
        notifyDataSetChanged()
    }
}
