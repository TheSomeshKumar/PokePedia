package com.thesomeshkumar.pokepedia.pokemon.data.mappers

import com.thesomeshkumar.pokepedia.pokemon.data.DTOTestFixtures
import com.thesomeshkumar.pokepedia.pokemon.data.dto.PokemonBasicDTO
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlin.test.Test

/**
 * Tests for Pokemon mapper functions
 * 
 * Mappers are pure functions that convert between DTOs and Domain models.
 * These are some of the easiest tests to write and maintain.
 * 
 * Tests cover:
 * - DTO to Domain model conversion
 * - Field mapping correctness
 * - Null handling
 * - Data transformation logic
 */
class PokemonMappersTest {

    // ========== PokemonDetailDTO.toDomain() Tests ==========

    @Test
    fun `PokemonDetailDTO toDomain should map all fields correctly`() {
        // Given
        val dto = DTOTestFixtures.createPokemonDetailDTO(id = 25, name = "pikachu")

        // When
        val domain = dto.toDomain()

        // Then
        domain.id shouldBe 25
        domain.name shouldBe "pikachu"
        domain.height shouldBe 7
        domain.weight shouldBe 69
        domain.baseExperience shouldBe 64
        domain.sprites.shouldNotBeNull()
        domain.stats.shouldNotBeNull()
        domain.types.shouldNotBeNull()
        domain.abilities.shouldNotBeNull()
    }

    @Test
    fun `PokemonDetailDTO toDomain should handle null baseExperience`() {
        // Given
        val dto = DTOTestFixtures.createPokemonDetailDTO().copy(baseExperience = null)

        // When
        val domain = dto.toDomain()

        // Then
        domain.baseExperience shouldBe 0 // Should default to 0
    }

    @Test
    fun `PokemonDetailDTO toDomain with species should include species data`() {
        // Given
        val pokemonDTO = DTOTestFixtures.createPokemonDetailDTO()
        val speciesDTO = DTOTestFixtures.createPokemonSpeciesDetailDTO()

        // When
        val domain = pokemonDTO.toDomain(speciesDetail = speciesDTO)

        // Then
        domain.species.shouldNotBeNull()
        domain.species?.name shouldBe "bulbasaur"
        domain.species?.isLegendary shouldBe false
        domain.species?.isMythical shouldBe false
    }

    @Test
    fun `PokemonDetailDTO toDomain with evolution chain should include evolution data`() {
        // Given
        val pokemonDTO = DTOTestFixtures.createPokemonDetailDTO()
        val evolutionDTO = DTOTestFixtures.createEvolutionChainDTO()

        // When
        val domain = pokemonDTO.toDomain(evolutionChain = evolutionDTO)

        // Then
        domain.evolutionChain.shouldNotBeNull()
        domain.evolutionChain.shouldHaveSize(2) // bulbasaur -> ivysaur
    }

    // ========== SpritesDTO.toDomain() Tests ==========

    @Test
    fun `SpritesDTO toDomain should map sprite URLs correctly`() {
        // Given
        val dto = DTOTestFixtures.createPokemonDetailDTO().sprites

        // When
        val domain = dto.toDomain()

        // Then
        domain.frontDefault.shouldNotBeNull()
        domain.frontShiny.shouldNotBeNull()
        domain.backDefault.shouldNotBeNull()
        domain.backShiny.shouldNotBeNull()
        domain.officialArtwork.shouldNotBeNull()
    }

    @Test
    fun `Sprites primaryImage should prefer official artwork`() {
        // Given
        val dto = DTOTestFixtures.createPokemonDetailDTO().sprites

        // When
        val domain = dto.toDomain()

        // Then
        domain.primaryImage shouldBe domain.officialArtwork
    }

    // ========== StatDTO.toDomain() Tests ==========

    @Test
    fun `StatDTO toDomain should map stat data correctly`() {
        // Given
        val dto = DTOTestFixtures.createPokemonDetailDTO().stats[0]

        // When
        val domain = dto.toDomain()

        // Then
        domain.baseStat shouldBe 45
        domain.effort shouldBe 0
        domain.stat.name shouldBe "hp"
    }

    @Test
    fun `Stat displayName should format stat names correctly`() {
        // Given
        val stats = DTOTestFixtures.createPokemonDetailDTO().stats

        // When
        val domain = stats.map { it.toDomain() }

        // Then
        domain[0].stat.displayName shouldBe "HP"
        domain[1].stat.displayName shouldBe "Attack"
    }

    // ========== TypeSlotDTO.toDomain() Tests ==========

    @Test
    fun `TypeSlotDTO toDomain should map type data correctly`() {
        // Given
        val dto = DTOTestFixtures.createPokemonDetailDTO().types[0]

        // When
        val domain = dto.toDomain()

        // Then
        domain.slot shouldBe 1
        domain.type.name shouldBe "grass"
        domain.type.displayName shouldBe "Grass"
    }

    @Test
    fun `Type colorHex should return correct color for grass type`() {
        // Given
        val dto = DTOTestFixtures.createPokemonDetailDTO().types[0]

        // When
        val domain = dto.toDomain()

        // Then
        domain.type.colorHex shouldBe "#78C850" // Grass color
    }

    // ========== AbilitySlotDTO.toDomain() Tests ==========

