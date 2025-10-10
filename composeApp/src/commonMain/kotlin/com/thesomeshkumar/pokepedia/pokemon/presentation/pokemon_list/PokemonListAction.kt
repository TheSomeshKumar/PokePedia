package com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list

/**
 * Created by Somesh Kumar on 27 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */
sealed class PokemonListAction {
    object LoadPokemon : PokemonListAction()
    object LoadNextPage : PokemonListAction()
    object Retry : PokemonListAction()
    data class SearchPokemon(val query: String) : PokemonListAction()
    object ClearSearch : PokemonListAction()
}
