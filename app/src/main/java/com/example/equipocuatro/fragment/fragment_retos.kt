package com.example.equipocuatro.fragment

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.equipocuatro.R
import com.example.equipocuatro.databinding.FragmentRetosBinding
import com.example.equipocuatro.dialogos.AgregarReto.Companion.showDialgoAgregarReto


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
            showDialgoAgregarReto(binding.root.context)
        }
    }
}