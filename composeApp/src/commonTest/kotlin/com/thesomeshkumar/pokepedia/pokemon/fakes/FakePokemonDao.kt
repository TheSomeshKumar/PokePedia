package com.thesomeshkumar.pokepedia.pokemon.fakes

import com.thesomeshkumar.pokepedia.pokemon.data.database.PokemonDao
import com.thesomeshkumar.pokepedia.pokemon.data.database.PokemonEntity

/**
 * Fake implementation of PokemonDao for testing repositories
 */
class FakePokemonDao : PokemonDao {
    
    // In-memory storage
    private val pokemonEntities = mutableMapOf<Int, PokemonEntity>()
    
    // Call tracking
    val insertPokemonCalls = mutableListOf<PokemonEntity>()
    val insertPokemonListCalls = mutableListOf<List<PokemonEntity>>()
    val getPokemonByIdCalls = mutableListOf<Int>()
    val deletePokemonCalls = mutableListOf<Int>()
    val searchPokemonCalls = mutableListOf<String>()
    
    override suspend fun insertPokemon(pokemon: PokemonEntity) {
        insertPokemonCalls.add(pokemon)
        pokemonEntities[pokemon.id] = pokemon
    }
    
    override suspend fun insertPokemonList(pokemonList: List<PokemonEntity>) {
        insertPokemonListCalls.add(pokemonList)
        pokemonList.forEach { pokemonEntities[it.id] = it }
    }
    
    override suspend fun getPokemonById(id: Int): PokemonEntity? {
        getPokemonByIdCalls.add(id)
        return pokemonEntities[id]
    }
    
    override suspend fun deletePokemon(id: Int) {
        deletePokemonCalls.add(id)
        pokemonEntities.remove(id)
    }
    
    override suspend fun deleteAllPokemon() {
        pokemonEntities.clear()
    }
    
    override suspend fun searchPokemon(query: String): List<PokemonEntity> {
        searchPokemonCalls.add(query)
        return pokemonEntities.values.filter { 
            it.name.contains(query, ignoreCase = true) 
        }
    }
    
    fun reset() {
        pokemonEntities.clear()
        insertPokemonCalls.clear()
        insertPokemonListCalls.clear()
        getPokemonByIdCalls.clear()
        deletePokemonCalls.clear()
        searchPokemonCalls.clear()
    }
}



