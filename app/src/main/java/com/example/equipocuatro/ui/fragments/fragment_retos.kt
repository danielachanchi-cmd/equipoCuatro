package com.example.equipocuatro.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.equipocuatro.databinding.FragmentRetosBinding
import com.example.equipocuatro.ui.dialogs.AgregarReto

class fragment_retos : Fragment() {

    private lateinit var binding: FragmentRetosBinding


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
        agregarReto()
    }


    private fun agregarReto(){
        binding.btnAgregar.setOnClickListener {
            AgregarReto.Companion.showDialgoAgregarReto(binding.root.context)
        }
    }
}