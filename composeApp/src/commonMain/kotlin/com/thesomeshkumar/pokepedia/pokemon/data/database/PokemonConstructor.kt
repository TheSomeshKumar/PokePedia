package com.thesomeshkumar.pokepedia.pokemon.data.database

import androidx.room.RoomDatabaseConstructor

/**
 * Created by Somesh Kumar on 28 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */

@Suppress("KotlinNoActualForExpect")
expect object PokemonConstructor : RoomDatabaseConstructor<PokemonDatabase> {
    override fun initialize(): PokemonDatabase
}