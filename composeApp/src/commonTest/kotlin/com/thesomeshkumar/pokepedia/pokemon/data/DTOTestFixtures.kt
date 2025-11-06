package com.thesomeshkumar.pokepedia.pokemon.data

import com.thesomeshkumar.pokepedia.pokemon.data.dto.*

/**
 * Test fixtures for DTO objects
 */
object DTOTestFixtures {
    
    fun createPokemonListResponse(count: Int = 20): PokemonListResponse {
        val results = (1..count).map { id ->
            PokemonBasicDTO(
                name = "pokemon$id",
                url = "https://pokeapi.co/api/v2/pokemon/$id/"
            )
        }
        
        return PokemonListResponse(
            count = 1000,
            next = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
            previous = null,
            results = results
        )
    }
    
    fun createPokemonDetailDTO(
        id: Int = 1,
        name: String = "bulbasaur"
    ): PokemonDetailDTO {
        return PokemonDetailDTO(
            id = id,
            name = name,
            height = 7,
            weight = 69,
            baseExperience = 64,
            order = id,
            sprites = SpritesDTO(
                frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png",
                frontShiny = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/$id.png",
                backDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/$id.png",
                backShiny = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/shiny/$id.png",
                other = OtherSpritesDTO(
                    officialArtwork = OfficialArtworkDTO(
                        frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png",
                        frontShiny = null
                    )
                )
            ),
            stats = listOf(
                StatDTO(baseStat = 45, effort = 0, stat = StatInfoDTO("hp", "url")),
                StatDTO(baseStat = 49, effort = 0, stat = StatInfoDTO("attack", "url"))
            ),
            types = listOf(
                TypeSlotDTO(slot = 1, type = TypeInfoDTO("grass", "url"))
            ),
            abilities = listOf(
                AbilitySlotDTO(isHidden = false, slot = 1, ability = AbilityInfoDTO("overgrow", "url"))
            ),
            species = SpeciesDTO(name = name, url = "https://pokeapi.co/api/v2/pokemon-species/$id/")
        )
    }
    
    fun createPokemonSpeciesDetailDTO(
        id: Int = 1,
        name: String = "bulbasaur"
    ): PokemonSpeciesDetailDTO {
        return PokemonSpeciesDetailDTO(
            id = id,
            name = name,
            isLegendary = false,
            isMythical = false,
            captureRate = 45,
            baseHappiness = 50,
            growthRate = GrowthRateDTO("medium-slow", "url"),
            habitat = HabitatDTO("grassland", "url"),
            flavorTextEntries = listOf(
                FlavorTextEntryDTO(
                    flavorText = "A strange seed was planted on its back at birth.",
                    language = LanguageDTO("en", "url"),
                    version = VersionDTO("red", "url")
                )
            ),
            evolutionChain = EvolutionChainLinkDTO("https://pokeapi.co/api/v2/evolution-chain/1/"),
            eggGroups = listOf(EggGroupDTO("monster", "url")),
            genderRate = 1,
            generation = GenerationDTO("generation-i", "url")
        )
    }
    
    fun createEvolutionChainDTO(): EvolutionChainDTO {
        return EvolutionChainDTO(
            id = 1,
            chain = ChainLinkDTO(
                species = SpeciesDTO("bulbasaur", "https://pokeapi.co/api/v2/pokemon-species/1/"),
                evolvesTo = listOf(
                    ChainLinkDTO(
                        species = SpeciesDTO("ivysaur", "https://pokeapi.co/api/v2/pokemon-species/2/"),
                        evolvesTo = emptyList(),
                        evolutionDetails = listOf(
                            EvolutionDetailDTO(
                                minLevel = 16,
                                trigger = TriggerDTO("level-up", "url"),
                                item = null,
                                minHappiness = null,
                                minBeauty = null,
                                minAffection = null,
                                needsOverworldRain = false,
                                partySpecies = null,
                                partyType = null,
                                relativePhysicalStats = null,
                                timeOfDay = "",
                                tradeSpecies = null,
                                turnUpsideDown = false
                            )
                        )
                    )
                ),
                evolutionDetails = emptyList()
            )
        )
    }
}



