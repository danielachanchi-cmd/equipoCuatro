package com.example.equipocuatro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equipocuatro.repository.AuthRepository
import com.example.equipocuatro.utils.Resource
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _res = MutableLiveData<Resource<AuthResult>>()
    val res: LiveData<Resource<AuthResult>> get() = _res

    fun register(email: String, pass: String){
        viewModelScope.launch {
            _res.postValue(Resource.Loading)
            try {
                val authResult = repository.register(email,pass)
                val uid = authResult?.user?.uid

                if (uid != null){
                    repository.createUserInFirestore(uid, email)
                    _res.postValue(Resource.Success(authResult))
                }
            }catch (e: Exception){
                _res.postValue(Resource.Error("Error en el registro"))
            }
        }
    }

    // HU-2.0 C10 P2:
    // Valida las credenciales con Firebase Authentication.
    // Si el login es exitoso se envía Resource.Success para permitir
    // la navegación a la ventana Home Principal.
    fun login(email: String, pass: String){
        viewModelScope.launch {
            _res.postValue(Resource.Loading)

            try {
                val authResult = repository.login(email, pass)

                if (authResult != null) {
                    _res.postValue(Resource.Success(authResult))
                } else {
                    _res.postValue(Resource.Error("Login incorrecto"))
                }
            }catch (e: Exception){
                _res.postValue(Resource.Error("Login incorrecto"))
            }
        }
    }

    fun isUserLoggedIn(): Boolean {
        return repository.getCurrentUser() != null
    }

    fun signOut() {
        repository.signOut()
    }
}
