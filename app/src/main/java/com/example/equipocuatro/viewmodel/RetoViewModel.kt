package com.example.equipocuatro.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.equipocuatro.model.Reto
import com.example.equipocuatro.repository.RetosRepository
import kotlinx.coroutines.launch

class RetoViewModel(application: Application): AndroidViewModel(application) {
    val context = getApplication<Application>()
    private val retoRepository = RetosRepository(context)

    private val _listReto = MutableLiveData<MutableList<Reto>>()
    val listReto: LiveData<MutableList<Reto>> get() = _listReto

    private val _progesState = MutableLiveData(false)
    val progresState: LiveData<Boolean> = _progesState

    fun saveReto(reto: Reto, message:(String)->Unit){
        viewModelScope.launch {
            _progesState.value = true
            try {
                retoRepository.saveReto(reto){msg ->
                    message(msg)
                }
                _progesState.value = false
            }catch (e: Exception){
                _progesState.value = false
            }
        }
    }

    fun getListReto(){
        viewModelScope.launch {
            _progesState.value = true
            try{
                _listReto.value = retoRepository.getListReto()
                _progesState.value = false
            }catch (e: Exception){
                _progesState.value = false
            }
        }
    }

    fun updateReto(reto: Reto, message: (String) -> Unit) {
        viewModelScope.launch {
            _progesState.value = true
            try {
                retoRepository.updateReto(reto) { msg ->
                    message(msg)
                }
                _listReto.value = retoRepository.getListReto()
                _progesState.value = false
            } catch (e: Exception) {
                _progesState.value = false
            }
        }
    }

    fun deleteReto(reto: Reto, message: (String) -> Unit) {
        viewModelScope.launch {
            _progesState.value = true
            try {
                retoRepository.deleteReto(reto) { msg ->
                    message(msg)
                }
                _listReto.value = retoRepository.getListReto()
                _progesState.value = false
            } catch (e: Exception) {
                _progesState.value = false
            }
        }
    }
}
