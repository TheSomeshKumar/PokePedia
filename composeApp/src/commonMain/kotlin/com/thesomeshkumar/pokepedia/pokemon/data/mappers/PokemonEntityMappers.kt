package com.thesomeshkumar.pokepedia.pokemon.data.mappers

import com.thesomeshkumar.pokepedia.pokemon.data.database.PokemonEntity
import com.thesomeshkumar.pokepedia.pokemon.domain.Pokemon
import com.thesomeshkumar.pokepedia.pokemon.domain.PokemonAbility
import com.thesomeshkumar.pokepedia.pokemon.domain.PokemonSpecies
import com.thesomeshkumar.pokepedia.pokemon.domain.PokemonSprites
import com.thesomeshkumar.pokepedia.pokemon.domain.PokemonStat
import com.thesomeshkumar.pokepedia.pokemon.domain.PokemonType
import kotlinx.serialization.json.Json

/**
 * Created by Somesh Kumar on 27 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */

private val json = Json { ignoreUnknownKeys = true }

fun Pokemon.toEntity(): PokemonEntity {
    return PokemonEntity(
        id = id,
        name = name,
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        order = order,
        frontSprite = sprites.frontDefault,
        frontShinySprite = sprites.frontShiny,
        backSprite = sprites.backDefault,
        backShinySprite = sprites.backShiny,
        officialArtwork = sprites.officialArtwork,
        stats = json.encodeToString(stats.map { it.toSerializable() }),
        types = json.encodeToString(types.map { it.toSerializable() }),
        abilities = json.encodeToString(abilities.map { it.toSerializable() }),
        isLegendary = species?.isLegendary ?: false,
        isMythical = species?.isMythical ?: false,
        description = description,
        captureRate = species?.captureRate ?: 0,
        baseHappiness = species?.baseHappiness ?: 0,
        growthRate = species?.growthRate ?: "",
        habitat = species?.habitat
    )
}

fun PokemonEntity.toDomain(): Pokemon {
    return Pokemon(
        id = id,
        name = name,
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        order = order,
        sprites = PokemonSprites(
            frontDefault = frontSprite,
            frontShiny = frontShinySprite,
            backDefault = backSprite,
            backShiny = backShinySprite,
            officialArtwork = officialArtwork
        ),
        stats = json
            .decodeFromString<List<SerializablePokemonStat>>(stats)
            .map { it.toDomain() },
        types = json
            .decodeFromString<List<SerializablePokemonType>>(types)
            .map { it.toDomain() },
        abilities = json
            .decodeFromString<List<SerializablePokemonAbility>>(abilities)
            .map { it.toDomain() },
        species = PokemonSpecies(
            name = name,
            url = "",
            isLegendary = isLegendary,
            isMythical = isMythical,
            captureRate = captureRate,
            baseHappiness = baseHappiness,
            growthRate = growthRate,
            habitat = habitat
        ),
        description = description
    )
}

// Serializable data classes for JSON conversion
@kotlinx.serialization.Serializable
data class SerializablePokemonStat(
    val baseStat: Int,
    val effort: Int,
    val statName: String,
    val statUrl: String
)

@kotlinx.serialization.Serializable
data class SerializablePokemonType(
    val slot: Int,
    val typeName: String,
    val typeUrl: String
)

@kotlinx.serialization.Serializable
data class SerializablePokemonAbility(
    val isHidden: Boolean,
    val slot: Int,
    val abilityName: String,
    val abilityUrl: String
)

// Extension functions for conversion
fun PokemonStat.toSerializable(): SerializablePokemonStat {
    return SerializablePokemonStat(
        baseStat = baseStat,
        effort = effort,
        statName = stat.name,
        statUrl = stat.url
    )
}

fun SerializablePokemonStat.toDomain(): PokemonStat {
    return PokemonStat(
        baseStat = baseStat,
        effort = effort,
        stat = PokemonStat.Stat(
            name = statName,
            url = statUrl
        )
    )
}

fun PokemonType.toSerializable(): SerializablePokemonType {
    return SerializablePokemonType(
        slot = slot,
        typeName = type.name,
        typeUrl = type.url
    )
}

fun SerializablePokemonType.toDomain(): PokemonType {
    return PokemonType(
        slot = slot,
        type = PokemonType.Type(
            name = typeName,
            url = typeUrl
        )
    )
}

fun PokemonAbility.toSerializable(): SerializablePokemonAbility {
    return SerializablePokemonAbility(
        isHidden = isHidden,
        slot = slot,
        abilityName = ability.name,
        abilityUrl = ability.url
    )
}

fun SerializablePokemonAbility.toDomain(): PokemonAbility {
    return PokemonAbility(
        isHidden = isHidden,
        slot = slot,
        ability = PokemonAbility.Ability(
            name = abilityName,
            url = abilityUrl
        )
    )
}
