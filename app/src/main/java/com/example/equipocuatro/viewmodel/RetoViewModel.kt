package com.example.equipocuatro.viewmodel



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equipocuatro.model.Reto
import com.example.equipocuatro.repository.RetosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RetoViewModel @Inject constructor(
    private val retoRepository: RetosRepository
) : ViewModel() {


    private val _listReto = MutableLiveData<MutableList<Reto>>()
    val listReto: LiveData<MutableList<Reto>> get() = _listReto

    private val _progesState = MutableLiveData(false)
    val progresState: LiveData<Boolean> = _progesState

    fun saveReto(reto: Reto, message:(String, Boolean)->Unit){
        viewModelScope.launch {
            _progesState.value = true
            try {
                var responseMessage = ""
                var responseSuccess = false
                retoRepository.saveReto(reto){msg, success ->
                    responseMessage = msg
                    responseSuccess = success
                }
                if (responseSuccess) {
                    _listReto.value = retoRepository.getListReto()
                }
                message(responseMessage, responseSuccess)
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

    fun updateReto(reto: Reto, message: (String, Boolean) -> Unit) {
        viewModelScope.launch {
            _progesState.value = true
            try {
                val result = retoRepository.updateReto(reto)
                if (result.isSuccess) {
                    _listReto.value = retoRepository.getListReto()
                    message("Reto actualizado correctamente", true)
                } else {
                    val error = result.exceptionOrNull()?.message.orEmpty()
                    message("Error al actualizar el reto: $error", false)
                }
            } catch (e: Exception) {
                message("Error al actual" +
                        "izar el reto: ${e.message}", false)
            } finally {
                _progesState.value = false
            }
        }
    }

    fun deleteReto(reto: Reto, message: (String, Boolean) -> Unit) {
        viewModelScope.launch {
            _progesState.value = true
            try {
                val result = retoRepository.deleteReto(reto)
                if (result.isSuccess) {
                    _listReto.value = retoRepository.getListReto()
                    message("Reto eliminado correctamente", true)
                } else {
                    val error = result.exceptionOrNull()?.message.orEmpty()
                    message("Error al eliminar el reto: $error", false)
                }
            } catch (e: Exception) {
                message("Error al eliminar el reto: ${e.message}", false)
            } finally {
                _progesState.value = false
            }
        }
    }
}
