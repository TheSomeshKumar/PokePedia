package com.thesomeshkumar.pokepedia.pokemon.domain

import com.thesomeshkumar.pokepedia.core.domain.DataError
import com.thesomeshkumar.pokepedia.core.domain.Result

data class PaginatedPokemon(
    val pokemon: List<Pokemon>,
    val offset: Int,
    val limit: Int,
    val hasNextPage: Boolean,
    val totalCount: Int?
)

/**
 * Created by SixFlags on 27 September, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
 */
interface PokemonRepository {
    suspend fun getPokemon(
        limit: Int = 20,
        offset: Int = 0
    ): Result<PaginatedPokemon, DataError.Remote>

    suspend fun getPokemonDetails(pokemonId: Int): Result<Pokemon, DataError.Remote>
    suspend fun getPokemonDetails(pokemonName: String): Result<Pokemon, DataError.Remote>
    suspend fun searchPokemon(query: String): Result<List<Pokemon>, DataError.Remote>

}
