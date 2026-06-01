package com.example.equipocuatro.repository

import com.example.equipocuatro.data.RetrofitClient
import com.example.equipocuatro.model.Pokemon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class PokemonRepository {

    private var cachedPokemon: List<Pokemon>? = null

    suspend fun getRandomPokemon(): Pokemon {
        return withContext(Dispatchers.IO) {
            try {
                val list = cachedPokemon ?: RetrofitClient.pokemonApi.getPokedex().pokemon.also {
                    cachedPokemon = it
                }
                if (list.isNotEmpty()) {
                    val selected = list[Random.nextInt(list.size)]
                    return@withContext selected.copy(img = resolveImageUrl(selected.img, selected.id))
                }
            } catch (_: Exception) {
                // API del PDF no disponible: usar sprite HTTPS de respaldo
            }
            fallbackPokemon()
        }
    }

    private fun resolveImageUrl(img: String, id: Int): String {
        val normalized = img.trim().replace("http://", "https://")
        if (normalized.startsWith("https://")) return normalized
        return pokeApiSpriteUrl(id)
    }

    private fun fallbackPokemon(): Pokemon {
        val id = Random.nextInt(1, 152)
        return Pokemon(
            id = id,
            name = "Pokemon",
            img = pokeApiSpriteUrl(id)
        )
    }

    private fun pokeApiSpriteUrl(id: Int): String =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}
