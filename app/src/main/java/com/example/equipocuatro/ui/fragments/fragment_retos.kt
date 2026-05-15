package com.example.equipocuatro.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.equipocuatro.databinding.FragmentRetosBinding
import com.example.equipocuatro.ui.adapter.RetoAdapter
import com.example.equipocuatro.ui.dialogs.AgregarReto
import com.example.equipocuatro.ui.dialogs.EditarReto
import com.example.equipocuatro.ui.dialogs.EliminarReto
import com.example.equipocuatro.viewmodel.Reto

class fragment_retos : Fragment() {

    private lateinit var binding: FragmentRetosBinding
    private lateinit var retoAdapter: RetoAdapter
    // Lista temporal de retos que se muestra en pantalla.
    private val retos = mutableListOf<Reto>()
    private var siguienteId = 1

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
        agregarReto()
    }

    private fun configurarRecyclerView() {
        // Configura el adapter y las acciones de editar/eliminar.
        retoAdapter = RetoAdapter(
            retos = retos,
            onEditarClick = { reto ->
                mostrarDialogoEditar(reto)
            },
            onEliminarClick = { reto ->
                mostrarDialogoEliminar(reto)
            }
        )

        // Conecta el RecyclerView con su layoutManager y adapter.
        binding.rvRetos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = retoAdapter
        }
    }

    private fun agregarReto(){
        binding.btnAgregar.setOnClickListener {
            AgregarReto.Companion.showDialgoAgregarReto(requireContext()) { descripcion ->
                // Agrega el nuevo reto a la lista y actualiza el RecyclerView.
                retos.add(
                    Reto(
                        id = siguienteId++,
                        descripcion = descripcion
                    )
                )
                retoAdapter.notifyItemInserted(retos.lastIndex)
            }
        }
    }

    private fun mostrarDialogoEditar(reto: Reto) {
        // Abre el dialogo de editar y actualiza
        EditarReto.showDialogoEditarReto(
            context = requireContext(),
            retoActual = reto.descripcion
        ) { descripcionEditada ->
            val posicion = retos.indexOfFirst { it.id == reto.id }
            if (posicion != -1) {
                retos[posicion] = reto.copy(descripcion = descripcionEditada)
                retoAdapter.notifyItemChanged(posicion)
            }
        }
    }

    private fun mostrarDialogoEliminar(reto: Reto) {
        // Abre el dialogo de eliminar y lo elimina
        EliminarReto.showDialogoEliminarReto(
            context = requireContext(),
            reto = reto.descripcion
        ) {
            val posicion = retos.indexOfFirst { it.id == reto.id }
            if (posicion != -1) {
                retos.removeAt(posicion)
                retoAdapter.notifyItemRemoved(posicion)
            }
        }
    }
}
