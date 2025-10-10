package com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ImageLoader
import coil3.compose.AsyncImage
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

// Helper function to parse hex color strings
private fun parseColorHex(colorHex: String): Color {
    return try {
        val hex = if (colorHex.startsWith("#")) colorHex else "#$colorHex"
        Color(
            hex
                .removePrefix("#")
                .toLong(16) or 0xFF000000
        )
    } catch (e: Exception) {
        Color.Gray
    }
}

/**
 * Created by Somesh Kumar on 27 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */

@Composable
fun PokemonListScreen(
    onPokemonClick: (PokemonUI) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PokemonListContent(
        state = state,
        onPokemonClick = onPokemonClick,
        onAction = viewModel::handleAction,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListContent(
    state: PokemonListState,
    onPokemonClick: (PokemonUI) -> Unit,
    onAction: (PokemonListAction) -> Unit,
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader = koinInject()
) {
    val listState = rememberLazyListState()

    // Pagination logic - Load more when reaching near the bottom
    LaunchedEffect(listState, state.isLoadingMore, state.canLoadMore, state.searchQuery) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val totalItems = layoutInfo.totalItemsCount
            
            // Return true when we're within 1 item of the bottom
            lastVisibleIndex >= totalItems - 2 && totalItems > 0
        }
            .distinctUntilChanged() // Only trigger when the condition actually changes
            .filter { shouldLoadMore -> shouldLoadMore } // Only proceed when true
            .collect {
                // Additional checks before loading
                if (state.canLoadMore && !state.isLoadingMore && state.searchQuery.isEmpty()) {
                    onAction(PokemonListAction.LoadNextPage)
                }
            }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Search Bar
        SearchBar(
            query = state.searchQuery,
            onQueryChange = { query ->
                onAction(PokemonListAction.SearchPokemon(query))
            },
            onClearClick = {
                onAction(PokemonListAction.ClearSearch)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Content
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.isLoading -> {
                    LoadingContent(modifier = Modifier.align(Alignment.Center))
                }

                state.errorMessage != null -> {
                    ErrorContent(
                        message = state.errorMessage.asString(),
                        onRetryClick = {
                            onAction(PokemonListAction.Retry)
                        },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.pokemonList.isEmpty() -> {
                    EmptyContent(modifier = Modifier.align(Alignment.Center))
                }

                else -> {
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        itemsIndexed(state.pokemonList) { index, pokemon ->
                            PokemonCard(
                                pokemon = pokemon,
                                onClick = { onPokemonClick(pokemon) },
                                imageLoader = imageLoader,
                            )
                        }

                        if (state.isLoadingMore) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (state.isSearching) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search Pokemon...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        },
        trailingIcon = if (query.isNotEmpty()) {
            {
                IconButton(onClick = onClearClick) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear"
                    )
                }
            }
        } else null,
        singleLine = true,
        modifier = modifier)
}

@Composable
private fun PokemonCard(
    pokemon: PokemonUI,
    onClick: () -> Unit,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier
) {
    val primaryColor = pokemon.primaryType?.let {
        parseColorHex(it.colorHex)
    } ?: MaterialTheme.colorScheme.primary

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Box {
            // Gradient background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                primaryColor.copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        )
                    )
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pokemon Image with gradient glow
                Box(
                    modifier = Modifier
                        .size(90.dp)
                ) {
                    // Gradient glow behind image
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        primaryColor.copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                    // Image
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.Center)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.5f))
                    ) {
                        AsyncImage(
                            model = pokemon.sprites.primaryImage,
                            contentDescription = pokemon.formattedName,
                            imageLoader = imageLoader,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Pokemon Info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Number badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(primaryColor.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = pokemon.pokemonNumber,
                            style = MaterialTheme.typography.labelSmall,
                            color = primaryColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = pokemon.formattedName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = primaryColor
                    )
                    
                    // Types
                    if (pokemon.types.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            pokemon.types.take(2).forEach { type ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(parseColorHex(type.colorHex).copy(alpha = 0.8f))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = type.displayName,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TypeChip(
    type: PokemonTypeUI,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(parseColorHex(type.colorHex))
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            )
    ) {
        Text(
            text = type.displayName,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Loading Pokemon...",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Oops! Something went wrong",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetryClick) {
            Text("Retry")
        }
    }
}

@Composable
private fun EmptyContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No Pokemon found",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Try adjusting your search query",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
