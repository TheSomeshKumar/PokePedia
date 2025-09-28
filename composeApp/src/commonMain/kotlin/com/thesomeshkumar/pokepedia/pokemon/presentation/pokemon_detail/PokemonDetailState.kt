package com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_detail

import com.thesomeshkumar.pokepedia.core.presentation.UiText
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonUI

/**
 * Created by SixFlags on 27 September, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
 */
data class PokemonDetailState(
    val isLoading: Boolean = false,
    val pokemon: PokemonUI? = null,
    val errorMessage: UiText? = null,
)
