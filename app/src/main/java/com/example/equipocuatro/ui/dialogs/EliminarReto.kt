package com.example.equipocuatro.ui.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.equipocuatro.R

class EliminarReto {
    companion object {
        fun showDialogoEliminarReto(
            context: Context,
            reto: String = "",
            onEliminar: () -> Unit = {}
        ) {
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_eliminar_reto, null)
            val tvRetoDeleteDescription = view.findViewById<TextView>(R.id.tvRetoDeleteDescription)
            val btnNoDelete = view.findViewById<Button>(R.id.btnNoDelete)
            val btnSiDelete = view.findViewById<Button>(R.id.btnSiDelete)

            val alertDialog = AlertDialog.Builder(context).create()
            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            alertDialog.setCancelable(false)
            alertDialog.setView(view)

            tvRetoDeleteDescription.text = reto

            btnNoDelete.setOnClickListener {
                alertDialog.dismiss()
            }

            btnSiDelete.setOnClickListener {
                onEliminar()
                alertDialog.dismiss()
            }

            alertDialog.show()
        }
    }
}