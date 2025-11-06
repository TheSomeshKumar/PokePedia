package com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.skydoves.compose.stability.runtime.TraceRecomposition
import com.thesomeshkumar.pokepedia.pokemon.presentation.components.AbilityChip
import com.thesomeshkumar.pokepedia.pokemon.presentation.components.ErrorContent
import com.thesomeshkumar.pokepedia.pokemon.presentation.components.InfoItem
import com.thesomeshkumar.pokepedia.pokemon.presentation.components.LoadingContent
import com.thesomeshkumar.pokepedia.pokemon.presentation.components.StatBar
import com.thesomeshkumar.pokepedia.pokemon.presentation.components.TypeChip
import com.thesomeshkumar.pokepedia.pokemon.presentation.components.parseColorHex
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonStatUI
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonTypeUI
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonUI
import com.thesomeshkumar.pokepedia.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import pokepedia.composeapp.generated.resources.Res
import pokepedia.composeapp.generated.resources.abilities_section
import pokepedia.composeapp.generated.resources.back_button
import pokepedia.composeapp.generated.resources.base_happiness_label
import pokepedia.composeapp.generated.resources.base_xp_label
import pokepedia.composeapp.generated.resources.basic_information
import pokepedia.composeapp.generated.resources.capture_rate_label
import pokepedia.composeapp.generated.resources.description_section
import pokepedia.composeapp.generated.resources.egg_groups_label
import pokepedia.composeapp.generated.resources.evolution_chain_section
import pokepedia.composeapp.generated.resources.gender_ratio_label
import pokepedia.composeapp.generated.resources.generation_label
import pokepedia.composeapp.generated.resources.growth_rate_label
import pokepedia.composeapp.generated.resources.habitat_label
import pokepedia.composeapp.generated.resources.height_label
import pokepedia.composeapp.generated.resources.legendary_badge
import pokepedia.composeapp.generated.resources.loading_pokemon_details
import pokepedia.composeapp.generated.resources.mythical_badge
import pokepedia.composeapp.generated.resources.species_information
import pokepedia.composeapp.generated.resources.stats_section
import pokepedia.composeapp.generated.resources.weight_label

/**
 * Created by Somesh Kumar on 27 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */

