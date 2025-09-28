package com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list

import com.thesomeshkumar.pokepedia.core.presentation.UiText
import com.thesomeshkumar.pokepedia.pokemon.domain.Pokemon

/**
 * Created by SixFlags on 27 September, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
 */
data class PokemonListState(
    val isLoading: Boolean = false,
    val pokemonList: List<PokemonUI> = emptyList(),
    val errorMessage: UiText? = null,
    val isLoadingMore: Boolean = false,
    val currentOffset: Int = 0,
    val canLoadMore: Boolean = true,
    val searchQuery: String = "",
    val isSearching: Boolean = false
)

data class PokemonUI(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val baseExperience: Int,
    val sprites: PokemonSpritesUI,
    val stats: List<PokemonStatUI>,
    val types: List<PokemonTypeUI>,
    val abilities: List<PokemonAbilityUI>,
    val isLegendary: Boolean,
    val isMythical: Boolean,
    val description: String
) {
    val formattedName: String
        get() = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

    val heightInMeters: String
        get() = "${(height / 10.0)} m"

    val weightInKilograms: String
        get() = "${(weight / 10.0)} kg"

    val primaryType: PokemonTypeUI?
        get() = types.firstOrNull()

    val pokemonNumber: String
        get() = "#${
            id
                .toString()
                .padStart(
                    3,
                    '0'
                )
        }"
}

data class PokemonSpritesUI(
    val frontDefault: String?,
    val frontShiny: String?,
    val backDefault: String?,
    val backShiny: String?,
    val officialArtwork: String?
) {
    val primaryImage: String?
        get() = officialArtwork ?: frontDefault
}

data class PokemonStatUI(
    val baseStat: Int,
    val effort: Int,
    val name: String,
    val displayName: String
) {
    val progressValue: Float
        get() = (baseStat / 255f).coerceIn(
            0f,
            1f
        )
}

data class PokemonTypeUI(
    val slot: Int,
    val name: String,
    val displayName: String,
    val colorHex: String
)

data class PokemonAbilityUI(
    val isHidden: Boolean,
    val slot: Int,
    val name: String,
    val displayName: String
)

// Mapper functions from Domain to UI
fun Pokemon.toUI(): PokemonUI {
    return PokemonUI(
        id = id,
        name = name,
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        sprites = sprites.toUI(),
        stats = stats.map { it.toUI() },
        types = types.map { it.toUI() },
        abilities = abilities.map { it.toUI() },
        isLegendary = isLegendary,
        isMythical = isMythical,
        description = description
    )
}

fun com.thesomeshkumar.pokepedia.pokemon.domain.PokemonSprites.toUI(): PokemonSpritesUI {
    return PokemonSpritesUI(
        frontDefault = frontDefault,
        frontShiny = frontShiny,
        backDefault = backDefault,
        backShiny = backShiny,
        officialArtwork = officialArtwork
    )
}

fun com.thesomeshkumar.pokepedia.pokemon.domain.PokemonStat.toUI(): PokemonStatUI {
    return PokemonStatUI(
        baseStat = baseStat,
        effort = effort,
        name = stat.name,
        displayName = stat.displayName
    )
}

fun com.thesomeshkumar.pokepedia.pokemon.domain.PokemonType.toUI(): PokemonTypeUI {
    return PokemonTypeUI(
        slot = slot,
        name = type.name,
        displayName = type.displayName,
        colorHex = type.colorHex
    )
}

fun com.thesomeshkumar.pokepedia.pokemon.domain.PokemonAbility.toUI(): PokemonAbilityUI {
    return PokemonAbilityUI(
        isHidden = isHidden,
        slot = slot,
        name = ability.name,
        displayName = ability.displayName
    )
}
