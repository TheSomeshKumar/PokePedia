package com.thesomeshkumar.pokepedia.pokemon.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by SixFlags on 27 September, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
 */

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: PokemonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(pokemonList: List<PokemonEntity>)

    @Query("SELECT * FROM pokemon WHERE id = :id")
    suspend fun getPokemonById(id: Int): PokemonEntity?

    @Query("DELETE FROM pokemon WHERE id = :id")
    suspend fun deletePokemon(id: Int)

    @Query("DELETE FROM pokemon")
    suspend fun deleteAllPokemon()

    @Query("SELECT * FROM pokemon WHERE name LIKE '%' || :query || '%'")
    suspend fun searchPokemon(query: String): List<PokemonEntity>
}
