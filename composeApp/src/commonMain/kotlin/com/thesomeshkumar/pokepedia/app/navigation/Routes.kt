package com.thesomeshkumar.pokepedia.app.navigation

import kotlinx.serialization.Serializable

/**
 * Created by Somesh Kumar on 23 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */

/**
 * Type-safe navigation routes using kotlinx.serialization as recommended by
 * JetBrains KMP Navigation documentation.
 */

@Serializable
object PokemonListRoute

@Serializable
data class PokemonDetailRoute(
    val pokemonId: Int
)
