package com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list

import androidx.compose.runtime.Immutable

/**
 * Created by Somesh Kumar on 27 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */
@Immutable
sealed class PokemonListAction {
    @Immutable
    data object LoadPokemon : PokemonListAction()
    
    @Immutable
    data object LoadNextPage : PokemonListAction()
    
    @Immutable
    data object Retry : PokemonListAction()
    
    @Immutable
    data class SearchPokemon(val query: String) : PokemonListAction()
    
    @Immutable
    data object ClearSearch : PokemonListAction()
}
