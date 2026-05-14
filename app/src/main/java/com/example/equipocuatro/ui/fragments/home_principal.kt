package com.example.equipocuatro.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.equipocuatro.R
import com.example.equipocuatro.databinding.FragmentHomePrincipalBinding

class home_principal : Fragment() {

    private var _binding: FragmentHomePrincipalBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePrincipalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Criterio 2 y HU-4.0: Calificar la app (Redirigir a Nequi en Play Store)
        binding.toolbarHome.starButton.setOnClickListener {
            val playStoreUrl = "https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(playStoreUrl))
            startActivity(intent)
        }

        // Criterio 4 y HU-5.0: Instrucciones del juego
        binding.toolbarHome.controlButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, Instrucciones())
                .addToBackStack(null) // Permite volver al home con el botón atrás
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}