@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    onBackClick: () -> Unit,
    onNavigateToPokemon: (Int) -> Unit,
    viewModel: PokemonDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(pokemonId) {
        viewModel.initWithPokemonId(pokemonId)
    }
    
    // Remember the action handler to prevent creating new lambda on each recomposition
    val onAction = remember(viewModel) {
        { action: PokemonDetailAction -> viewModel.handleAction(action) }
    }

    PokemonDetailContent(
        state = state,
        onBackClick = onBackClick,
        onAction = onAction,
        onNavigateToPokemon = onNavigateToPokemon
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@TraceRecomposition
fun PokemonDetailContent(
    state: PokemonDetailState,
    onBackClick: () -> Unit,
    onAction: (PokemonDetailAction) -> Unit,
    onNavigateToPokemon: (Int) -> Unit,
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader = koinInject()
) {
    // Remember retry action to prevent creating new lambda on each recomposition
    val onRetry = remember(onAction) {
        { onAction(PokemonDetailAction.Retry) }
    }
    
    when {
        state.isLoading -> {
                Box(modifier = modifier.fillMaxSize()) {
                    LoadingContent(
                        message = stringResource(Res.string.loading_pokemon_details),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            state.errorMessage != null -> {
                Box(modifier = modifier.fillMaxSize()) {
                    ErrorContent(
                        message = state.errorMessage.asString(),
                        onRetryClick = onRetry,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

        state.pokemon != null -> {
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            
            Box(modifier = modifier.fillMaxSize()) {
                // Scrollable content
                PokemonContent(
                    pokemon = state.pokemon,
                    imageLoader = imageLoader,
                    scrollBehavior = scrollBehavior,
                    onNavigateToPokemon = onNavigateToPokemon,
                    modifier = Modifier.fillMaxSize()
                )
                
                // Floating Top Bar
                TopAppBar(
                    title = {
                        Text(
                            text = state.pokemon.formattedName,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(Res.string.back_button),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    actions = {},
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokemonContent(
    pokemon: PokemonUI,
    imageLoader: ImageLoader,
    scrollBehavior: androidx.compose.material3.TopAppBarScrollBehavior,
    onNavigateToPokemon: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimensions = AppTheme.dimensions
    var showContent by remember { mutableStateOf(false) }
    
    LaunchedEffect(pokemon.id) {
        showContent = true
    }
    
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(bottom = dimensions.spaceLarge)
    ) {
        item {
            // Hero Section
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(animationSpec = tween(300)) +
                        slideInVertically(
                            initialOffsetY = { -100 },
                            animationSpec = tween(300)
                        )
            ) {
                PokemonHeroSection(
                    pokemon = pokemon,
                    imageLoader = imageLoader
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(dimensions.spaceXXLarge))
        }

        // Basic Info
        item {
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(animationSpec = tween(300, delayMillis = 50)) +
                        slideInVertically(
                            initialOffsetY = { 50 },
                            animationSpec = tween(300, delayMillis = 50)
                        )
            ) {
                BasicInfoSection(pokemon = pokemon)
            }
        }

        item {
            Spacer(modifier = Modifier.height(dimensions.spaceXXLarge))
        }

        // Description
        if (pokemon.description.isNotEmpty()) {
            item {
                AnimatedVisibility(
                    visible = showContent,
                    enter = fadeIn(animationSpec = tween(300, delayMillis = 100)) +
                            slideInVertically(
                                initialOffsetY = { 50 },
                                animationSpec = tween(300, delayMillis = 100)
                            )
                ) {
                    DescriptionSection(description = pokemon.description)
                }
            }

            item {
                Spacer(modifier = Modifier.height(dimensions.spaceXXLarge))
            }
        }

        // Stats Section
        item {
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(animationSpec = tween(300, delayMillis = 150)) +
                        slideInVertically(
                            initialOffsetY = { 50 },
                            animationSpec = tween(300, delayMillis = 150)
                        )
            ) {
                StatsSection(stats = pokemon.stats)
            }
        }

        item {
            Spacer(modifier = Modifier.height(dimensions.spaceXXLarge))
        }

        // Abilities Section
        if (pokemon.abilities.isNotEmpty()) {
            item {
                AnimatedVisibility(
                    visible = showContent,
                    enter = fadeIn(animationSpec = tween(300, delayMillis = 200)) +
                            slideInVertically(
                                initialOffsetY = { 50 },
                                animationSpec = tween(300, delayMillis = 200)
                            )
                ) {
                    AbilitiesSection(abilities = pokemon.abilities)
                }
            }

            item {
                Spacer(modifier = Modifier.height(dimensions.spaceXXLarge))
            }
        }

        // Evolution Chain Section
        if (pokemon.evolutionChain.isNotEmpty()) {
            item {
                AnimatedVisibility(
                    visible = showContent,
                    enter = fadeIn(animationSpec = tween(300, delayMillis = 250)) +
                            slideInVertically(
                                initialOffsetY = { 50 },
                                animationSpec = tween(300, delayMillis = 250)
                            )
                ) {
                    EvolutionChainSection(
                        evolutionChain = pokemon.evolutionChain,
                        imageLoader = imageLoader,
                        onEvolutionClick = onNavigateToPokemon
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(dimensions.spaceXXLarge))
            }
        }

        // Species/Breeding Info Section
        pokemon.species?.let { species ->
            item {
                AnimatedVisibility(
                    visible = showContent,
                    enter = fadeIn(animationSpec = tween(300, delayMillis = 300)) +
                            slideInVertically(
                                initialOffsetY = { 50 },
                                animationSpec = tween(300, delayMillis = 300)
                            )
                ) {
                    SpeciesInfoSection(species = species)
                }
            }
        }
    }
}

@Composable
private fun PokemonHeroSection(
    pokemon: PokemonUI,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier
) {
    val primaryColor = pokemon.primaryType?.let {
        parseColorHex(it.colorHex)
    } ?: MaterialTheme.colorScheme.primary
    val dimensions = AppTheme.dimensions

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensions.heroSectionHeight)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = 0.3f),
                        Color.Transparent
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensions.heroTopPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Pokemon Number
            Text(
                text = pokemon.pokemonNumber,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(dimensions.spaceLarge))

            // Pokemon Image
            AsyncImage(
                model = pokemon.sprites.primaryImage,
                contentDescription = pokemon.formattedName,
                imageLoader = imageLoader,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(dimensions.imageSizeExtraLarge)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(dimensions.spaceLarge))

            // Types
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(dimensions.spaceSmall)
            ) {
                items(pokemon.types) { type ->
                    TypeChip(type = type)
                }
            }
        }
    }
}

@Composable
private fun BasicInfoSection(
    pokemon: PokemonUI,
    modifier: Modifier = Modifier
) {
    val primaryColor = pokemon.primaryType?.let {
        parseColorHex(it.colorHex)
    } ?: MaterialTheme.colorScheme.primary
    val dimensions = AppTheme.dimensions

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.spaceLarge),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensions.cardElevation),
        shape = RoundedCornerShape(dimensions.cardCornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        )
    ) {
        Box {
            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensions.gradientOverlayHeight)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                primaryColor.copy(alpha = 0.7f),
                                primaryColor.copy(alpha = 0.3f)
                            )
                        )
                    )
            )
            
            Column(
                modifier = Modifier.padding(dimensions.cardPadding)
            ) {
                Text(
                    text = stringResource(Res.string.basic_information),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )

                Spacer(modifier = Modifier.height(dimensions.spaceLarge))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    InfoItem(
                        label = stringResource(Res.string.height_label),
                        value = pokemon.heightInMeters,
                        color = primaryColor,
                        modifier = Modifier.weight(1f)
                    )
                    InfoItem(
                        label = stringResource(Res.string.weight_label),
                        value = pokemon.weightInKilograms,
                        color = primaryColor,
                        modifier = Modifier.weight(1f)
                    )
                    InfoItem(
                        label = stringResource(Res.string.base_xp_label),
                        value = pokemon.baseExperience.toString(),
                        color = primaryColor,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DescriptionSection(
    description: String,
    modifier: Modifier = Modifier
) {
    val dimensions = AppTheme.dimensions
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.spaceLarge),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensions.cardElevation),
        shape = RoundedCornerShape(dimensions.cardCornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(dimensions.cardPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(dimensions.badgeIndicatorWidth, dimensions.badgeIndicatorHeight)
                        .background(
                            MaterialTheme.colorScheme.secondary,
                            RoundedCornerShape(dimensions.badgeCornerRadius)
                        )
                )
                Spacer(modifier = Modifier.width(dimensions.spaceMedium))
                Text(
                    text = stringResource(Res.string.description_section),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(dimensions.spaceMedium))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Composable
private fun StatsSection(
    stats: List<PokemonStatUI>,
    modifier: Modifier = Modifier
) {
    val dimensions = AppTheme.dimensions
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.spaceLarge),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensions.cardElevation),
        shape = RoundedCornerShape(dimensions.cardCornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier.padding(dimensions.cardPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(dimensions.badgeIndicatorWidth, dimensions.badgeIndicatorHeight)
                        .background(
                            MaterialTheme.colorScheme.tertiary,
                            RoundedCornerShape(dimensions.badgeCornerRadius)
                        )
                )
                Spacer(modifier = Modifier.width(dimensions.spaceMedium))
                Text(
                    text = stringResource(Res.string.stats_section),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            Spacer(modifier = Modifier.height(dimensions.spaceLarge))

            stats.forEach { stat ->
                StatBar(stat = stat)
                Spacer(modifier = Modifier.height(dimensions.spaceMedium))
            }
        }
    }
}

@Composable
private fun AbilitiesSection(
    abilities: List<com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonAbilityUI>,
    modifier: Modifier = Modifier
) {
    val dimensions = AppTheme.dimensions
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.spaceLarge),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensions.cardElevation),
        shape = RoundedCornerShape(dimensions.cardCornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier.padding(dimensions.cardPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(dimensions.badgeIndicatorWidth, dimensions.badgeIndicatorHeight)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(dimensions.badgeCornerRadius)
                        )
                )
                Spacer(modifier = Modifier.width(dimensions.spaceMedium))
                Text(
                    text = stringResource(Res.string.abilities_section),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(dimensions.spaceLarge))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(dimensions.spaceMedium)
            ) {
                items(abilities) { ability ->
                    AbilityChip(ability = ability)
                }
            }
        }
    }
}

