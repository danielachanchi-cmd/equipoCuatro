package com.example.equipocuatro.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.equipocuatro.databinding.FragmentRetosBinding
import com.example.equipocuatro.ui.adapter.RetoAdapter
import com.example.equipocuatro.ui.dialogs.AgregarReto
import com.example.equipocuatro.ui.dialogs.EditarReto
import com.example.equipocuatro.ui.dialogs.EliminarReto
import com.example.equipocuatro.model.Reto
import com.example.equipocuatro.viewmodel.RetoViewModel
import com.example.equipocuatro.viewmodel.HomeViewModel

class fragment_retos : Fragment() {

    private lateinit var binding: FragmentRetosBinding
    private lateinit var retoAdapter: RetoAdapter
    private val retoViewModel: RetoViewModel by activityViewModels()
    private val musicViewModel: HomeViewModel by activityViewModels()
    private var musicaEstabaOn = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRetosBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controlarMusica()
        configurarRecyclerView()
        setupObservers()
        agregarReto()
        volver()

        retoViewModel.getListReto()
    }

    private fun controlarMusica() {
        musicaEstabaOn = musicViewModel.isMusicEnabled.value == true
        if (musicaEstabaOn) {
            musicViewModel.toggleMusic()
        }
    }

    private fun setupObservers() {
        observerProgress()
        retoViewModel.listReto.observe(viewLifecycleOwner) { listaActualizada ->
            retoAdapter.actualizarRetos(listaActualizada)
        }
    }

    private fun configurarRecyclerView() {
        retoAdapter = RetoAdapter(
            retos = mutableListOf(),
            onEditarClick = { reto -> mostrarDialogoEditar(reto) },
            onEliminarClick = { reto -> mostrarDialogoEliminar(reto) }
        )

        binding.rvRetos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = retoAdapter
        }
    }

    private fun agregarReto(){
        binding.btnAgregar.setOnClickListener {
            AgregarReto.showDialgoAgregarReto(requireContext()){
                retoViewModel.getListReto()
            }
        }
    }

    private fun mostrarDialogoEditar(reto: Reto) {

        EditarReto.showDialogoEditarReto(
            context = requireContext(),
            retoActual = reto.descripcion
        ) { descripcionEditada ->
            retoViewModel.updateReto(reto.copy(descripcion = descripcionEditada)) { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarDialogoEliminar(reto: Reto) {

        EliminarReto.showDialogoEliminarReto(
            context = requireContext(),
            reto = reto.descripcion
        ) {
            retoViewModel.deleteReto(reto) { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun volver(){
        binding.toolbarRetos.btnBack.setOnClickListener {
            val musicaEstaAhoraOff = musicViewModel.isMusicEnabled.value == false


            if (musicaEstabaOn && musicaEstaAhoraOff) {
                musicViewModel.toggleMusic() 
            }
            findNavController().popBackStack()
        }
    }

    private fun observerProgress(){
        retoViewModel.progresState.observe(viewLifecycleOwner){status ->
            binding.progress.isVisible = status
        }
    }
}
