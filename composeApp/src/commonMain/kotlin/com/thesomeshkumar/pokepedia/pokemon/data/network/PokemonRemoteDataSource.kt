package com.thesomeshkumar.pokepedia.pokemon.data.network

import com.thesomeshkumar.pokepedia.core.data.safeCall
import com.thesomeshkumar.pokepedia.core.domain.DataError
import com.thesomeshkumar.pokepedia.core.domain.Result
import com.thesomeshkumar.pokepedia.pokemon.data.dto.PokemonDetailDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.PokemonListResponse
import com.thesomeshkumar.pokepedia.pokemon.data.dto.PokemonSpeciesDetailDTO
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * Created by SixFlags on 27 September, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
 */

class PokemonRemoteDataSource(
    private val httpClient: HttpClient
) : RemoteDataSource {

    override suspend fun getPokemonList(
        limit: Int,
        offset: Int
    ): Result<PokemonListResponse, DataError.Remote> {
        return safeCall<PokemonListResponse> {
            httpClient.get("pokemon") {
                parameter(
                    "limit",
                    limit
                )
                parameter(
                    "offset",
                    offset
                )
            }
        }
    }

    override suspend fun getPokemonDetails(pokemonId: Int): Result<PokemonDetailDTO, DataError.Remote> {
        return safeCall<PokemonDetailDTO> {
            httpClient.get("pokemon/$pokemonId")
        }
    }

    override suspend fun getPokemonDetails(pokemonName: String): Result<PokemonDetailDTO, DataError.Remote> {
        return safeCall<PokemonDetailDTO> {
            httpClient.get("pokemon/$pokemonName")
        }
    }

    override suspend fun getPokemonSpecies(pokemonId: Int): Result<PokemonSpeciesDetailDTO, DataError.Remote> {
        return safeCall<PokemonSpeciesDetailDTO> {
            httpClient.get("pokemon-species/$pokemonId")
        }
    }

    override suspend fun getPokemonSpecies(pokemonName: String): Result<PokemonSpeciesDetailDTO, DataError.Remote> {
        return safeCall<PokemonSpeciesDetailDTO> {
            httpClient.get("pokemon-species/$pokemonName")
        }
    }
}