@Composable
private fun EvolutionChainSection(
    evolutionChain: List<com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.EvolutionStageUI>,
    imageLoader: ImageLoader,
    onEvolutionClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimensions = AppTheme.dimensions
    val pokemonColors = AppTheme.pokemonColors
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.spaceLarge),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensions.cardElevation),
        shape = RoundedCornerShape(dimensions.cardCornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = pokemonColors.evolutionPrimary.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(dimensions.cardPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(dimensions.badgeIndicatorWidth, dimensions.badgeIndicatorHeight)
                        .background(
                            pokemonColors.evolutionPrimary,
                            RoundedCornerShape(dimensions.badgeCornerRadius)
                        )
                )
                Spacer(modifier = Modifier.width(dimensions.spaceMedium))
                Text(
                    text = stringResource(Res.string.evolution_chain_section),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = pokemonColors.evolutionPrimary
                )
            }

            Spacer(modifier = Modifier.height(dimensions.spaceExtraLarge))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                evolutionChain.forEachIndexed { index, evolution ->
                    // Remember click handler for each evolution to prevent unnecessary recompositions
                    val evolutionClickHandler = remember(evolution.pokemonId) {
                        { onEvolutionClick(evolution.pokemonId) }
                    }
                    
                    // Evolution Stage Card
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clip(MaterialTheme.shapes.medium)
                            .clickable(onClick = evolutionClickHandler)
                            .padding(dimensions.spaceSmall)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(dimensions.imageSizeMedium)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            pokemonColors.evolutionPrimary.copy(alpha = 0.2f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        ) {
                            AsyncImage(
                                model = evolution.imageUrl,
                                contentDescription = evolution.formattedName,
                                imageLoader = imageLoader,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(dimensions.imageSizeSmall)
                                    .align(Alignment.Center)
                            )
                        }

                        Spacer(modifier = Modifier.height(dimensions.spaceSmall))

                        Text(
                            text = evolution.formattedName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        if (evolution.minLevel != null || evolution.item != null) {
                            Spacer(modifier = Modifier.height(dimensions.spaceExtraSmall))
                            Box(
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.small)
                                    .background(pokemonColors.evolutionPrimary.copy(alpha = 0.15f))
                                    .padding(
                                        horizontal = dimensions.spaceSmall,
                                        vertical = dimensions.spaceExtraSmall
                                    )
                            ) {
                                Text(
                                    text = evolution.evolutionMethod,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = pokemonColors.evolutionPrimary,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // Arrow between evolutions
                    if (index < evolutionChain.size - 1) {
                        Text(
                            text = "→",
                            style = MaterialTheme.typography.headlineMedium,
                            color = pokemonColors.evolutionPrimary.copy(alpha = 0.6f),
                            modifier = Modifier.padding(horizontal = dimensions.spaceExtraSmall)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SpeciesInfoSection(
    species: com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonSpeciesUI,
    modifier: Modifier = Modifier
) {
    val dimensions = AppTheme.dimensions
    val pokemonColors = AppTheme.pokemonColors
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.spaceLarge),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensions.cardElevation),
        shape = RoundedCornerShape(dimensions.cardCornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = pokemonColors.speciesPrimary.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(dimensions.cardPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(dimensions.badgeIndicatorWidth, dimensions.badgeIndicatorHeight)
                        .background(
                            pokemonColors.speciesPrimary,
                            RoundedCornerShape(dimensions.badgeCornerRadius)
                        )
                )
                Spacer(modifier = Modifier.width(dimensions.spaceMedium))
                Text(
                    text = stringResource(Res.string.species_information),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = pokemonColors.speciesPrimary
                )
            }

            Spacer(modifier = Modifier.height(dimensions.spaceLarge))

            // Classification badges
            if (species.isLegendary || species.isMythical) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimensions.spaceSmall),
                    modifier = Modifier.padding(bottom = dimensions.spaceLarge)
                ) {
                    if (species.isLegendary) {
                        ClassificationBadge(
                            text = stringResource(Res.string.legendary_badge), 
                            color = pokemonColors.legendaryBadge
                        )
                    }
                    if (species.isMythical) {
                        ClassificationBadge(
                            text = stringResource(Res.string.mythical_badge), 
                            color = pokemonColors.mythicalBadge
                        )
                    }
                }
            }

            // Generation
            SpeciesInfoRow(
                label = stringResource(Res.string.generation_label),
                value = species.generation
            )

            // Habitat
            if (species.habitat.isNotEmpty()) {
                SpeciesInfoRow(
                    label = stringResource(Res.string.habitat_label),
                    value = species.habitat
                )
            }

            // Capture Rate
            SpeciesInfoRow(
                label = stringResource(Res.string.capture_rate_label),
                value = "${species.captureRate}/255"
            )

            // Base Happiness
            SpeciesInfoRow(
                label = stringResource(Res.string.base_happiness_label),
                value = "${species.baseHappiness}/255"
            )

            // Growth Rate
            SpeciesInfoRow(
                label = stringResource(Res.string.growth_rate_label),
                value = species.growthRate
            )

            // Gender Ratio
            SpeciesInfoRow(
                label = stringResource(Res.string.gender_ratio_label),
                value = species.genderRatio
            )

            // Egg Groups
            if (species.eggGroups.isNotEmpty()) {
                Spacer(modifier = Modifier.height(dimensions.spaceMedium))
                Text(
                    text = stringResource(Res.string.egg_groups_label),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = pokemonColors.speciesPrimary
                )
                Spacer(modifier = Modifier.height(dimensions.spaceSmall))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(dimensions.spaceSmall)
                ) {
                    items(species.eggGroups) { eggGroup ->
                        EggGroupChip(eggGroup = eggGroup)
                    }
                }
            }
        }
    }
}

@Composable
private fun ClassificationBadge(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val dimensions = AppTheme.dimensions
    
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        color.copy(alpha = 0.3f),
                        color.copy(alpha = 0.15f)
                    )
                )
            )
            .padding(
                horizontal = dimensions.chipPaddingHorizontal,
                vertical = dimensions.spaceSmall
            )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SpeciesInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val dimensions = AppTheme.dimensions
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensions.spaceExtraSmall),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun EggGroupChip(
    eggGroup: String,
    modifier: Modifier = Modifier
) {
    val dimensions = AppTheme.dimensions
    val pokemonColors = AppTheme.pokemonColors
    
    val formattedEggGroup = eggGroup.replace("-", " ").split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        pokemonColors.speciesPrimary.copy(alpha = 0.2f),
                        pokemonColors.speciesPrimary.copy(alpha = 0.1f)
                    )
                )
            )
            .padding(
                horizontal = dimensions.chipPaddingHorizontal - 2.dp,
                vertical = dimensions.spaceSmall
            )
    ) {
        Text(
            text = formattedEggGroup,
            style = MaterialTheme.typography.labelMedium,
            color = pokemonColors.speciesPrimary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

