package com.example.equipocuatro.repository

import com.example.equipocuatro.model.Reto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.random.Random
import javax.inject.Inject

class RetosRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val retosCollection = firestore.collection("retos")

    suspend fun saveReto(reto: Reto, messageResponse: (String, Boolean)-> Unit){
        try{
            val retoId = generateRetoId()
            val retoToSave = reto.copy(id = retoId)

            withContext(Dispatchers.IO) {
                retosCollection.document(retoId.toString()).set(retoToSave).await()
            }
            messageResponse("Reto guardado correctamente", true)
        } catch (e: Exception){
            messageResponse("Error al guardar el reto: ${e.message}", false)
        }
    }

    suspend fun getListReto(): MutableList<Reto> {
        return withContext(Dispatchers.IO) {
            retosCollection
                .orderBy("id")
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    val id = document.getLong("id")?.toInt() ?: document.id.toIntOrNull()
                    val descripcion = document.getString("descripcion")

                    if (id != null && !descripcion.isNullOrBlank()) {
                        Reto(id = id, descripcion = descripcion)
                    } else {
                        null
                    }
                }
                .asReversed()
                .toMutableList()
        }
    }

    suspend fun getRandomReto(): Reto? {
        return withContext(Dispatchers.IO) {
            getListReto().randomOrNull()
        }
    }

    suspend fun updateReto(reto: Reto): Result<Unit> {
        try {
            withContext(Dispatchers.IO) {
                retosCollection.document(reto.id.toString()).set(reto).await()
            }
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun deleteReto(reto: Reto): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                retosCollection.document(reto.id.toString()).delete().await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun generateRetoId(): Int {
        val timestampId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
        return if (timestampId != 0) timestampId else Random.nextInt(1, Int.MAX_VALUE)
    }
}
