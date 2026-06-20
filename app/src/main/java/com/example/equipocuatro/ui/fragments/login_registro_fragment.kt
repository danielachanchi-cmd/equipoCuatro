package com.example.equipocuatro.ui.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.equipocuatro.R
import com.example.equipocuatro.databinding.FragmentLoginRegistroFragmentBinding
import com.example.equipocuatro.utils.Resource
import com.example.equipocuatro.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class login_registro_fragment : Fragment() {

    private  lateinit var binding: FragmentLoginRegistroFragmentBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginRegistroFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return  binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (authViewModel.isUserLoggedIn()) {
            findNavController().navigate(R.id.action_login_registro_fragment_to_home_principal2)
        }
        setupListeners()
        validadrCampos()
        registrar()
    }

    private fun registrar(){
        binding.tvRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val  password = binding.etPassword.text.toString().trim()
            authViewModel.register(email,password)
        }
        authViewModel.res.observe(viewLifecycleOwner){resource ->
            when(resource){
                is Resource.Loading->{
                    binding.progress.visibility = View.VISIBLE
                    binding.tvRegister.isEnabled= false
                    binding.tvRegister.alpha=0.5f
                }
                is Resource.Success ->{
                    binding.progress.visibility = View.GONE
                    binding.tvRegister.isEnabled = true
                    binding.tvRegister.alpha = 1f
                    findNavController().navigate(R.id.action_login_registro_fragment_to_home_principal2)
                }
                is Resource.Error ->{
                    binding.progress.visibility = View.GONE
                    binding.tvRegister.isEnabled = true
                    binding.tvRegister.alpha = 1f
                    Toast.makeText(requireContext(),"Error en el registro", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validadrCampos(){
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val esValido = email.isNotEmpty() && password.length in 6..10

        binding.tvRegister.apply {
            isEnabled = esValido
            isClickable = esValido

            if (esValido){
                setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                setTypeface(null, Typeface.BOLD)
            }else{
                setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text))
                setTypeface(null, Typeface.NORMAL)
            }
        }
        binding.btnLogin.isEnabled =
            email.isNotEmpty() && password.isNotEmpty()
    }

    private fun validarPasswordHU25() {

        val password = binding.etPassword.text.toString()

        when {

            password.isEmpty() -> {
                binding.tvPasswordError.visibility = View.GONE

                binding.tilPassword.boxStrokeColor =
                    ContextCompat.getColor(requireContext(), android.R.color.white)
            }

            password.length < 6 -> {

                binding.tvPasswordError.visibility = View.VISIBLE

                binding.tilPassword.boxStrokeColor =
                    ContextCompat.getColor(requireContext(), android.R.color.holo_red_light)
            }

            else -> {

                binding.tvPasswordError.visibility = View.GONE

                binding.tilPassword.boxStrokeColor =
                    ContextCompat.getColor(requireContext(), android.R.color.white)
            }
        }
    }

    private fun setupListeners() {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validadrCampos()
                validarPasswordHU25()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etEmail.addTextChangedListener(watcher)
        binding.etPassword.addTextChangedListener(watcher)
    }
}