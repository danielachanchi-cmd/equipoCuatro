package com.example.equipocuatro

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RetosActivity : AppCompatActivity() {

    private lateinit var adapter: RetosAdapter
    private lateinit var dbHelper: RetosDbHelper
    private val listaRetos = mutableListOf<Reto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retos)

        dbHelper = RetosDbHelper(this)
        listaRetos.addAll(dbHelper.obtenerRetos())

        val rvRetos = findViewById<RecyclerView>(R.id.rvRetos)
        val fabAddReto = findViewById<FloatingActionButton>(R.id.fabAddReto)

        adapter = RetosAdapter(
            listaRetos,
            onEditClick = { reto -> showEditRetoDialog(reto) },
            onDeleteClick = { reto -> showDeleteRetoDialog(reto) }
        )
        rvRetos.layoutManager = LinearLayoutManager(this)
        rvRetos.adapter = adapter

        fabAddReto.setOnClickListener {
            showAddRetoDialog()
        }

        findViewById<View>(R.id.btnBack)?.setOnClickListener {
            finish()
        }
    }

    private fun showAddRetoDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_agregar_reto, null)
        val etReto = dialogView.findViewById<EditText>(R.id.etReto)
        val btnCancelar = dialogView.findViewById<Button>(R.id.btnCancelar)
        val btnGuardar = dialogView.findViewById<Button>(R.id.btnGuardar)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        etReto.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isNotEmpty = s.toString().trim().isNotEmpty()
                btnGuardar.isEnabled = isNotEmpty
                val color = if (isNotEmpty) R.color.orange else R.color.gray_disabled
                btnGuardar.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this@RetosActivity, color))
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        btnGuardar.setOnClickListener {
            val description = etReto.text.toString().trim()
            if (description.isNotEmpty()) {
                dbHelper.insertarReto(description)
                refreshRetos()
                dialog.dismiss()
            }
        }

        dialog.show()
        dialog.setCanceledOnTouchOutside(false)
    }

    private fun showEditRetoDialog(reto: Reto) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_editar_reto, null)
        val etReto = dialogView.findViewById<EditText>(R.id.etRetoEdit)
        val btnCancelar = dialogView.findViewById<Button>(R.id.btnCancelarEdit)
        val btnGuardar = dialogView.findViewById<Button>(R.id.btnGuardarEdit)

        etReto.setText(reto.descripcion)
        etReto.setSelection(etReto.text.length)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        btnGuardar.setOnClickListener {
            val updatedDescription = etReto.text.toString().trim()
            if (updatedDescription.isNotEmpty()) {
                dbHelper.actualizarReto(reto.copy(descripcion = updatedDescription))
                refreshRetos()
                dialog.dismiss()
            }
        }

        dialog.show()
        dialog.setCanceledOnTouchOutside(false)
    }

    private fun showDeleteRetoDialog(reto: Reto) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_eliminar_reto, null)
        val tvDescription = dialogView.findViewById<TextView>(R.id.tvRetoDeleteDescription)
        val btnNo = dialogView.findViewById<Button>(R.id.btnNoDelete)
        val btnSi = dialogView.findViewById<Button>(R.id.btnSiDelete)

        tvDescription.text = reto.descripcion

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        btnSi.setOnClickListener {
            dbHelper.eliminarReto(reto.id)
            refreshRetos()
            dialog.dismiss()
        }

        dialog.show()
        dialog.setCanceledOnTouchOutside(false)
    }

    private fun refreshRetos() {
        adapter.updateData(dbHelper.obtenerRetos())
    }
}
