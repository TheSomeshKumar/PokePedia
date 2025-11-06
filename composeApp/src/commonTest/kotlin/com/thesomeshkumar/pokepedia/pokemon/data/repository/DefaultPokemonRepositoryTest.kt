package com.thesomeshkumar.pokepedia.pokemon.data.repository

import com.thesomeshkumar.pokepedia.core.domain.DataError
import com.thesomeshkumar.pokepedia.core.domain.Result
import com.thesomeshkumar.pokepedia.pokemon.PokemonTestFixtures
import com.thesomeshkumar.pokepedia.pokemon.data.DTOTestFixtures
import com.thesomeshkumar.pokepedia.pokemon.fakes.FakePokemonDao
import com.thesomeshkumar.pokepedia.pokemon.fakes.FakeRemoteDataSource
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Comprehensive tests for DefaultPokemonRepository
 * 
 * Tests the data layer integration between remote and local data sources.
 * Uses Fake implementations for testing.
 * 
 * Tests cover:
 * - Pokemon list retrieval with pagination
 * - Pokemon details retrieval (with species and evolution)
 * - Search functionality
 * - Error handling
 * - Data mapping
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DefaultPokemonRepositoryTest {

    private lateinit var repository: DefaultPokemonRepository
    private lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    private lateinit var fakePokemonDao: FakePokemonDao

    @BeforeTest
    fun setup() {
        fakeRemoteDataSource = FakeRemoteDataSource()
        fakePokemonDao = FakePokemonDao()
        repository = DefaultPokemonRepository(fakeRemoteDataSource, fakePokemonDao)
    }

    // ========== Get Pokemon List Tests ==========

    @Test
    fun `getPokemon should return paginated pokemon on success`() = runTest {
        // Given
        val mockResponse = DTOTestFixtures.createPokemonListResponse(count = 20)
        fakeRemoteDataSource.getPokemonListResult = Result.Success(mockResponse)

        // When
        val result = repository.getPokemon(limit = 20, offset = 0)

        // Then
        result.shouldBeInstanceOf<Result.Success<*>>()
        val paginatedPokemon = (result as Result.Success).data
        
        paginatedPokemon.pokemon.shouldHaveSize(20)
        paginatedPokemon.offset shouldBe 0
        paginatedPokemon.limit shouldBe 20
        paginatedPokemon.hasNextPage.shouldBeTrue()
        paginatedPokemon.totalCount shouldBe 1000
        
        require(fakeRemoteDataSource.getPokemonListCalls.size == 1) {
            "Expected 1 call but got ${fakeRemoteDataSource.getPokemonListCalls.size}"
        }
        require(fakeRemoteDataSource.getPokemonListCalls[0] == Pair(20, 0)) {
            "Expected (20, 0) but got ${fakeRemoteDataSource.getPokemonListCalls[0]}"
        }
    }

    @Test
    fun `getPokemon should handle different offsets correctly`() = runTest {
        // Given
        val mockResponse = DTOTestFixtures.createPokemonListResponse(count = 20)
        fakeRemoteDataSource.getPokemonListResult = Result.Success(mockResponse)

        // When
        val result = repository.getPokemon(limit = 20, offset = 40)

        // Then
        result.shouldBeInstanceOf<Result.Success<*>>()
        val paginatedPokemon = (result as Result.Success).data
        
        paginatedPokemon.offset shouldBe 40
        require(fakeRemoteDataSource.getPokemonListCalls[0] == Pair(20, 40))
    }

    @Test
    fun `getPokemon should return error when remote source fails`() = runTest {
        // Given
        fakeRemoteDataSource.getPokemonListResult = Result.Error(DataError.Remote.NO_INTERNET)

        // When
        val result = repository.getPokemon(limit = 20, offset = 0)

        // Then
        result.shouldBeInstanceOf<Result.Error<*>>()
        (result as Result.Error).error shouldBe DataError.Remote.NO_INTERNET
    }

    // ========== Get Pokemon Details Tests ==========

    @Test
    fun `getPokemonDetails by ID should return pokemon with species and evolution`() = runTest {
        // Given
        val pokemonDTO = DTOTestFixtures.createPokemonDetailDTO(id = 1, name = "bulbasaur")
        val speciesDTO = DTOTestFixtures.createPokemonSpeciesDetailDTO(id = 1, name = "bulbasaur")
        val evolutionDTO = DTOTestFixtures.createEvolutionChainDTO()
        
        fakeRemoteDataSource.getPokemonDetailsByIdResult = Result.Success(pokemonDTO)
        fakeRemoteDataSource.getPokemonSpeciesByIdResult = Result.Success(speciesDTO)
        fakeRemoteDataSource.getEvolutionChainResult = Result.Success(evolutionDTO)

        // When
        val result = repository.getPokemonDetails(1)

        // Then
        result.shouldBeInstanceOf<Result.Success<*>>()
        val pokemon = (result as Result.Success).data
        
        pokemon.id shouldBe 1
        pokemon.name shouldBe "bulbasaur"
        pokemon.species.shouldNotBeNull()
        pokemon.evolutionChain.shouldNotBeNull()
        
        // Verify calls were made
        require(fakeRemoteDataSource.getPokemonDetailsByIdCalls.contains(1))
        require(fakeRemoteDataSource.getPokemonSpeciesByIdCalls.contains(1))
        require(fakeRemoteDataSource.getEvolutionChainCalls.contains(1))
    }

    @Test
    fun `getPokemonDetails should return error when pokemon fetch fails`() = runTest {
        // Given
        fakeRemoteDataSource.getPokemonDetailsByIdResult = Result.Error(DataError.Remote.SERVER)
        fakeRemoteDataSource.getPokemonSpeciesByIdResult = Result.Error(DataError.Remote.SERVER)

        // When
        val result = repository.getPokemonDetails(999)

        // Then
        result.shouldBeInstanceOf<Result.Error<*>>()
        (result as Result.Error).error shouldBe DataError.Remote.SERVER
    }

    @Test
    fun `getPokemonDetails should handle species fetch failure gracefully`() = runTest {
        // Given
        val pokemonDTO = DTOTestFixtures.createPokemonDetailDTO(id = 1)
        fakeRemoteDataSource.getPokemonDetailsByIdResult = Result.Success(pokemonDTO)
        fakeRemoteDataSource.getPokemonSpeciesByIdResult = Result.Error(DataError.Remote.SERVER)

        // When
        val result = repository.getPokemonDetails(1)

        // Then
        result.shouldBeInstanceOf<Result.Success<*>>()
        val pokemon = (result as Result.Success).data
        
        pokemon.id shouldBe 1
        pokemon.species.shouldBeNull() // Species should be null when fetch fails
    }

    @Test
    fun `getPokemonDetails should handle evolution chain failure gracefully`() = runTest {
        // Given
        val pokemonDTO = DTOTestFixtures.createPokemonDetailDTO(id = 1)
        val speciesDTO = DTOTestFixtures.createPokemonSpeciesDetailDTO(id = 1)
        
        fakeRemoteDataSource.getPokemonDetailsByIdResult = Result.Success(pokemonDTO)
        fakeRemoteDataSource.getPokemonSpeciesByIdResult = Result.Success(speciesDTO)
        fakeRemoteDataSource.getEvolutionChainResult = Result.Error(DataError.Remote.SERVER)

        // When
        val result = repository.getPokemonDetails(1)

        // Then
        result.shouldBeInstanceOf<Result.Success<*>>()
        val pokemon = (result as Result.Success).data
        
        pokemon.evolutionChain.shouldBeEmpty()
    }

    // ========== Get Pokemon Details by Name Tests ==========

    @Test
    fun `getPokemonDetails by name should return pokemon`() = runTest {
        // Given
        val pokemonDTO = DTOTestFixtures.createPokemonDetailDTO(id = 25, name = "pikachu")
        val speciesDTO = DTOTestFixtures.createPokemonSpeciesDetailDTO(id = 25, name = "pikachu")
        val evolutionDTO = DTOTestFixtures.createEvolutionChainDTO()
        
        fakeRemoteDataSource.getPokemonDetailsByNameResult = Result.Success(pokemonDTO)
        fakeRemoteDataSource.getPokemonSpeciesByNameResult = Result.Success(speciesDTO)
        fakeRemoteDataSource.getEvolutionChainResult = Result.Success(evolutionDTO)

        // When
        val result = repository.getPokemonDetails("pikachu")

        // Then
        result.shouldBeInstanceOf<Result.Success<*>>()
        val pokemon = (result as Result.Success).data
        
        pokemon.name shouldBe "pikachu"
        require(fakeRemoteDataSource.getPokemonDetailsByNameCalls.contains("pikachu"))
        require(fakeRemoteDataSource.getPokemonSpeciesByNameCalls.contains("pikachu"))
    }

    // ========== Search Pokemon Tests ==========

    @Test
    fun `searchPokemon should return results from local database`() = runTest {
        // Given - populate fake database
        val entity1 = createMockPokemonEntity(id = 1, name = "bulbasaur")
        val entity2 = createMockPokemonEntity(id = 2, name = "ivysaur")
        fakePokemonDao.insertPokemonList(listOf(entity1, entity2))

        // When
        val result = repository.searchPokemon("saur")

        // Then
        result.shouldBeInstanceOf<Result.Success<*>>()
        val pokemon = (result as Result.Success).data
        
        pokemon.shouldHaveSize(2)
        require(fakePokemonDao.searchPokemonCalls.contains("saur"))
    }

    @Test
    fun `searchPokemon should return empty list when no results found`() = runTest {
        // When
        val result = repository.searchPokemon("xyz")

        // Then
        result.shouldBeInstanceOf<Result.Success<*>>()
        val pokemon = (result as Result.Success).data
        pokemon.shouldBeEmpty()
    }

    @Test
    fun `searchPokemon should convert query to lowercase`() = runTest {
        // Given
        val entity = createMockPokemonEntity(id = 25, name = "pikachu")
        fakePokemonDao.insertPokemon(entity)

        // When
        val result = repository.searchPokemon("PIKA")

        // Then
        result.shouldBeInstanceOf<Result.Success<*>>()
        
        // Verify lowercase conversion
        require(fakePokemonDao.searchPokemonCalls.contains("pika")) {
            "Expected 'pika' but got ${fakePokemonDao.searchPokemonCalls}"
        }
    }

    // ========== Helper Methods ==========

    private fun createMockPokemonEntity(id: Int, name: String) = 
        com.thesomeshkumar.pokepedia.pokemon.data.database.PokemonEntity(
            id = id,
            name = name,
            height = 7,
            weight = 69,
            baseExperience = 64,
            order = id,
            frontSprite = "front.png",
            frontShinySprite = "front_shiny.png",
            backSprite = "back.png",
            backShinySprite = "back_shiny.png",
            officialArtwork = "official.png",
            stats = "[]",
            types = "[]",
            abilities = "[]",
            isLegendary = false,
            isMythical = false,
            description = "Test description",
            captureRate = 45,
            baseHappiness = 50,
            growthRate = "medium-slow",
            habitat = "grassland"
        )
}



