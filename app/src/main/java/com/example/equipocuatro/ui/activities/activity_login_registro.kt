package com.example.equipocuatro.ui.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.equipocuatro.R
import com.example.equipocuatro.databinding.ActivityLoginRegistroBinding
import com.example.equipocuatro.model.UserRequest
import com.example.equipocuatro.utils.Resource
import com.example.equipocuatro.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class activity_login_registro : AppCompatActivity() {

    private  lateinit var binding: ActivityLoginRegistroBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (authViewModel.isUserLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_registro)
        setupListeners()
        validadrCampos()
        registrar()
        login()
    }

    private fun registrar(){
        binding.tvRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val userRequest = UserRequest(email,password)
            authViewModel.register(userRequest)
        }
        authViewModel.res.observe(this){resource ->
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
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is Resource.Error ->{
                    binding.progress.visibility = View.GONE
                    binding.tvRegister.isEnabled = true
                    binding.tvRegister.alpha = 1f

                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // HU-2.0 C10 P2:
    // Obtiene las credenciales ingresadas por el usuario
    // y solicita la autenticación mediante AuthViewModel.
    private fun login() {

        binding.btnLogin.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            authViewModel.login(email, password)
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
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setTypeface(null, Typeface.BOLD)
            }else{
                setTextColor(ContextCompat.getColor(context, R.color.gray_text))
                setTypeface(null, Typeface.NORMAL)
            }
        }
        //Deshabilitar boton login
        binding.btnLogin.isEnabled =
            email.isNotEmpty() && password.length in 6..10

        //Habilitarse y color blanco
        if (binding.btnLogin.isEnabled) {

            binding.btnLogin.setTextColor(
                ContextCompat.getColor(this, R.color.white)
            )

            binding.btnLogin.setTypeface(null, Typeface.BOLD)

        } else {

            binding.btnLogin.setTextColor(
                ContextCompat.getColor(this, R.color.gray_text)
            )

            binding.btnLogin.setTypeface(null, Typeface.NORMAL)
        }
    }

    private fun validarPasswordHU25() {

        val password = binding.etPassword.text.toString()

        when {

            password.isEmpty() -> {
                binding.tvPasswordError.visibility = View.GONE

                binding.tilPassword.boxStrokeColor =
                    ContextCompat.getColor(this, android.R.color.white)
            }

            password.length < 6 -> {

                binding.tvPasswordError.visibility = View.VISIBLE

                binding.tilPassword.boxStrokeColor =
                    ContextCompat.getColor(this, android.R.color.holo_red_light)
            }

            else -> {

                binding.tvPasswordError.visibility = View.GONE

                binding.tilPassword.boxStrokeColor =
                    ContextCompat.getColor(this, android.R.color.white)
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