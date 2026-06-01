package com.example.equipocuatro.data

import com.example.equipocuatro.model.PokedexResponse
import retrofit2.http.GET

interface PokemonApiService {
    @GET("Biuni/PokemonGO-Pokedex/master/pokedex.json")
    suspend fun getPokedex(): PokedexResponse
}
