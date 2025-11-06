package com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_detail

import androidx.compose.runtime.Immutable

/**
 * Created by Somesh Kumar on 27 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */
@Immutable
sealed class PokemonDetailAction {
    @Immutable
    data class LoadPokemon(val pokemonId: Int) : PokemonDetailAction()
    
    @Immutable
    data object Retry : PokemonDetailAction()
}
