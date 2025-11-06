package com.thesomeshkumar.pokepedia.pokemon.fakes

import com.thesomeshkumar.pokepedia.core.domain.DataError
import com.thesomeshkumar.pokepedia.core.domain.Result
import com.thesomeshkumar.pokepedia.pokemon.data.dto.*
import com.thesomeshkumar.pokepedia.pokemon.data.network.RemoteDataSource

/**
 * Fake implementation of RemoteDataSource for testing repositories
 */
class FakeRemoteDataSource : RemoteDataSource {
    
    // Configurable results
    var getPokemonListResult: Result<PokemonListResponse, DataError.Remote>? = null
    var getPokemonDetailsByIdResult: Result<PokemonDetailDTO, DataError.Remote>? = null
    var getPokemonDetailsByNameResult: Result<PokemonDetailDTO, DataError.Remote>? = null
    var getPokemonSpeciesByIdResult: Result<PokemonSpeciesDetailDTO, DataError.Remote>? = null
    var getPokemonSpeciesByNameResult: Result<PokemonSpeciesDetailDTO, DataError.Remote>? = null
    var getEvolutionChainResult: Result<EvolutionChainDTO, DataError.Remote>? = null
    
    // Call tracking
    val getPokemonListCalls = mutableListOf<Pair<Int, Int>>() // limit, offset
    val getPokemonDetailsByIdCalls = mutableListOf<Int>()
    val getPokemonDetailsByNameCalls = mutableListOf<String>()
    val getPokemonSpeciesByIdCalls = mutableListOf<Int>()
    val getPokemonSpeciesByNameCalls = mutableListOf<String>()
    val getEvolutionChainCalls = mutableListOf<Int>()
    
    override suspend fun getPokemonList(limit: Int, offset: Int): Result<PokemonListResponse, DataError.Remote> {
        getPokemonListCalls.add(Pair(limit, offset))
        return getPokemonListResult ?: error("getPokemonListResult not set")
    }
    
    override suspend fun getPokemonDetails(pokemonId: Int): Result<PokemonDetailDTO, DataError.Remote> {
        getPokemonDetailsByIdCalls.add(pokemonId)
        return getPokemonDetailsByIdResult ?: error("getPokemonDetailsByIdResult not set")
    }
    
    override suspend fun getPokemonDetails(pokemonName: String): Result<PokemonDetailDTO, DataError.Remote> {
        getPokemonDetailsByNameCalls.add(pokemonName)
        return getPokemonDetailsByNameResult ?: error("getPokemonDetailsByNameResult not set")
    }
    
    override suspend fun getPokemonSpecies(pokemonId: Int): Result<PokemonSpeciesDetailDTO, DataError.Remote> {
        getPokemonSpeciesByIdCalls.add(pokemonId)
        return getPokemonSpeciesByIdResult ?: error("getPokemonSpeciesByIdResult not set")
    }
    
    override suspend fun getPokemonSpecies(pokemonName: String): Result<PokemonSpeciesDetailDTO, DataError.Remote> {
        getPokemonSpeciesByNameCalls.add(pokemonName)
        return getPokemonSpeciesByNameResult ?: error("getPokemonSpeciesByNameResult not set")
    }
    
    override suspend fun getEvolutionChain(evolutionChainId: Int): Result<EvolutionChainDTO, DataError.Remote> {
        getEvolutionChainCalls.add(evolutionChainId)
        return getEvolutionChainResult ?: error("getEvolutionChainResult not set")
    }
    
    fun reset() {
        getPokemonListResult = null
        getPokemonDetailsByIdResult = null
        getPokemonDetailsByNameResult = null
        getPokemonSpeciesByIdResult = null
        getPokemonSpeciesByNameResult = null
        getEvolutionChainResult = null
        getPokemonListCalls.clear()
        getPokemonDetailsByIdCalls.clear()
        getPokemonDetailsByNameCalls.clear()
        getPokemonSpeciesByIdCalls.clear()
        getPokemonSpeciesByNameCalls.clear()
        getEvolutionChainCalls.clear()
    }
}



