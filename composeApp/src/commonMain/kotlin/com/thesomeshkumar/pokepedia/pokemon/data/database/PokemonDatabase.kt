package com.thesomeshkumar.pokepedia.pokemon.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Created by SixFlags on 27 September, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
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
