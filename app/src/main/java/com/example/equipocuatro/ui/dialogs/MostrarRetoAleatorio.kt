package com.example.equipocuatro.ui.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import com.example.equipocuatro.R
import com.example.equipocuatro.model.ChallengeDialogData
import com.example.equipocuatro.viewmodel.HomeViewModel

class MostrarRetoAleatorio {
    companion object {
        fun showDialogoRetoAleatorio(
            context: Context,
            data: ChallengeDialogData
        ) {
            val homeViewModel = ViewModelProvider(context as AppCompatActivity)[HomeViewModel::class.java]
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_mostrar_reto_aleatorio, null)
            val tvReto = view.findViewById<TextView>(R.id.tvRetoAleatorio)
            val ivPokemon = view.findViewById<ImageView>(R.id.ivPokemon)
            val btnCerrar = view.findViewById<android.widget.Button>(R.id.btnCerrarReto)

            tvReto.text = data.retoDescription

            // Precarga mientras se muestra el diálogo (menos espera visible, HU 12)
            context.imageLoader.enqueue(
                ImageRequest.Builder(context)
                    .data(data.pokemonImageUrl)
                    .build()
            )

            val alertDialog = AlertDialog.Builder(context).create()
            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            alertDialog.setCancelable(false)
            alertDialog.setView(view)

            ivPokemon.load(data.pokemonImageUrl) {
                crossfade(true)
                placeholder(R.drawable.beer)
                error(R.drawable.beer)
            }

            btnCerrar.setOnClickListener {
                alertDialog.dismiss()
                homeViewModel.onChallengeDialogClosed()
            }

            alertDialog.show()
        }
    }
}
