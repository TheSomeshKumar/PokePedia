package com.thesomeshkumar.pokepedia.pokemon.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by Somesh Kumar on 27 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */

@Serializable
data class PokemonListResponse(
    @SerialName("count") val count: Int,
    @SerialName("next") val next: String?,
    @SerialName("previous") val previous: String?,
    @SerialName("results") val results: List<PokemonBasicDTO>
)

@Serializable
data class PokemonBasicDTO(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
) {
    val id: Int
        get() = url
            .trimEnd('/')
            .split('/')
            .last()
            .toInt()

    fun getImageUrl(): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
    }
}

@Serializable
data class PokemonDetailDTO(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("height") val height: Int,
    @SerialName("weight") val weight: Int,
    @SerialName("base_experience") val baseExperience: Int?,
    @SerialName("order") val order: Int,
    @SerialName("sprites") val sprites: SpritesDTO,
    @SerialName("stats") val stats: List<StatDTO>,
    @SerialName("types") val types: List<TypeSlotDTO>,
    @SerialName("abilities") val abilities: List<AbilitySlotDTO>,
    @SerialName("species") val species: SpeciesDTO
)

@Serializable
data class SpritesDTO(
    @SerialName("front_default") val frontDefault: String?,
    @SerialName("front_shiny") val frontShiny: String?,
    @SerialName("back_default") val backDefault: String?,
    @SerialName("back_shiny") val backShiny: String?,
    @SerialName("other") val other: OtherSpritesDTO? = null
)

@Serializable
data class OtherSpritesDTO(
    @SerialName("official-artwork") val officialArtwork: OfficialArtworkDTO? = null
)

@Serializable
data class OfficialArtworkDTO(
    @SerialName("front_default") val frontDefault: String?,
    @SerialName("front_shiny") val frontShiny: String?
)

@Serializable
data class StatDTO(
    @SerialName("base_stat") val baseStat: Int,
    @SerialName("effort") val effort: Int,
    @SerialName("stat") val stat: StatInfoDTO
)

@Serializable
data class StatInfoDTO(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)

@Serializable
data class TypeSlotDTO(
    @SerialName("slot") val slot: Int,
    @SerialName("type") val type: TypeInfoDTO
)

@Serializable
data class TypeInfoDTO(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)

@Serializable
data class AbilitySlotDTO(
    @SerialName("is_hidden") val isHidden: Boolean,
    @SerialName("slot") val slot: Int,
    @SerialName("ability") val ability: AbilityInfoDTO
)

@Serializable
data class AbilityInfoDTO(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)

@Serializable
data class SpeciesDTO(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)

@Serializable
data class PokemonSpeciesDetailDTO(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("is_legendary") val isLegendary: Boolean,
    @SerialName("is_mythical") val isMythical: Boolean,
    @SerialName("capture_rate") val captureRate: Int,
    @SerialName("base_happiness") val baseHappiness: Int,
    @SerialName("growth_rate") val growthRate: GrowthRateDTO,
    @SerialName("habitat") val habitat: HabitatDTO?,
    @SerialName("flavor_text_entries") val flavorTextEntries: List<FlavorTextEntryDTO>,
    @SerialName("evolution_chain") val evolutionChain: EvolutionChainLinkDTO?,
    @SerialName("egg_groups") val eggGroups: List<EggGroupDTO>,
    @SerialName("gender_rate") val genderRate: Int,
    @SerialName("generation") val generation: GenerationDTO
)

@Serializable
data class GrowthRateDTO(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)

@Serializable
data class HabitatDTO(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)

@Serializable
data class FlavorTextEntryDTO(
    @SerialName("flavor_text") val flavorText: String,
    @SerialName("language") val language: LanguageDTO,
    @SerialName("version") val version: VersionDTO
)

@Serializable
data class LanguageDTO(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)

@Serializable
data class VersionDTO(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)

@Serializable
data class EvolutionChainLinkDTO(
    @SerialName("url") val url: String
) {
    val evolutionChainId: Int
        get() = url
            .trimEnd('/')
            .split('/')
            .last()
            .toInt()
}

@Serializable
data class EvolutionChainDTO(
    @SerialName("id") val id: Int,
    @SerialName("chain") val chain: ChainLinkDTO
)

@Serializable
data class ChainLinkDTO(
    @SerialName("species") val species: SpeciesDTO,
    @SerialName("evolves_to") val evolvesTo: List<ChainLinkDTO>,
    @SerialName("evolution_details") val evolutionDetails: List<EvolutionDetailDTO>
)

@Serializable
data class EvolutionDetailDTO(
    @SerialName("min_level") val minLevel: Int?,
    @SerialName("trigger") val trigger: TriggerDTO,
    @SerialName("item") val item: ItemDTO?,
    @SerialName("min_happiness") val minHappiness: Int?,
    @SerialName("min_beauty") val minBeauty: Int?,
    @SerialName("min_affection") val minAffection: Int?,
    @SerialName("needs_overworld_rain") val needsOverworldRain: Boolean,
    @SerialName("party_species") val partySpecies: SpeciesDTO?,
    @SerialName("party_type") val partyType: TypeInfoDTO?,
    @SerialName("relative_physical_stats") val relativePhysicalStats: Int?,
    @SerialName("time_of_day") val timeOfDay: String,
    @SerialName("trade_species") val tradeSpecies: SpeciesDTO?,
    @SerialName("turn_upside_down") val turnUpsideDown: Boolean
)

@Serializable
data class TriggerDTO(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)

@Serializable
data class ItemDTO(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)

@Serializable
data class EggGroupDTO(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)

@Serializable
data class GenerationDTO(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)
