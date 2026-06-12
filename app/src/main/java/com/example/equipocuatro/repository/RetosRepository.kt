package com.example.equipocuatro.repository

import android.content.Context
import com.example.equipocuatro.data.RetoDB
import com.example.equipocuatro.data.RetoDao
import com.example.equipocuatro.model.Reto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RetosRepository @Inject constructor(
    private val retoDao: RetoDao
) {
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

    suspend fun getRandomReto(): Reto? {
        return withContext(Dispatchers.IO) {
            retoDao.getRandomReto()
        }
    }

    suspend fun updateReto(reto: Reto, messageResponse: (String) -> Unit) {
        try {
            withContext(Dispatchers.IO) {
                retoDao.updateReto(reto)
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