    @Test
    fun `AbilitySlotDTO toDomain should map ability data correctly`() {
        // Given
        val dto = DTOTestFixtures.createPokemonDetailDTO().abilities[0]

        // When
        val domain = dto.toDomain()

        // Then
        domain.isHidden shouldBe false
        domain.slot shouldBe 1
        domain.ability.name shouldBe "overgrow"
        domain.ability.displayName shouldBe "Overgrow"
    }

    // ========== PokemonSpeciesDetailDTO.toDomain() Tests ==========

    @Test
    fun `PokemonSpeciesDetailDTO toDomain should map species data correctly`() {
        // Given
        val dto = DTOTestFixtures.createPokemonSpeciesDetailDTO()

        // When
        val domain = dto.toDomain()

        // Then
        domain.name shouldBe "bulbasaur"
        domain.isLegendary shouldBe false
        domain.isMythical shouldBe false
        domain.captureRate shouldBe 45
        domain.baseHappiness shouldBe 50
    }

    @Test
    fun `PokemonSpecies formattedGrowthRate should format correctly`() {
        // Given
        val dto = DTOTestFixtures.createPokemonSpeciesDetailDTO()

        // When
        val domain = dto.toDomain()

        // Then
        domain.formattedGrowthRate shouldBe "Medium Slow"
    }

    @Test
    fun `PokemonSpecies formattedGeneration should format correctly`() {
        // Given
        val dto = DTOTestFixtures.createPokemonSpeciesDetailDTO()

        // When
        val domain = dto.toDomain()

        // Then
        domain.formattedGeneration shouldBe "GEN I"
    }

    @Test
    fun `PokemonSpecies genderRatio should calculate correctly`() {
        // Given - genderRate of 1 means 12.5% female (1/8)
        val dto = DTOTestFixtures.createPokemonSpeciesDetailDTO()

        // When
        val domain = dto.toDomain()

        // Then - 12.5% female = 12%, 87.5% male = 87%
        // Formula: femalePercent = (genderRate / 8.0 * 100).toInt() = 12
        //          malePercent = 100 - femalePercent = 88
        domain.genderRatio shouldBe "88% ♂  12% ♀"
    }

    // ========== PokemonBasicDTO.toDomain() Tests ==========

    @Test
    fun `PokemonBasicDTO toDomain should map basic data correctly`() {
        // Given
        val dto = PokemonBasicDTO(
            name = "bulbasaur",
            url = "https://pokeapi.co/api/v2/pokemon/1/"
        )

        // When
        val domain = dto.toDomain()

        // Then
        domain.id shouldBe 1
        domain.name shouldBe "bulbasaur"
        domain.sprites.officialArtwork.shouldNotBeNull()
    }

    @Test
    fun `PokemonBasicDTO should extract ID from URL correctly`() {
        // Given
        val dto = PokemonBasicDTO(
            name = "pikachu",
            url = "https://pokeapi.co/api/v2/pokemon/25/"
        )

        // Then
        dto.id shouldBe 25
    }

    @Test
    fun `PokemonBasicDTO should generate correct image URL`() {
        // Given
        val dto = PokemonBasicDTO(
            name = "charizard",
            url = "https://pokeapi.co/api/v2/pokemon/6/"
        )

        // Then
        dto.getImageUrl() shouldBe "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/6.png"
    }

    // ========== EvolutionChainDTO.toEvolutionStages() Tests ==========

    @Test
    fun `EvolutionChainDTO toEvolutionStages should map evolution chain correctly`() {
        // Given
        val dto = DTOTestFixtures.createEvolutionChainDTO()

        // When
        val stages = dto.toEvolutionStages()

        // Then
        stages.shouldHaveSize(2)
        stages[0].pokemonName shouldBe "bulbasaur"
        stages[1].pokemonName shouldBe "ivysaur"
        stages[1].minLevel shouldBe 16
    }

    @Test
    fun `EvolutionStage should extract pokemon ID from species URL`() {
        // Given
        val dto = DTOTestFixtures.createEvolutionChainDTO()

        // When
        val stages = dto.toEvolutionStages()

        // Then
        stages[0].pokemonId shouldBe 1
        stages[1].pokemonId shouldBe 2
    }

    @Test
    fun `EvolutionStage evolutionMethod should format level correctly`() {
        // Given
        val dto = DTOTestFixtures.createEvolutionChainDTO()

        // When
        val stages = dto.toEvolutionStages()

        // Then
        stages[1].evolutionMethod shouldBe "Level 16"
    }

    // ========== getEnglishDescription Tests ==========

    @Test
    fun `getEnglishDescription should extract English flavor text`() {
        // Given
        val dto = DTOTestFixtures.createPokemonSpeciesDetailDTO()

        // When
        val description = dto.getEnglishDescription()

        // Then
        description shouldBe "A strange seed was planted on its back at birth."
    }

    @Test
    fun `getEnglishDescription should clean up whitespace and line breaks`() {
        // Given
        val dto = DTOTestFixtures.createPokemonSpeciesDetailDTO()

        // When
        val description = dto.getEnglishDescription()

        // Then
        description.contains("\n") shouldBe false
        description.contains("  ") shouldBe false // No double spaces
    }
}

