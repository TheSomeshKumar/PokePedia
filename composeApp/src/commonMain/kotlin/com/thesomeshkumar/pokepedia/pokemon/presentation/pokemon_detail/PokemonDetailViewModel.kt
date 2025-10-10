package com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesomeshkumar.pokepedia.core.domain.onError
import com.thesomeshkumar.pokepedia.core.domain.onSuccess
import com.thesomeshkumar.pokepedia.core.presentation.toUiText
import com.thesomeshkumar.pokepedia.pokemon.domain.PokemonRepository
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.toUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Created by Somesh Kumar on 27 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */
class PokemonDetailViewModel(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PokemonDetailState())
    val state: StateFlow<PokemonDetailState> = _state.asStateFlow()

    private var currentPokemonId: Int = -1

    fun handleAction(action: PokemonDetailAction) {
        when (action) {
            is PokemonDetailAction.LoadPokemon -> loadPokemon(action.pokemonId)
            is PokemonDetailAction.Retry -> retry()
        }
    }

    fun initWithPokemonId(pokemonId: Int) {
        if (currentPokemonId != pokemonId) {
            currentPokemonId = pokemonId
            handleAction(PokemonDetailAction.LoadPokemon(pokemonId))
        }
    }

    private fun loadPokemon(pokemonId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                errorMessage = null
            )

            pokemonRepository
                .getPokemonDetails(pokemonId)
                .onSuccess { pokemon ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        pokemon = pokemon.toUI()
                    )
                }
                .onError { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = error.toUiText()
                    )
                }
        }
    }


    private fun retry() {
        if (currentPokemonId != -1) {
            loadPokemon(currentPokemonId)
        }
    }
}
