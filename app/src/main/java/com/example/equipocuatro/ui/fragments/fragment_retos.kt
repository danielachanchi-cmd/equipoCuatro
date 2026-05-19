package com.example.equipocuatro.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.equipocuatro.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.equipocuatro.databinding.FragmentRetosBinding
import com.example.equipocuatro.ui.adapter.RetoAdapter
import com.example.equipocuatro.ui.dialogs.AgregarReto
import com.example.equipocuatro.ui.dialogs.EditarReto
import com.example.equipocuatro.ui.dialogs.EliminarReto
import com.example.equipocuatro.model.Reto
import com.example.equipocuatro.viewmodel.RetoViewModel

class fragment_retos : Fragment() {

    private lateinit var binding: FragmentRetosBinding
    private lateinit var retoAdapter: RetoAdapter
    private val retoViewModel: RetoViewModel by viewModels()
    // Lista temporal de retos que se muestra en pantalla.
    private val retos = mutableListOf<Reto>()


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
        configurarRecyclerView()
        retoViewModel.getListReto()
        retoViewModel.listReto.observe(viewLifecycleOwner){listaActualizada->
            retoAdapter.actualizarRetos(listaActualizada)
        }
        agregarReto()
        volver()

    }

    private fun configurarRecyclerView() {
        // Configura el adapter y las acciones de editar/eliminar.
        retoAdapter = RetoAdapter(
            retos = mutableListOf(),
            onEditarClick = { reto -> mostrarDialogoEditar(reto) },
            onEliminarClick = { reto -> mostrarDialogoEliminar(reto) }
        )

        // Conecta el RecyclerView con su layoutManager y adapter.
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
            findNavController().navigate(R.id.action_fragment_retos_to_home_principal24)
        }
    }
}
