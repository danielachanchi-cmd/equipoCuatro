package com.example.equipocuatro.dialogos


import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.UiContext
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.equipocuatro.R
import com.example.equipocuatro.databinding.DialogAgregarRetoBinding
import com.example.equipocuatro.databinding.FragmentRetosBinding


class AgregarReto {
    companion object{
        fun showDialgoAgregarReto(
            context: Context
        ){
            val inflater = LayoutInflater.from(context)
            val binding = DialogAgregarRetoBinding.inflate(inflater)

            val alertDialog = AlertDialog.Builder(context).create()
            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            alertDialog.setCancelable(false)
            alertDialog.setView(binding.root)

            binding.etReto.addTextChangedListener {
                var reto = binding.etReto.text
                if(reto.isNotEmpty()){
                    binding.btnGuardar.setBackgroundColor(ContextCompat.getColor(context, R.color.orange))
                    binding.btnGuardar.isEnabled = true
                } else {
                    binding.btnGuardar.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_disabled))
                    binding.btnGuardar.isEnabled = false
                }
            }

            binding.btnGuardar.setOnClickListener {
                alertDialog.dismiss()
            }

            binding.btnCancelar.setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
        }
    }
}