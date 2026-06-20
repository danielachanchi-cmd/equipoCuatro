package com.example.equipocuatro.ui.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.equipocuatro.R

class EditarReto {
    companion object {
        fun showDialogoEditarReto(
            context: Context,
            retoActual: String = "",
            onGuardar: (String, (Boolean) -> Unit) -> Unit
        ) {
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_editar_reto, null)
            val etRetoEdit = view.findViewById<EditText>(R.id.etRetoEdit)
            val btnCancelarEdit = view.findViewById<Button>(R.id.btnCancelarEdit)
            val btnGuardarEdit = view.findViewById<Button>(R.id.btnGuardarEdit)

            val alertDialog = AlertDialog.Builder(context).create()
            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            alertDialog.setCancelable(false)
            alertDialog.setView(view)

            fun actualizarBotonGuardar() {
                val tieneReto = etRetoEdit.text.toString().trim().isNotEmpty()
                val color = if (tieneReto) R.color.orange else R.color.gray_disabled

                btnGuardarEdit.setBackgroundColor(ContextCompat.getColor(context, color))
                btnGuardarEdit.isEnabled = tieneReto
            }

            etRetoEdit.setText(retoActual)
            etRetoEdit.setSelection(etRetoEdit.text.length)
            actualizarBotonGuardar()

            etRetoEdit.addTextChangedListener {
                actualizarBotonGuardar()
            }

            btnGuardarEdit.setOnClickListener {
                val retoEditado = etRetoEdit.text.toString().trim()
                if (retoEditado.isNotEmpty()) {
                    btnGuardarEdit.isEnabled = false
                    onGuardar(retoEditado) { guardadoExitoso ->
                        if (guardadoExitoso) {
                            alertDialog.dismiss()
                        } else {
                            actualizarBotonGuardar()
                        }
                    }
                }
            }

            btnCancelarEdit.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()
        }
    }
}
