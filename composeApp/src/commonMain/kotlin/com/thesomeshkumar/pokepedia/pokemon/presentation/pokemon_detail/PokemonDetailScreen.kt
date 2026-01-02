@file:Suppress(
    "MagicNumber",
    "PackageName",
    "TooManyFunctions",
    "FunctionNaming",
    "LongMethod",
    "LongParameterList"
)

package com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_detail

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import com.thesomeshkumar.pokepedia.pokemon.presentation.components.AnimatedBadgeIndicator
import com.thesomeshkumar.pokepedia.pokemon.presentation.components.StatBar
import com.thesomeshkumar.pokepedia.pokemon.presentation.components.TypeChip
import com.thesomeshkumar.pokepedia.pokemon.presentation.components.parseColorHex
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.EvolutionStageUI
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonAbilityUI
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonSpeciesUI
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonStatUI
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonUI
import com.thesomeshkumar.pokepedia.theme.AppTheme
import kotlinx.coroutines.delay
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
import kotlin.math.PI
import kotlin.math.sin

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
        {
                action: PokemonDetailAction ->
            viewModel.handleAction(action)
        }
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
        {
            onAction(PokemonDetailAction.Retry)
        }
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

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(bottom = dimensions.spaceLarge)
    ) {
        item(key = "hero_${pokemon.id}") {
            // Hero Section with enhanced entrance
            PokemonHeroSection(
                pokemon = pokemon,
                imageLoader = imageLoader,
            )
        }

        item {
            Spacer(modifier = Modifier.height(dimensions.spaceXXLarge))
        }

        // Basic Info with enhanced animation
        item(key = "basic_info_${pokemon.id}") {
            BasicInfoSection(
                pokemon = pokemon,
            )
        }

        item {
            Spacer(modifier = Modifier.height(dimensions.spaceXXLarge))
        }

        // Description
        if (pokemon.description.isNotEmpty()) {
            item(key = "description_${pokemon.id}") {
                DescriptionSection(
                    description = pokemon.description,
                )
            }

            item {
                Spacer(modifier = Modifier.height(dimensions.spaceXXLarge))
            }
        }

        // Stats Section
        item(key = "stats_${pokemon.id}") {
            StatsSection(
                stats = pokemon.stats,
            )
        }

        item {
            Spacer(modifier = Modifier.height(dimensions.spaceXXLarge))
        }

        // Abilities Section
        if (pokemon.abilities.isNotEmpty()) {
            item(key = "abilities_${pokemon.id}") {
                AbilitiesSection(
                    abilities = pokemon.abilities,
                )
            }

            item {
                Spacer(modifier = Modifier.height(dimensions.spaceXXLarge))
            }
        }

        // Evolution Chain Section
        if (pokemon.evolutionChain.isNotEmpty()) {
            item(key = "evolution_${pokemon.id}") {
                EvolutionChainSection(
                    evolutionChain = pokemon.evolutionChain,
                    imageLoader = imageLoader,
                    onEvolutionClick = onNavigateToPokemon,
                )
            }

            item {
                Spacer(modifier = Modifier.height(dimensions.spaceXXLarge))
            }
        }

        // Species/Breeding Info Section
        pokemon.species?.let { species ->
            item(key = "species_${pokemon.id}") {
                SpeciesInfoSection(
                    species = species,
                )
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
    // Remember parsed color to avoid parsing on every recomposition
    val primaryColor = remember(pokemon.primaryType?.colorHex) {
        pokemon.primaryType?.let { parseColorHex(it.colorHex) } ?: Color.Unspecified
    }
    val finalPrimaryColor = if (primaryColor == Color.Unspecified) {
        MaterialTheme.colorScheme.primary
    } else {
        primaryColor
    }
    val dimensions = AppTheme.dimensions

    // Animated gradient background
    val infiniteTransition = rememberInfiniteTransition(label = "hero_gradient")
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                4000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient_offset"
    )

    // Floating animation for Pokemon image
    val floatingOffset by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                2000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating_offset"
    )

    // Scale pulse animation
    val scalePulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                2000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale_pulse"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensions.heroSectionHeight)
    ) {
        // Animated gradient background with flowing colors
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            finalPrimaryColor.copy(alpha = 0.4f + gradientOffset * 0.2f),
                            finalPrimaryColor.copy(alpha = 0.2f),
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        // Animated particles/orbs in background
        AnimatedParticles(
            color = finalPrimaryColor,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensions.heroTopPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Pokemon Number with shimmer effect
            Text(
                text = pokemon.pokemonNumber,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.graphicsLayer {
                    alpha = 0.7f + gradientOffset * 0.3f
                }
            )

            Spacer(modifier = Modifier.height(dimensions.spaceLarge))

            // Pokemon Image with glow effect and floating animation
            Box(
                contentAlignment = Alignment.Center
            ) {
                // Glow effect behind image
                Box(
                    modifier = Modifier
                        .size(dimensions.imageSizeExtraLarge * 1.3f)
                        .scale(scalePulse)
                        .blur(30.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    finalPrimaryColor.copy(alpha = 0.6f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                )

                // Rotating ring effect
                Box(
                    modifier = Modifier
                        .size(dimensions.imageSizeExtraLarge * 1.2f)
                        .rotate(gradientOffset * 360f)
                        .alpha(0.3f)
                        .background(
                            brush = Brush.sweepGradient(
                                colors = listOf(
                                    finalPrimaryColor.copy(alpha = 0.8f),
                                    Color.Transparent,
                                    Color.Transparent,
                                    finalPrimaryColor.copy(alpha = 0.8f)
                                )
                            ),
                            shape = CircleShape
                        )
                )

                // Pokemon Image with floating animation
                AsyncImage(
                    model = pokemon.sprites.primaryImage,
                    contentDescription = pokemon.formattedName,
                    imageLoader = imageLoader,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(dimensions.imageSizeExtraLarge)
                        .offset(y = floatingOffset.dp)
                        .graphicsLayer {
                            scaleX = scalePulse
                            scaleY = scalePulse
                        }
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(dimensions.spaceLarge))

            // Types with staggered entrance animation
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(dimensions.spaceSmall)
            ) {
                items(pokemon.types) { type ->
                    val scale = remember { Animatable(0f) }
                    LaunchedEffect(type) {
                        delay(pokemon.types.indexOf(type) * 100L)
                        scale.animateTo(
                            targetValue = 1f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    }
                    Box(
                        modifier = Modifier.scale(scale.value)
                    ) {
                        TypeChip(type = type)
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedParticles(
    color: Color,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "particles")

    // Create multiple particles with different animations
    repeat(8) { index ->
        val offsetX by infiniteTransition.animateFloat(
            initialValue = (index * 50f) % 400f,
            targetValue = ((index * 50f) + 100f) % 400f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    (3000 + index * 500),
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "particle_x_$index"
        )

        val offsetY by infiniteTransition.animateFloat(
            initialValue = (index * 40f) % 300f,
            targetValue = ((index * 40f) + 80f) % 300f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    (2500 + index * 400),
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "particle_y_$index"
        )

        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.1f,
            targetValue = 0.8f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    (1500 + index * 200),
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "particle_alpha_$index"
        )

        Box(
            modifier = modifier.offset(
                x = offsetX.dp,
                y = offsetY.dp
            )
        ) {
            Box(
                modifier = Modifier
                    .size((15 + index * 3).dp)
                    .alpha(alpha)
                    .blur(8.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                color.copy(alpha = 0.6f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
private fun BasicInfoSection(
    pokemon: PokemonUI,
    modifier: Modifier = Modifier
) {
    // Remember parsed color to avoid parsing on every recomposition
    val primaryColor = remember(pokemon.primaryType?.colorHex) {
        pokemon.primaryType?.let { parseColorHex(it.colorHex) } ?: Color.Unspecified
    }
    val finalPrimaryColor = if (primaryColor == Color.Unspecified) {
        MaterialTheme.colorScheme.primary
    } else {
        primaryColor
    }
    val dimensions = AppTheme.dimensions

    // Shimmer effect animation
    val infiniteTransition = rememberInfiniteTransition(label = "basic_info_shimmer")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                2000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )

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
            // Animated gradient overlay with shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensions.gradientOverlayHeight)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                finalPrimaryColor.copy(alpha = 0.7f),
                                finalPrimaryColor.copy(alpha = 0.9f),
                                finalPrimaryColor.copy(alpha = 0.7f),
                                finalPrimaryColor.copy(alpha = 0.3f)
                            ),
                            startX = shimmerOffset * 1000f,
                            endX = (shimmerOffset + 1f) * 1000f
                        )
                    )
            )

            Column(
                modifier = Modifier.padding(dimensions.cardPadding)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedBadgeIndicator(color = finalPrimaryColor)
                    Spacer(modifier = Modifier.width(dimensions.spaceMedium))
                    Text(
                        text = stringResource(Res.string.basic_information),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = finalPrimaryColor
                    )
                }

                Spacer(modifier = Modifier.height(dimensions.spaceLarge))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val infoItems = listOf(
                        Triple(
                            stringResource(Res.string.height_label),
                            pokemon.heightInMeters,
                            0
                        ),
                        Triple(
                            stringResource(Res.string.weight_label),
                            pokemon.weightInKilograms,
                            1
                        ),
                        Triple(
                            stringResource(Res.string.base_xp_label),
                            pokemon.baseExperience.toString(),
                            2
                        )
                    )

                    infoItems.forEach { (label, value, index) ->
                        val scale = remember { Animatable(0f) }
                        LaunchedEffect(Unit) {
                            delay(index * 100L)
                            scale.animateTo(
                                targetValue = 1f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .scale(scale.value)
                        ) {
                            InfoItem(
                                label = label,
                                value = value,
                                color = finalPrimaryColor,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
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
                AnimatedBadgeIndicator(color = MaterialTheme.colorScheme.secondary)
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
                AnimatedBadgeIndicator(color = MaterialTheme.colorScheme.tertiary)
                Spacer(modifier = Modifier.width(dimensions.spaceMedium))
                Text(
                    text = stringResource(Res.string.stats_section),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            Spacer(modifier = Modifier.height(dimensions.spaceLarge))

            stats.forEachIndexed { index, stat ->
                AnimatedStatBar(
                    stat = stat,
                    index = index
                )
                if (index < stats.size - 1) {
                    Spacer(modifier = Modifier.height(dimensions.spaceMedium))
                }
            }
        }
    }
}

@Composable
private fun AnimatedStatBar(
    stat: PokemonStatUI,
    index: Int,
    modifier: Modifier = Modifier
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(stat) {
        delay(index * 80L)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    Box(
        modifier = modifier.graphicsLayer {
            scaleX = progress.value
            transformOrigin = androidx.compose.ui.graphics.TransformOrigin(
                0f,
                0.5f
            )
        }
    ) {
        StatBar(stat = stat)
    }
}

@Composable
private fun AbilitiesSection(
    abilities: List<PokemonAbilityUI>,
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
                AnimatedBadgeIndicator(color = MaterialTheme.colorScheme.primary)
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
                    val scale = remember { Animatable(0f) }
                    LaunchedEffect(ability) {
                        delay(abilities.indexOf(ability) * 100L)
                        scale.animateTo(
                            targetValue = 1f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMedium
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .scale(scale.value)
                            .graphicsLayer {
                                rotationY = (1f - scale.value) * 180f
                            }
                    ) {
                        AbilityChip(ability = ability)
                    }
                }
            }
        }
    }
}

@Composable
private fun EvolutionChainSection(
    evolutionChain: List<EvolutionStageUI>,
    imageLoader: ImageLoader,
    onEvolutionClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimensions = AppTheme.dimensions
    val pokemonColors = AppTheme.pokemonColors

    // Animated arrow
    val infiniteTransition = rememberInfiniteTransition(label = "evolution_arrow")
    val arrowScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                800,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "arrow_scale"
    )

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
                AnimatedBadgeIndicator(color = pokemonColors.evolutionPrimary)
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
                        {
                            onEvolutionClick(evolution.pokemonId)
                        }
                    }

                    // Evolution Stage Card with entrance animation
                    val scale = remember { Animatable(0f) }

                    LaunchedEffect(evolution) {
                        delay(index * 200L)
                        scale.animateTo(
                            targetValue = 1f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .scale(scale.value)
                            .clip(MaterialTheme.shapes.medium)
                            .clickable(onClick = evolutionClickHandler)
                            .padding(dimensions.spaceSmall)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(dimensions.imageSizeMedium)
                                .clip(CircleShape)
                        ) {
                            // Pulsing glow effect
                            val glowAlpha by infiniteTransition.animateFloat(
                                initialValue = 0.2f,
                                targetValue = 0.5f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(
                                        1500,
                                        easing = FastOutSlowInEasing
                                    ),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "glow_alpha_$index"
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .blur(15.dp)
                                    .background(
                                        Brush.radialGradient(
                                            colors = listOf(
                                                pokemonColors.evolutionPrimary.copy(alpha = glowAlpha),
                                                Color.Transparent
                                            )
                                        )
                                    )
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.radialGradient(
                                            colors = listOf(
                                                pokemonColors.evolutionPrimary.copy(alpha = 0.2f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                            )

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

                    // Animated arrow between evolutions
                    if (index < evolutionChain.size - 1) {
                        Text(
                            text = "→",
                            style = MaterialTheme.typography.headlineMedium,
                            color = pokemonColors.evolutionPrimary.copy(alpha = 0.6f),
                            modifier = Modifier
                                .padding(horizontal = dimensions.spaceExtraSmall)
                                .scale(arrowScale)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SpeciesInfoSection(
    species: PokemonSpeciesUI,
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
                AnimatedBadgeIndicator(color = pokemonColors.speciesPrimary)
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

    // Glowing animation for legendary/mythical badges
    val infiniteTransition = rememberInfiniteTransition(label = "badge_glow")
    val glowIntensity by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                1500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_intensity"
    )

    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                2500,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )

    Box(
        modifier = modifier.clip(MaterialTheme.shapes.medium)
    ) {
        // Glow layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(10.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            color.copy(alpha = glowIntensity),
                            color.copy(alpha = glowIntensity * 0.5f)
                        )
                    )
                )
        )

        // Main badge with shimmer
        Box(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            color.copy(alpha = 0.3f),
                            color.copy(alpha = 0.5f),
                            color.copy(alpha = 0.3f),
                            color.copy(alpha = 0.15f)
                        ),
                        startX = shimmerOffset * 200f,
                        endX = (shimmerOffset + 1f) * 200f
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
                fontWeight = FontWeight.Bold,
                modifier = Modifier.graphicsLayer {
                    shadowElevation = 4f
                }
            )
        }
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

    // Remember formatted string to avoid processing on every recomposition
    val formattedEggGroup = remember(eggGroup) {
        eggGroup
            .replace(
                "-",
                " "
            )
            .split(" ")
            .joinToString(" ") { word ->
                word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            }
    }

    // Shimmer animation
    val infiniteTransition = rememberInfiniteTransition(label = "egg_group_shimmer")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                2000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        pokemonColors.speciesPrimary.copy(alpha = 0.2f),
                        pokemonColors.speciesPrimary.copy(alpha = 0.35f),
                        pokemonColors.speciesPrimary.copy(alpha = 0.2f),
                        pokemonColors.speciesPrimary.copy(alpha = 0.1f)
                    ),
                    startX = shimmerOffset * 200f,
                    endX = (shimmerOffset + 1f) * 200f
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

