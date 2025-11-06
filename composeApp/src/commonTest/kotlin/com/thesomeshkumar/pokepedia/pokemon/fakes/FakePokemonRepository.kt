package com.thesomeshkumar.pokepedia.pokemon.fakes

import com.thesomeshkumar.pokepedia.core.domain.DataError
import com.thesomeshkumar.pokepedia.core.domain.Result
import com.thesomeshkumar.pokepedia.pokemon.domain.PaginatedPokemon
import com.thesomeshkumar.pokepedia.pokemon.domain.Pokemon
import com.thesomeshkumar.pokepedia.pokemon.domain.PokemonRepository

/**
 * Fake implementation of PokemonRepository for testing.
 * This is the recommended approach for KMP testing instead of mocking libraries.
 * 
 * Allows you to control the behavior and verify interactions manually.
 */
class FakePokemonRepository : PokemonRepository {
    
    // Configurable behavior
    var getPokemonResult: Result<PaginatedPokemon, DataError.Remote>? = null
    var getPokemonDetailsByIdResult: Result<Pokemon, DataError.Remote>? = null
    var getPokemonDetailsByNameResult: Result<Pokemon, DataError.Remote>? = null
    var searchPokemonResult: Result<List<Pokemon>, DataError.Remote>? = null
    
    // Track calls for verification
    val getPokemonCalls = mutableListOf<Pair<Int, Int>>() // limit, offset
    val getPokemonDetailsByIdCalls = mutableListOf<Int>()
    val getPokemonDetailsByNameCalls = mutableListOf<String>()
    val searchPokemonCalls = mutableListOf<String>()
    
    override suspend fun getPokemon(limit: Int, offset: Int): Result<PaginatedPokemon, DataError.Remote> {
        getPokemonCalls.add(Pair(limit, offset))
        return getPokemonResult ?: error("getPokemonResult not set")
    }
    
    override suspend fun getPokemonDetails(pokemonId: Int): Result<Pokemon, DataError.Remote> {
        getPokemonDetailsByIdCalls.add(pokemonId)
        return getPokemonDetailsByIdResult ?: error("getPokemonDetailsByIdResult not set")
    }
    
    override suspend fun getPokemonDetails(pokemonName: String): Result<Pokemon, DataError.Remote> {
        getPokemonDetailsByNameCalls.add(pokemonName)
        return getPokemonDetailsByNameResult ?: error("getPokemonDetailsByNameResult not set")
    }
    
    override suspend fun searchPokemon(query: String): Result<List<Pokemon>, DataError.Remote> {
        searchPokemonCalls.add(query)
        return searchPokemonResult ?: error("searchPokemonResult not set")
    }
    
    // Helper methods
    fun reset() {
        getPokemonResult = null
        getPokemonDetailsByIdResult = null
        getPokemonDetailsByNameResult = null
        searchPokemonResult = null
        getPokemonCalls.clear()
        getPokemonDetailsByIdCalls.clear()
        getPokemonDetailsByNameCalls.clear()
        searchPokemonCalls.clear()
    }
    
    fun verifyGetPokemonCalled(times: Int = 1, limit: Int? = null, offset: Int? = null) {
        val actualCalls = getPokemonCalls.size
        require(actualCalls == times) {
            "Expected getPokemon to be called $times times, but was called $actualCalls times"
        }
        
        if (limit != null || offset != null) {
            val matching = getPokemonCalls.filter { (l, o) ->
                (limit == null || l == limit) && (offset == null || o == offset)
            }
            require(matching.size == times) {
                "Expected getPokemon($limit, $offset) to be called $times times, but was called ${matching.size} times"
            }
        }
    }
    
    fun verifyGetPokemonDetailsById(pokemonId: Int, times: Int = 1) {
        val actualCalls = getPokemonDetailsByIdCalls.count { it == pokemonId }
        require(actualCalls == times) {
            "Expected getPokemonDetails($pokemonId) to be called $times times, but was called $actualCalls times"
        }
    }
    
    fun verifyGetPokemonDetailsByName(pokemonName: String, times: Int = 1) {
        val actualCalls = getPokemonDetailsByNameCalls.count { it == pokemonName }
        require(actualCalls == times) {
            "Expected getPokemonDetails($pokemonName) to be called $times times, but was called $actualCalls times"
        }
    }
    
    fun verifySearchPokemon(query: String, times: Int = 1) {
        val actualCalls = searchPokemonCalls.count { it == query }
        require(actualCalls == times) {
            "Expected searchPokemon($query) to be called $times times, but was called $actualCalls times"
        }
    }
}



