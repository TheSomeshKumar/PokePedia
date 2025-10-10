package com.thesomeshkumar.pokepedia.app.navigation

import androidx.navigation.NavController

/**
 * Created by Somesh Kumar on 23 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */

/**
 * Type-safe navigation extensions to provide a cleaner API for navigation actions.
 * This demonstrates the power of type-safe navigation as recommended by JetBrains KMP.
 */

/**
 * Navigate to pokemon list screen.
 * Type-safe alternative to navController.navigate("pokemon_list")
 */
fun NavController.navigateToPokemonList() {
    navigate(PokemonListRoute)
}

/**
 * Navigate to pokemon detail screen with the specified pokemon ID.
 * Type-safe alternative to navController.navigate("pokemon_detail/$pokemonId")
 *
 * @param pokemonId The ID of the pokemon to display details for
 */
fun NavController.navigateToPokemonDetail(pokemonId: Int) {
    navigate(PokemonDetailRoute(pokemonId = pokemonId))
}

/**
 * Navigate to pokemon detail screen with pokemon UI object.
 * Convenient overload that extracts the ID from the pokemon object.
 *
 * @param pokemon The pokemon object containing the ID
 */
fun NavController.navigateToPokemonDetail(pokemon: com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonUI) {
    navigate(PokemonDetailRoute(pokemonId = pokemon.id))
}
