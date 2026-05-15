package com.example.equipocuatro.ui.dialogs

import android.R
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.equipocuatro.databinding.DialogAgregarRetoBinding

class AgregarReto {
    companion object{
        fun showDialgoAgregarReto(
            context: Context,
            onGuardar: (String) -> Unit = {}
        ){
            val inflater = LayoutInflater.from(context)
            val binding = DialogAgregarRetoBinding.inflate(inflater)

            val alertDialog = AlertDialog.Builder(context).create()
            alertDialog.window?.setBackgroundDrawableResource(R.color.transparent)
            alertDialog.setCancelable(false)
            alertDialog.setView(binding.root)

            // Activa el boton guardar solo cuando hay texto escrito.
            binding.etReto.addTextChangedListener {
                var reto = binding.etReto.text
                if(reto.isNotEmpty()){
                    binding.btnGuardar.setBackgroundColor(ContextCompat.getColor(context, com.example.equipocuatro.R.color.orange))
                    binding.btnGuardar.isEnabled = true
                } else {
                    binding.btnGuardar.setBackgroundColor(ContextCompat.getColor(context, com.example.equipocuatro.R.color.gray_disabled))
                    binding.btnGuardar.isEnabled = false
                }
            }

            binding.btnGuardar.setOnClickListener {
                // Envia el texto escrito al fragment para agregarlo al RecyclerView.
                val reto = binding.etReto.text.toString().trim()
                if (reto.isNotEmpty()) {
                    onGuardar(reto)
                    alertDialog.dismiss()
                }
            }

            binding.btnCancelar.setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
        }
    }
}
