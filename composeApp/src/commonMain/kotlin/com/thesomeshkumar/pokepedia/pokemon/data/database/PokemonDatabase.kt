package com.thesomeshkumar.pokepedia.pokemon.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Created by Somesh Kumar on 27 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */

@Database(
    entities = [PokemonEntity::class],
    version = 1
)
@ConstructedBy(PokemonConstructor::class)
abstract class PokemonDatabase : RoomDatabase() {
    abstract val pokemonDao: PokemonDao
}

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<PokemonDatabase>
}
