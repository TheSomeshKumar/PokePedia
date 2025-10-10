package com.thesomeshkumar.pokepedia.pokemon.data.mappers

import com.thesomeshkumar.pokepedia.pokemon.data.dto.AbilityInfoDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.AbilitySlotDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.ChainLinkDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.EvolutionChainDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.PokemonBasicDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.PokemonDetailDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.PokemonSpeciesDetailDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.SpritesDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.StatDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.StatInfoDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.TypeInfoDTO
import com.thesomeshkumar.pokepedia.pokemon.data.dto.TypeSlotDTO
import com.thesomeshkumar.pokepedia.pokemon.domain.EvolutionStage
import com.thesomeshkumar.pokepedia.pokemon.domain.Pokemon
import com.thesomeshkumar.pokepedia.pokemon.domain.PokemonAbility
import com.thesomeshkumar.pokepedia.pokemon.domain.PokemonSpecies
import com.thesomeshkumar.pokepedia.pokemon.domain.PokemonSprites
import com.thesomeshkumar.pokepedia.pokemon.domain.PokemonStat
import com.thesomeshkumar.pokepedia.pokemon.domain.PokemonType

/**
 * Created by Somesh Kumar on 27 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */

fun PokemonDetailDTO.toDomain(
    speciesDetail: PokemonSpeciesDetailDTO? = null,
    evolutionChain: EvolutionChainDTO? = null
): Pokemon {
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
        description = speciesDetail?.getEnglishDescription() ?: "",
        evolutionChain = evolutionChain?.toEvolutionStages() ?: emptyList()
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
        habitat = habitat?.name,
        eggGroups = eggGroups.map { it.name },
        genderRate = genderRate,
        generation = generation.name
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

fun EvolutionChainDTO.toEvolutionStages(): List<EvolutionStage> {
    val stages = mutableListOf<EvolutionStage>()
    
    fun processChainLink(chainLink: ChainLinkDTO, previousStages: List<EvolutionStage> = emptyList()) {
        val pokemonId = chainLink.species.url
            .trimEnd('/')
            .split('/')
            .last()
            .toInt()
        
        val evolutionDetail = chainLink.evolutionDetails.firstOrNull()
        
        val stage = EvolutionStage(
            pokemonId = pokemonId,
            pokemonName = chainLink.species.name,
            minLevel = evolutionDetail?.minLevel,
            trigger = evolutionDetail?.trigger?.name ?: "level-up",
            item = evolutionDetail?.item?.name
        )
        
        stages.add(stage)
        
        // Process all evolution branches
        chainLink.evolvesTo.forEach { nextLink ->
            processChainLink(nextLink, previousStages + stage)
        }
    }
    
    processChainLink(chain)
    return stages
}
