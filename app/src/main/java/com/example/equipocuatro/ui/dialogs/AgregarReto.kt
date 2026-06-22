package com.example.equipocuatro.ui.dialogs

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.equipocuatro.databinding.DialogAgregarRetoBinding
import com.example.equipocuatro.viewmodel.RetoViewModel
import com.example.equipocuatro.model.Reto

class AgregarReto {
    companion object{

        fun showDialgoAgregarReto(
            context: Context,
            retoViewModel: RetoViewModel,
            onGuardar:() -> Unit

        ){
            val inflater = LayoutInflater.from(context)
            val binding = DialogAgregarRetoBinding.inflate(inflater)

            val alertDialog = AlertDialog.Builder(context).create()
            alertDialog.window?.setBackgroundDrawableResource(R.color.transparent)
            alertDialog.setCancelable(false)
            alertDialog.setView(binding.root)


            binding.etReto.addTextChangedListener {
                val reto = binding.etReto.text
                if(reto.isNotEmpty()){
                    binding.btnGuardar.setBackgroundColor(ContextCompat.getColor(context, com.example.equipocuatro.R.color.orange))
                    binding.btnGuardar.isEnabled = true
                } else {
                    binding.btnGuardar.setBackgroundColor(ContextCompat.getColor(context, com.example.equipocuatro.R.color.gray_disabled))
                    binding.btnGuardar.isEnabled = false
                }
            }

            binding.btnGuardar.setOnClickListener {

                val descripcionReto = binding.etReto.text.toString().trim()
                val reto = Reto(descripcion = descripcionReto)
                binding.btnGuardar.isEnabled = false

                retoViewModel.saveReto(reto){message, success ->
                    Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
                    binding.btnGuardar.isEnabled = !success
                    if (success) {
                        onGuardar()
                        alertDialog.dismiss()
                    }
                }
            }

            binding.btnCancelar.setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
        }
    }
}
