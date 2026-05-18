package com.example.equipocuatro.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
data class Reto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val descripcion: String
): Serializable