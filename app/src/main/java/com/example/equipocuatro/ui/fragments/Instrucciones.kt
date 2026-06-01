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

class Instrucciones : Fragment() {

    private lateinit var binding: FragmentInstruccionesBinding
    private val viewModel: HomeViewModel by activityViewModels()
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
        viewModel.cancelActiveGame()
        pauseMusic()
        setupClickListeners()
    }

    private fun pauseMusic() {
        musicWasPlaying = viewModel.isMusicEnabled.value == true
        if (musicWasPlaying) {
            viewModel.toggleMusic()
        }
    }

    private fun setupClickListeners() {
        binding.toolbarInstrucciones.btnBack.setOnClickListener {
            if (musicWasPlaying) {
                viewModel.toggleMusic()
            }
            findNavController().popBackStack()
        }
    }
}
