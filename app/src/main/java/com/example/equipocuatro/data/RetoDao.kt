package com.example.equipocuatro.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.equipocuatro.model.Reto

@Dao
interface RetoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReto(reto: Reto): Long

    @Query("SELECT * FROM Reto ORDER BY id DESC")
    suspend fun getLisReto(): MutableList<Reto>

    @Query("SELECT * FROM Reto ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomReto(): Reto?

    @Delete
    suspend fun deleteReto(reto: Reto): Int

    @Update
    suspend fun updateReto(reto: Reto): Int
}