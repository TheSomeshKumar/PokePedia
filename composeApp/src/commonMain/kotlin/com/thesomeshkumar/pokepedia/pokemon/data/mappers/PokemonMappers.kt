package com.thesomeshkumar.pokepedia.pokemon.data.mappers

import com.thesomeshkumar.pokepedia.pokemon.data.dto.AbilityInfoDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.AbilitySlotDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.PokemonBasicDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.PokemonDetailDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.PokemonSpeciesDetailDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.SpritesDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.StatDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.StatInfoDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.TypeInfoDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.TypeSlotDTO
import com.thesomeshkumar.pokepedia.pokemon.domain.Pokemon
import com.thesomeshkumar.pokepedia.pokemon.domain.PokemonAbility
import com.thesomeshkumar.pokepedia.pokemon.domain.PokemonSpecies
import com.thesomeshkumar.pokepedia.pokemon.domain.PokemonSprites
import com.thesomeshkumar.pokepedia.pokemon.domain.PokemonStat
import com.thesomeshkumar.pokepedia.pokemon.domain.PokemonType

/**
 * Created by SixFlags on 27 September, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
 */

fun PokemonDetailDTO.toDomain(speciesDetail: PokemonSpeciesDetailDTO? = null): Pokemon {
    return Pokemon(
        id = id,
        name = name,
        height = height,
        weight = weight,
        baseExperience = baseExperience ?: 0,
        order = order,
        sprites = sprites.toDomain(),
        stats = stats.map { it.toDomain() },
        types = types.map { it.toDomain() },
        abilities = abilities.map { it.toDomain() },
        species = speciesDetail?.toDomain(),
        description = speciesDetail?.getEnglishDescription() ?: ""
    )
}

fun SpritesDTO.toDomain(): PokemonSprites {
    return PokemonSprites(
        frontDefault = frontDefault,
        frontShiny = frontShiny,
        backDefault = backDefault,
        backShiny = backShiny,
        officialArtwork = other?.officialArtwork?.frontDefault
    )
}

fun StatDTO.toDomain(): PokemonStat {
    return PokemonStat(
        baseStat = baseStat,
        effort = effort,
        stat = stat.toDomain()
    )
}

fun StatInfoDTO.toDomain(): PokemonStat.Stat {
    return PokemonStat.Stat(
        name = name,
        url = url
    )
}

fun TypeSlotDTO.toDomain(): PokemonType {
    return PokemonType(
        slot = slot,
        type = type.toDomain()
    )
}

fun TypeInfoDTO.toDomain(): PokemonType.Type {
    return PokemonType.Type(
        name = name,
        url = url
    )
}

fun AbilitySlotDTO.toDomain(): PokemonAbility {
    return PokemonAbility(
        isHidden = isHidden,
        slot = slot,
        ability = ability.toDomain()
    )
}

fun AbilityInfoDTO.toDomain(): PokemonAbility.Ability {
    return PokemonAbility.Ability(
        name = name,
        url = url
    )
}

fun PokemonSpeciesDetailDTO.toDomain(): PokemonSpecies {
    return PokemonSpecies(
        name = name,
        url = "",
        isLegendary = isLegendary,
        isMythical = isMythical,
        captureRate = captureRate,
        baseHappiness = baseHappiness,
        growthRate = growthRate.name,
        habitat = habitat?.name
    )
}

fun PokemonSpeciesDetailDTO.getEnglishDescription(): String {
    return flavorTextEntries
        .filter { it.language.name == "en" }
        .firstOrNull()?.flavorText
        ?.replace(
            "\n",
            " "
        )
        ?.replace(
            "\u000c",
            " "
        )
        ?.replace(
            "\\s+".toRegex(),
            " "
        )
        ?.trim() ?: ""
}

fun PokemonBasicDTO.toDomain(): Pokemon {
    return Pokemon(
        id = id,
        name = name,
        height = 0,
        weight = 0,
        baseExperience = 0,
        order = 0,
        sprites = PokemonSprites(
            frontDefault = null,
            frontShiny = null,
            backDefault = null,
            backShiny = null,
            officialArtwork = getImageUrl()
        ),
        stats = emptyList(),
        types = emptyList(),
        abilities = emptyList()
    )
}
