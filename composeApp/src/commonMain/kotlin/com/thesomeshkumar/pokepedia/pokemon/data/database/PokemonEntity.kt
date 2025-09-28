package com.thesomeshkumar.pokepedia.pokemon.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by SixFlags on 27 September, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
 */

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val baseExperience: Int,
    val order: Int,
    val frontSprite: String?,
    val frontShinySprite: String?,
    val backSprite: String?,
    val backShinySprite: String?,
    val officialArtwork: String?,
    val stats: String, // JSON string of stats
    val types: String, // JSON string of types
    val abilities: String, // JSON string of abilities
    val isLegendary: Boolean,
    val isMythical: Boolean,
    val description: String,
    val captureRate: Int,
    val baseHappiness: Int,
    val growthRate: String,
    val habitat: String?
)
