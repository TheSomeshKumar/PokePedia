package com.thesomeshkumar.pokepedia.pokemon.domain

/**
 * Created by SixFlags on 27 September, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
 */
data class Pokemon(
    val id: Int,
    val name: String,
    val height: Int, // in decimeters
    val weight: Int, // in hectograms
    val baseExperience: Int,
    val order: Int,
    val sprites: PokemonSprites,
    val stats: List<PokemonStat>,
    val types: List<PokemonType>,
    val abilities: List<PokemonAbility>,
    val species: PokemonSpecies? = null,
    val description: String = ""
) {
    val heightInMeters: String
        get() = "${(height / 10.0)} m"

    val weightInKilograms: String
        get() = "${(weight / 10.0)} kg"

    val formattedName: String
        get() = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

    val primaryType: PokemonType?
        get() = types.firstOrNull()

    val isLegendary: Boolean
        get() = species?.isLegendary ?: false

    val isMythical: Boolean
        get() = species?.isMythical ?: false
}

data class PokemonSprites(
    val frontDefault: String?,
    val frontShiny: String?,
    val backDefault: String?,
    val backShiny: String?,
    val officialArtwork: String? = null
) {
    val primaryImage: String?
        get() = officialArtwork ?: frontDefault
}

data class PokemonStat(
    val baseStat: Int,
    val effort: Int,
    val stat: Stat
) {
    data class Stat(
        val name: String,
        val url: String
    ) {
        val displayName: String
            get() = when (name) {
                "hp" -> "HP"
                "attack" -> "Attack"
                "defense" -> "Defense"
                "special-attack" -> "Sp. Attack"
                "special-defense" -> "Sp. Defense"
                "speed" -> "Speed"
                else -> name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            }
    }
}

data class PokemonType(
    val slot: Int,
    val type: Type
) {
    data class Type(
        val name: String,
        val url: String
    ) {
        val displayName: String
            get() = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

        val colorHex: String
            get() = when (name) {
                "normal" -> "#A8A878"
                "fire" -> "#F08030"
                "water" -> "#6890F0"
                "electric" -> "#F8D030"
                "grass" -> "#78C850"
                "ice" -> "#98D8D8"
                "fighting" -> "#C03028"
                "poison" -> "#A040A0"
                "ground" -> "#E0C068"
                "flying" -> "#A890F0"
                "psychic" -> "#F85888"
                "bug" -> "#A8B820"
                "rock" -> "#B8A038"
                "ghost" -> "#705898"
                "dragon" -> "#7038F8"
                "dark" -> "#705848"
                "steel" -> "#B8B8D0"
                "fairy" -> "#EE99AC"
                else -> "#68A090"
            }
    }
}

data class PokemonAbility(
    val isHidden: Boolean,
    val slot: Int,
    val ability: Ability
) {
    data class Ability(
        val name: String,
        val url: String
    ) {
        val displayName: String
            get() = name
                .replace(
                    "-",
                    " "
                )
                .split(" ")
                .joinToString(" ") { word ->
                    word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                }
    }
}

data class PokemonSpecies(
    val name: String,
    val url: String,
    val isLegendary: Boolean = false,
    val isMythical: Boolean = false,
    val captureRate: Int = 0,
    val baseHappiness: Int = 0,
    val growthRate: String = "",
    val habitat: String? = null
)
