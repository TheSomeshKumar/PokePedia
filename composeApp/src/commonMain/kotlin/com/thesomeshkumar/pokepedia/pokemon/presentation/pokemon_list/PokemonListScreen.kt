package com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.thesomeshkumar.pokepedia.pokemon.presentation.components.ErrorContent
import com.thesomeshkumar.pokepedia.pokemon.presentation.components.LoadingContent
import com.thesomeshkumar.pokepedia.pokemon.presentation.components.SearchBar
import com.thesomeshkumar.pokepedia.pokemon.presentation.components.parseColorHex
import com.thesomeshkumar.pokepedia.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import pokepedia.composeapp.generated.resources.Res
import pokepedia.composeapp.generated.resources.adjust_search_query
import pokepedia.composeapp.generated.resources.no_pokemon_found

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
    val dimensions = AppTheme.dimensions
    val gridState = rememberLazyGridState()

    // Pagination logic - Load more when reaching near the bottom
    LaunchedEffect(gridState, state.isLoadingMore, state.canLoadMore, state.searchQuery) {
        snapshotFlow {
            val layoutInfo = gridState.layoutInfo
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val totalItems = layoutInfo.totalItemsCount
            
            // Return true when we're within 4 items of the bottom (2 rows in grid)
            lastVisibleIndex >= totalItems - 5 && totalItems > 0
        }
            .distinctUntilChanged()
            .filter { shouldLoadMore -> shouldLoadMore }
            .collect {
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
                .padding(dimensions.spaceLarge)
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
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 160.dp),
                        state = gridState,
                        contentPadding = PaddingValues(dimensions.spaceLarge),
                        horizontalArrangement = Arrangement.spacedBy(dimensions.spaceMedium),
                        verticalArrangement = Arrangement.spacedBy(dimensions.spaceMedium)
                    ) {
                        itemsIndexed(
                            items = state.pokemonList,
                            key = { _, pokemon -> pokemon.id }
                        ) { index, pokemon ->
                            AnimatedPokemonCard(
                                pokemon = pokemon,
                                index = index,
                                onClick = { onPokemonClick(pokemon) },
                                imageLoader = imageLoader
                            )
                        }

                        if (state.isLoadingMore) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(dimensions.spaceLarge),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(dimensions.iconSizeLarge)
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
private fun AnimatedPokemonCard(
    pokemon: PokemonUI,
    index: Int,
    onClick: () -> Unit,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier
) {
    // Use remember with key to avoid recreating animatables on recomposition
    val animationState = remember(pokemon.id) {
        AnimationState(
            offsetY = Animatable(100f),
            alpha = Animatable(0f),
            scale = Animatable(0.8f),
            hasAnimated = false
        )
    }
    
    // Only animate once when the card first appears
    LaunchedEffect(pokemon.id) {
        if (!animationState.hasAnimated) {
            animationState.hasAnimated = true
            // Stagger the animation based on index (only for items within first 2 rows)
            val staggerDelay = if (index < 10) (index % 10) * 30L else 0L
            delay(staggerDelay)
            
            // Run all animations in parallel for snappier feel
            launch {
                animationState.offsetY.animateTo(
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            }
            launch {
                animationState.alpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 250,
                        easing = FastOutSlowInEasing
                    )
                )
            }
            launch {
                animationState.scale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
        }
    }
    
    PokemonGridCard(
        pokemon = pokemon,
        onClick = onClick,
        imageLoader = imageLoader,
        modifier = modifier
            .offset(y = animationState.offsetY.value.dp)
            .scale(animationState.scale.value)
            .alpha(animationState.alpha.value)
    )
}

// Data class to hold animation state
private data class AnimationState(
    val offsetY: Animatable<Float, AnimationVector1D>,
    val alpha: Animatable<Float, AnimationVector1D>,
    val scale: Animatable<Float, AnimationVector1D>,
    var hasAnimated: Boolean = false
)


@Composable
private fun PokemonGridCard(
    pokemon: PokemonUI,
    onClick: () -> Unit,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier
) {
    val dimensions = AppTheme.dimensions
    val primaryColor = pokemon.primaryType?.let {
        parseColorHex(it.colorHex)
    } ?: MaterialTheme.colorScheme.primary
    
    val secondaryColor = pokemon.types.getOrNull(1)?.let {
        parseColorHex(it.colorHex)
    } ?: primaryColor

    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 2.dp
        ),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Animated gradient background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                primaryColor.copy(alpha = 0.15f),
                                secondaryColor.copy(alpha = 0.08f),
                                Color.White
                            ),
                            startY = 0f,
                            endY = 800f
                        )
                    )
            )
            
            // Decorative circles for depth
            Box(
                modifier = Modifier
                    .size(dimensions.imageSizeLarge)
                    .offset(x = (-30).dp, y = (-30).dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .alpha(0.1f)
                    .background(primaryColor)
            )
            
            Box(
                modifier = Modifier
                    .size(dimensions.imageSizeSmall)
                    .offset(x = dimensions.spaceExtraLarge, y = dimensions.spaceExtraLarge)
                    .align(Alignment.BottomStart)
                    .clip(CircleShape)
                    .alpha(0.08f)
                    .background(secondaryColor)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensions.spaceLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Number badge
                Box(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .shadow(
                            elevation = 4.dp,
                            shape = MaterialTheme.shapes.medium
                        )
                        .clip(MaterialTheme.shapes.medium)
                        .background(Color.White)
                        .padding(
                            horizontal = dimensions.chipPaddingVertical,
                            vertical = dimensions.spaceSmall - 2.dp
                        )
                ) {
                    Text(
                        text = pokemon.pokemonNumber,
                        style = MaterialTheme.typography.labelMedium,
                        color = primaryColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(dimensions.spaceExtraSmall))

                // Pokemon Image with 3D effect
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    // Shadow/depth layer 1
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .offset(y = dimensions.spaceSmall)
                            .scale(0.9f)
                            .alpha(0.2f)
                            .clip(CircleShape)
                            .background(primaryColor)
                    )
                    
                    // Glow layer
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        primaryColor.copy(alpha = 0.4f),
                                        primaryColor.copy(alpha = 0.2f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                    
                    // Main image container
                    Surface(
                        modifier = Modifier
                            .size(dimensions.imageSizeLarge)
                            .shadow(
                                elevation = 12.dp,
                                shape = CircleShape,
                                spotColor = primaryColor.copy(alpha = 0.5f)
                            ),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.9f)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = pokemon.sprites.primaryImage,
                                contentDescription = pokemon.formattedName,
                                imageLoader = imageLoader,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(dimensions.chipPaddingHorizontal - 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(dimensions.spaceMedium))

                // Pokemon name
                Text(
                    text = pokemon.formattedName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = primaryColor,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(dimensions.spaceSmall))

                // Types row
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    pokemon.types.take(2).forEach { type ->
                        Box(
                            modifier = Modifier
                                .shadow(
                                    elevation = 4.dp,
                                    shape = MaterialTheme.shapes.large,
                                    spotColor = parseColorHex(type.colorHex).copy(alpha = 0.5f)
                                )
                                .clip(MaterialTheme.shapes.large)
                                .background(parseColorHex(type.colorHex))
                                .padding(
                                    horizontal = dimensions.spaceMedium,
                                    vertical = dimensions.spaceSmall - 2.dp
                                )
                        ) {
                            Text(
                                text = type.displayName,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                        
                        if (pokemon.types.size > 1 && type != pokemon.types.last()) {
                            Spacer(modifier = Modifier.width(dimensions.spaceSmall - 2.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyContent(
    modifier: Modifier = Modifier
) {
    val dimensions = AppTheme.dimensions
    
    Column(
        modifier = modifier.padding(dimensions.spaceLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.no_pokemon_found),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(dimensions.spaceSmall))
        Text(
            text = stringResource(Res.string.adjust_search_query),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
