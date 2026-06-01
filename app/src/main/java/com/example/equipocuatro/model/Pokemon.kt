package com.example.equipocuatro.model

import com.google.gson.annotations.SerializedName

data class PokedexResponse(
    @SerializedName("pokemon")
    val pokemon: List<Pokemon>
)

data class Pokemon(
    val id: Int,
    val name: String,
    val img: String
)
