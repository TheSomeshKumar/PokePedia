package com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_detail

/**
 * Created by SixFlags on 27 September, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
 */
sealed class PokemonDetailAction {
    data class LoadPokemon(val pokemonId: Int) : PokemonDetailAction()
    object Retry : PokemonDetailAction()
}
