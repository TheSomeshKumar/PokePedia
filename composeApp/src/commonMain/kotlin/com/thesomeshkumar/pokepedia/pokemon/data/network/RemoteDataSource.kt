package com.thesomeshkumar.pokepedia.pokemon.data.network

import com.thesomeshkumar.pokepedia.core.domain.DataError
import com.thesomeshkumar.pokepedia.core.domain.Result
import com.thesomeshkumar.pokepedia.pokemon.data.dto.EvolutionChainDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.PokemonDetailDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.PokemonListResponse
import com.thesomeshkumar.pokepedia.pokemon.data.dto.PokemonSpeciesDetailDTO

/**
 * Created by Somesh Kumar on 27 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */
interface RemoteDataSource {
    suspend fun getPokemonList(
        limit: Int = 20,
        offset: Int = 0
    ): Result<PokemonListResponse, DataError.Remote>

    suspend fun getPokemonDetails(pokemonId: Int): Result<PokemonDetailDTO, DataError.Remote>
    suspend fun getPokemonDetails(pokemonName: String): Result<PokemonDetailDTO, DataError.Remote>
    suspend fun getPokemonSpecies(pokemonId: Int): Result<PokemonSpeciesDetailDTO, DataError.Remote>
    suspend fun getPokemonSpecies(pokemonName: String): Result<PokemonSpeciesDetailDTO, DataError.Remote>
    suspend fun getEvolutionChain(evolutionChainId: Int): Result<EvolutionChainDTO, DataError.Remote>
}
