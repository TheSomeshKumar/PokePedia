package com.thesomeshkumar.pokepedia.pokemon.data.repository

import com.thesomeshkumar.pokepedia.core.domain.DataError
import com.thesomeshkumar.pokepedia.core.domain.Result
import com.thesomeshkumar.pokepedia.pokemon.data.database.PokemonDao
import com.thesomeshkumar.pokepedia.pokemon.data.mappers.toDomain
import com.thesomeshkumar.pokepedia.pokemon.data.network.RemoteDataSource
import com.thesomeshkumar.pokepedia.pokemon.domain.PaginatedPokemon
import com.thesomeshkumar.pokepedia.pokemon.domain.Pokemon
import com.thesomeshkumar.pokepedia.pokemon.domain.PokemonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Created by SixFlags on 27 September, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
 */

class DefaultPokemonRepository(
    private val remoteDataSource: RemoteDataSource,
    private val pokemonDao: PokemonDao
) : PokemonRepository {

    override suspend fun getPokemon(
        limit: Int,
        offset: Int
    ): Result<PaginatedPokemon, DataError.Remote> {
        return when (val result = remoteDataSource.getPokemonList(
            limit,
            offset
        )) {
            is Result.Error -> Result.Error(result.error)
            is Result.Success -> {
                // Use only basic data from the list endpoint - no additional detail calls
                val pokemonList = result.data.results.map { it.toDomain() }

                val paginatedPokemon = PaginatedPokemon(
                    pokemon = pokemonList,
                    offset = offset,
                    limit = limit,
                    hasNextPage = result.data.next != null,
                    totalCount = result.data.count
                )

                Result.Success(paginatedPokemon)
            }
        }
    }

    override suspend fun getPokemonDetails(pokemonId: Int): Result<Pokemon, DataError.Remote> {
        return coroutineScope {
            // Parallelize independent API calls for better performance
            val pokemonDeferred = async { remoteDataSource.getPokemonDetails(pokemonId) }
            val speciesDeferred = async { remoteDataSource.getPokemonSpecies(pokemonId) }

            // Await pokemon details - if it fails, return early
            val pokemonResult = pokemonDeferred.await()
            if (pokemonResult is Result.Error) {
                return@coroutineScope Result.Error(pokemonResult.error)
            }

            // Await species details (non-blocking since it's already running)
            val speciesResult = speciesDeferred.await()
            val speciesData = if (speciesResult is Result.Success) {
                speciesResult.data
            } else null

            // Get evolution chain (depends on species data, so must be sequential)
            val evolutionChainData = speciesData?.evolutionChain?.let { chainLink ->
                val chainResult = remoteDataSource.getEvolutionChain(chainLink.evolutionChainId)
                if (chainResult is Result.Success) chainResult.data else null
            }

            val pokemonData = (pokemonResult as Result.Success).data
            val pokemon = pokemonData.toDomain(speciesData, evolutionChainData)
            Result.Success(pokemon)
        }
    }

    override suspend fun getPokemonDetails(pokemonName: String): Result<Pokemon, DataError.Remote> {
        return coroutineScope {
            // Parallelize independent API calls for better performance
            val pokemonDeferred = async { remoteDataSource.getPokemonDetails(pokemonName) }
            val speciesDeferred = async { remoteDataSource.getPokemonSpecies(pokemonName) }

            // Await pokemon details - if it fails, return early
            val pokemonResult = pokemonDeferred.await()
            if (pokemonResult is Result.Error) {
                return@coroutineScope Result.Error(pokemonResult.error)
            }

            // Await species details (non-blocking since it's already running)
            val speciesResult = speciesDeferred.await()
            val speciesData = if (speciesResult is Result.Success) {
                speciesResult.data
            } else null

            // Get evolution chain (depends on species data, so must be sequential)
            val evolutionChainData = speciesData?.evolutionChain?.let { chainLink ->
                val chainResult = remoteDataSource.getEvolutionChain(chainLink.evolutionChainId)
                if (chainResult is Result.Success) chainResult.data else null
            }

            val pokemonData = (pokemonResult as Result.Success).data
            val pokemon = pokemonData.toDomain(speciesData, evolutionChainData)
            Result.Success(pokemon)
        }
    }

    override suspend fun searchPokemon(query: String): Result<List<Pokemon>, DataError.Remote> {
        // For now, we'll search in the local database
        // In a real app, you might want to implement server-side search
        return try {
            val localResults = pokemonDao.searchPokemon(query.lowercase())
            val pokemon = localResults.map { it.toDomain() }
            Result.Success(pokemon)
        } catch (e: Exception) {
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

}
