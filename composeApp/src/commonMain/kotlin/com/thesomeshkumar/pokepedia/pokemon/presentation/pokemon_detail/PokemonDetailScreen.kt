package com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonStatUI
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonTypeUI
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonUI
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
 * Created by SixFlags on 27 September, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
 */

@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    onBackClick: () -> Unit,
    viewModel: PokemonDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(pokemonId) {
        viewModel.initWithPokemonId(pokemonId)
    }

    PokemonDetailContent(
        state = state,
        onBackClick = onBackClick,
        onAction = viewModel::handleAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailContent(
    state: PokemonDetailState,
    onBackClick: () -> Unit,
    onAction: (PokemonDetailAction) -> Unit,
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader = koinInject()
) {
    when {
        state.isLoading -> {
            Box(modifier = modifier.fillMaxSize()) {
                LoadingContent(modifier = Modifier.align(Alignment.Center))
            }
        }

        state.errorMessage != null -> {
            Box(modifier = modifier.fillMaxSize()) {
                ErrorContent(
                    message = state.errorMessage.asString(),
                    onRetryClick = {
                        onAction(PokemonDetailAction.Retry)
                    },
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
                                contentDescription = "Back",
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
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            // Hero Section
            PokemonHeroSection(
                pokemon = pokemon,
                imageLoader = imageLoader
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Basic Info
        item {
            BasicInfoSection(pokemon = pokemon)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Description
        if (pokemon.description.isNotEmpty()) {
            item {
                DescriptionSection(description = pokemon.description)
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Stats Section
        item {
            StatsSection(stats = pokemon.stats)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Abilities Section
        if (pokemon.abilities.isNotEmpty()) {
            item {
                AbilitiesSection(abilities = pokemon.abilities)
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Evolution Chain Section
        if (pokemon.evolutionChain.isNotEmpty()) {
            item {
                EvolutionChainSection(
                    evolutionChain = pokemon.evolutionChain,
                    imageLoader = imageLoader
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Species/Breeding Info Section
        pokemon.species?.let { species ->
            item {
                SpeciesInfoSection(species = species)
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

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
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
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Pokemon Number
            Text(
                text = pokemon.pokemonNumber,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Pokemon Image
            AsyncImage(
                model = pokemon.sprites.primaryImage,
                contentDescription = pokemon.formattedName,
                imageLoader = imageLoader,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Types
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
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

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        )
    ) {
        Box {
            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
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
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Basic Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    InfoItem(
                        label = "Height",
                        value = pokemon.heightInMeters,
                        color = primaryColor,
                        modifier = Modifier.weight(1f)
                    )
                    InfoItem(
                        label = "Weight",
                        value = pokemon.weightInKilograms,
                        color = primaryColor,
                        modifier = Modifier.weight(1f)
                    )
                    InfoItem(
                        label = "Base XP",
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
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(4.dp, 24.dp)
                        .background(
                            MaterialTheme.colorScheme.secondary,
                            RoundedCornerShape(2.dp)
                        )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

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
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(4.dp, 24.dp)
                        .background(
                            MaterialTheme.colorScheme.tertiary,
                            RoundedCornerShape(2.dp)
                        )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Stats",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            stats.forEach { stat ->
                StatBar(stat = stat)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun AbilitiesSection(
    abilities: List<com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonAbilityUI>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(4.dp, 24.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(2.dp)
                        )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Abilities",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(abilities) { ability ->
                    AbilityChip(ability = ability)
                }
            }
        }
    }
}

@Composable
private fun InfoItem(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StatBar(
    stat: PokemonStatUI,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = stat.progressValue,
        animationSpec = tween(durationMillis = 1000),
        label = "stat_progress"
    )

    val statColor = when {
        stat.baseStat >= 100 -> Color(0xFF4CAF50)
        stat.baseStat >= 70 -> Color(0xFF2196F3)
        stat.baseStat >= 50 -> Color(0xFFFFC107)
        else -> Color(0xFFFF5722)
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stat.displayName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(statColor.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = stat.baseStat.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = statColor
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .clip(RoundedCornerShape(5.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                statColor,
                                statColor.copy(alpha = 0.7f)
                            )
                        )
                    )
            )
        }
    }
}

@Composable
private fun TypeChip(
    type: PokemonTypeUI,
    modifier: Modifier = Modifier
) {
    val typeColor = parseColorHex(type.colorHex)
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        typeColor,
                        typeColor.copy(alpha = 0.85f)
                    )
                )
            )
            .padding(
                horizontal = 20.dp,
                vertical = 10.dp
            )
    ) {
        Text(
            text = type.displayName,
            style = MaterialTheme.typography.labelLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun AbilityChip(
    ability: com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonAbilityUI,
    modifier: Modifier = Modifier
) {
    val chipColor = if (ability.isHidden) {
        MaterialTheme.colorScheme.secondary
    } else {
        MaterialTheme.colorScheme.primary
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        chipColor,
                        chipColor.copy(alpha = 0.8f)
                    )
                )
            )
            .padding(
                horizontal = 16.dp,
                vertical = 10.dp
            )
    ) {
        Text(
            text = if (ability.isHidden) "${ability.displayName} ✨" else ability.displayName,
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
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
            text = "Loading Pokemon details...",
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
private fun EvolutionChainSection(
    evolutionChain: List<com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.EvolutionStageUI>,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF6C63FF).copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(4.dp, 24.dp)
                        .background(
                            Color(0xFF6C63FF),
                            RoundedCornerShape(2.dp)
                        )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Evolution Chain",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6C63FF)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                evolutionChain.forEachIndexed { index, evolution ->
                    // Evolution Stage Card
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFF6C63FF).copy(alpha = 0.2f),
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
                                    .size(80.dp)
                                    .align(Alignment.Center)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = evolution.formattedName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        if (evolution.minLevel != null || evolution.item != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF6C63FF).copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = evolution.evolutionMethod,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF6C63FF),
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
                            color = Color(0xFF6C63FF).copy(alpha = 0.6f),
                            modifier = Modifier.padding(horizontal = 4.dp)
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
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFF6B9D).copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(4.dp, 24.dp)
                        .background(
                            Color(0xFFFF6B9D),
                            RoundedCornerShape(2.dp)
                        )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Species Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF6B9D)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Classification badges
            if (species.isLegendary || species.isMythical) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    if (species.isLegendary) {
                        ClassificationBadge(text = "✨ Legendary", color = Color(0xFFFFD700))
                    }
                    if (species.isMythical) {
                        ClassificationBadge(text = "🌟 Mythical", color = Color(0xFFFF69B4))
                    }
                }
            }

            // Generation
            SpeciesInfoRow(
                label = "Generation",
                value = species.generation
            )

            // Habitat
            if (species.habitat.isNotEmpty()) {
                SpeciesInfoRow(
                    label = "Habitat",
                    value = species.habitat
                )
            }

            // Capture Rate
            SpeciesInfoRow(
                label = "Capture Rate",
                value = "${species.captureRate}/255"
            )

            // Base Happiness
            SpeciesInfoRow(
                label = "Base Happiness",
                value = "${species.baseHappiness}/255"
            )

            // Growth Rate
            SpeciesInfoRow(
                label = "Growth Rate",
                value = species.growthRate
            )

            // Gender Ratio
            SpeciesInfoRow(
                label = "Gender Ratio",
                value = species.genderRatio
            )

            // Egg Groups
            if (species.eggGroups.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Egg Groups",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF6B9D)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
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
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        color.copy(alpha = 0.3f),
                        color.copy(alpha = 0.15f)
                    )
                )
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
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
    val formattedEggGroup = eggGroup.replace("-", " ").split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFF6B9D).copy(alpha = 0.2f),
                        Color(0xFFFF6B9D).copy(alpha = 0.1f)
                    )
                )
            )
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = formattedEggGroup,
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFFFF6B9D),
            fontWeight = FontWeight.SemiBold
        )
    }
}
