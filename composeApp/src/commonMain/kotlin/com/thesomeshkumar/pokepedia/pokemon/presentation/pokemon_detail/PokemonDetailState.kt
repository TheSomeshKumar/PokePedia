package com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_detail

import androidx.compose.runtime.Immutable
import com.thesomeshkumar.pokepedia.core.presentation.UiText
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonUI

/**
 * Created by Somesh Kumar on 27 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */
@Immutable
data class PokemonDetailState(
    val isLoading: Boolean = false,
    val pokemon: PokemonUI? = null,
    val errorMessage: UiText? = null,
)
