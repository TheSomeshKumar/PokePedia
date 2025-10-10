package com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesomeshkumar.pokepedia.core.domain.onError
import com.thesomeshkumar.pokepedia.core.domain.onSuccess
import com.thesomeshkumar.pokepedia.core.presentation.toUiText
import com.thesomeshkumar.pokepedia.pokemon.domain.PokemonRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Created by Somesh Kumar on 27 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */
class PokemonListViewModel(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PokemonListState())
    val state: StateFlow<PokemonListState> = _state.asStateFlow()

    private var searchJob: Job? = null
    private val limit = 20
    
    // Store the full loaded list for in-memory search
    private var fullPokemonList: List<PokemonUI> = emptyList()
    
    // Store pagination state before search to restore it later
    private var canLoadMoreBeforeSearch: Boolean = true

    init {
        handleAction(PokemonListAction.LoadPokemon)
    }

    fun handleAction(action: PokemonListAction) {
        when (action) {
            is PokemonListAction.LoadPokemon -> loadPokemon()
            is PokemonListAction.LoadNextPage -> loadNextPage()
            is PokemonListAction.Retry -> retry()
            is PokemonListAction.SearchPokemon -> searchPokemon(action.query)
            is PokemonListAction.ClearSearch -> clearSearch()
        }
    }

    private fun loadPokemon() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                errorMessage = null
            )

            pokemonRepository
                .getPokemon(
                    limit = limit,
                    offset = 0
                )
                .onSuccess { paginatedPokemon ->
                    val pokemonList = paginatedPokemon.pokemon.map { it.toUI() }
                    fullPokemonList = pokemonList // Store the full list for search
                    
                    _state.value = _state.value.copy(
                        isLoading = false,
                        pokemonList = pokemonList,
                        currentOffset = paginatedPokemon.offset + paginatedPokemon.pokemon.size,
                        canLoadMore = paginatedPokemon.hasNextPage
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

    private fun loadNextPage() {
        if (_state.value.isLoadingMore || !_state.value.canLoadMore) return

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoadingMore = true)

            pokemonRepository
                .getPokemon(
                    limit = limit,
                    offset = _state.value.currentOffset
                )
                .onSuccess { paginatedPokemon ->
                    val currentList = _state.value.pokemonList
                    val newList = paginatedPokemon.pokemon.map { it.toUI() }
                    val updatedList = currentList + newList
                    
                    fullPokemonList = updatedList // Update the full list for search

                    _state.value = _state.value.copy(
                        isLoadingMore = false,
                        pokemonList = updatedList,
                        currentOffset = paginatedPokemon.offset + paginatedPokemon.pokemon.size,
                        canLoadMore = paginatedPokemon.hasNextPage
                    )
                }
                .onError { error ->
                    _state.value = _state.value.copy(
                        isLoadingMore = false,
                        errorMessage = error.toUiText()
                    )
                }
        }
    }

    private fun retry() {
        if (_state.value.searchQuery.isNotEmpty()) {
            searchPokemon(_state.value.searchQuery)
        } else {
            loadPokemon()
        }
    }

    private fun searchPokemon(query: String) {
        searchJob?.cancel()
        
        // Save the current pagination state before searching
        if (_state.value.searchQuery.isEmpty() && query.isNotEmpty()) {
            canLoadMoreBeforeSearch = _state.value.canLoadMore
        }

        _state.value = _state.value.copy(
            searchQuery = query,
            isSearching = true
        )

        if (query.isEmpty()) {
            clearSearch()
            return
        }

        searchJob = viewModelScope.launch {
            delay(300) // Debounce search

            // Filter the in-memory list instead of querying the database
            val searchResults = fullPokemonList.filter { pokemon ->
                pokemon.name.contains(query, ignoreCase = true) ||
                pokemon.formattedName.contains(query, ignoreCase = true)
            }

            _state.value = _state.value.copy(
                isSearching = false,
                pokemonList = searchResults,
                canLoadMore = false, // Disable pagination during search
                errorMessage = null
            )
        }
    }

    private fun clearSearch() {
        searchJob?.cancel()
        _state.value = _state.value.copy(
            searchQuery = "",
            isSearching = false,
            pokemonList = fullPokemonList, // Restore the full list
            canLoadMore = canLoadMoreBeforeSearch // Restore the pagination state from before search
        )
    }
}

