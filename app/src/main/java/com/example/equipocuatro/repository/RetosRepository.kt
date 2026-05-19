package com.example.equipocuatro.repository

import android.content.Context
import com.example.equipocuatro.data.RetoDB
import com.example.equipocuatro.data.RetoDao
import com.example.equipocuatro.model.Reto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetosRepository (val context: Context) {

    private var retoDao:RetoDao = RetoDB.getDatabase(context).retoDao()

    suspend fun saveReto(reto: Reto, messageResponse: (String)-> Unit){
        try{
            withContext(Dispatchers.IO){
                retoDao.saveReto(reto)
            }
            messageResponse("Reto guardado correctamente")
        } catch (e: Exception){
            messageResponse("Error al guardar el reto: ${e.message}")
        }
    }

    suspend fun getListReto(): MutableList<Reto> {
        return withContext(Dispatchers.IO){
            retoDao.getLisReto()
        }
    }

    suspend fun updateReto(reto: Reto, messageResponse: (String) -> Unit) {
        try {
            withContext(Dispatchers.IO) {
                ret oDao.updateReto(reto)
            }
            messageResponse("Reto actualizado correctamente")
        } catch (e: Exception) {
            messageResponse("Error al actualizar el reto: ${e.message}")
        }
    }

    suspend fun deleteReto(reto: Reto, messageResponse: (String) -> Unit) {
        try {
            withContext(Dispatchers.IO) {
                retoDao.deleteReto(reto)
            }
            messageResponse("Reto eliminado correctamente")
        } catch (e: Exception) {
            messageResponse("Error al eliminar el reto: ${e.message}")
        }
    }
}
