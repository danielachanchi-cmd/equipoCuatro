package com.example.equipocuatro.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.equipocuatro.databinding.FragmentInstruccionesBinding

import com.example.equipocuatro.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Instrucciones : Fragment() {

    private lateinit var binding: FragmentInstruccionesBinding
    private val viewModel: HomeViewModel by activityViewModels()

    // Guarda si la música estaba activa
    private var musicWasPlaying = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInstruccionesBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pauseMusic()

        setupClickListeners()
    }

    // Pausar música al entrar
    private fun pauseMusic() {

        musicWasPlaying = viewModel.isMusicEnabled.value == true

        if (musicWasPlaying) {

            viewModel.toggleMusic()
        }
    }

    // Botón atrás
    private fun setupClickListeners() {

        binding.toolbarInstrucciones.btnBack.setOnClickListener {

            // Restaurar música si originalmente estaba activa
            if (musicWasPlaying) {

                viewModel.toggleMusic()
            }

            // Volver al fragment anterior
            findNavController().popBackStack()
        }
    }
}