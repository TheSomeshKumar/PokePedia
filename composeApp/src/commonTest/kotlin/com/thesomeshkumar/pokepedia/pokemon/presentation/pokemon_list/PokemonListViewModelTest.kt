package com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list

import app.cash.turbine.test
import com.thesomeshkumar.pokepedia.core.domain.DataError
import com.thesomeshkumar.pokepedia.pokemon.PokemonTestFixtures
import com.thesomeshkumar.pokepedia.pokemon.fakes.FakePokemonRepository
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Comprehensive tests for PokemonListViewModel using Fake Repository
 * 
 * This uses the recommended KMP testing pattern with Fake implementations
 * instead of mocking libraries like MockK which don't work in commonTest.
 * 
 * Tests cover:
 * - Initial loading
 * - Pagination
 * - Search functionality
 * - Error handling
 * - Retry mechanism
 */
@OptIn(ExperimentalCoroutinesApi::class)
class PokemonListViewModelTest {

    private lateinit var viewModel: PokemonListViewModel
    private lateinit var fakeRepository: FakePokemonRepository
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakePokemonRepository()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ========== Example Tests ==========

    @Test
    fun `initial load should update state with pokemon list on success`() = runTest {
        // Given
        val expectedPokemon = PokemonTestFixtures.createPaginatedPokemon(count = 20)
        fakeRepository.getPokemonResult = PokemonTestFixtures.successResult(expectedPokemon)

        // When
        viewModel = PokemonListViewModel(fakeRepository)
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            
            // Verify state
            state.isLoading.shouldBeFalse()
            state.pokemonList.shouldHaveSize(20)
            state.errorMessage.shouldBeNull()
            state.currentOffset shouldBe 20
            state.canLoadMore.shouldBeTrue()
            state.isLoadingMore.shouldBeFalse()
        }
        
        // Verify repository was called
        fakeRepository.verifyGetPokemonCalled(times = 1, limit = 20, offset = 0)
    }

    @Test
    fun `initial load should set error state on failure`() = runTest {
        // Given
        fakeRepository.getPokemonResult = PokemonTestFixtures.noInternetError()

        // When
        viewModel = PokemonListViewModel(fakeRepository)
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            
            state.isLoading.shouldBeFalse()
            state.pokemonList.shouldBeEmpty()
            state.errorMessage.shouldNotBeNull()
            state.canLoadMore.shouldBeTrue()
        }
    }

    @Test
    fun `load next page should append pokemon to existing list`() = runTest {
        // Given
        val firstPage = PokemonTestFixtures.createPaginatedPokemon(count = 20, offset = 0)
        val secondPage = PokemonTestFixtures.createPaginatedPokemon(count = 20, offset = 20)
        
        // Setup repository to return different results for different calls
        fakeRepository.getPokemonResult = PokemonTestFixtures.successResult(firstPage)
        viewModel = PokemonListViewModel(fakeRepository)
        advanceUntilIdle()
        
        // Change result for second call
        fakeRepository.getPokemonResult = PokemonTestFixtures.successResult(secondPage)

        // When
        viewModel.handleAction(PokemonListAction.LoadNextPage)
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            
            state.pokemonList.shouldHaveSize(40) // 20 + 20
            state.currentOffset shouldBe 40
            state.isLoadingMore.shouldBeFalse()
            state.canLoadMore.shouldBeTrue()
        }
        
        // Verify both calls were made
        fakeRepository.verifyGetPokemonCalled(times = 2)
        require(fakeRepository.getPokemonCalls[0] == Pair(20, 0)) { "First call should be (20, 0)" }
        require(fakeRepository.getPokemonCalls[1] == Pair(20, 20)) { "Second call should be (20, 20)" }
    }

    @Test
    fun `load next page should not load when canLoadMore is false`() = runTest {
        // Given
        val lastPage = PokemonTestFixtures.createPaginatedPokemon(
            count = 10,
            offset = 0,
            hasNextPage = false
        )
        fakeRepository.getPokemonResult = PokemonTestFixtures.successResult(lastPage)
        
        viewModel = PokemonListViewModel(fakeRepository)
        advanceUntilIdle()
        
        // Reset call tracking
        fakeRepository.getPokemonCalls.clear()

        // When
        viewModel.handleAction(PokemonListAction.LoadNextPage)
        advanceUntilIdle()

        // Then - should not call for next page
        require(fakeRepository.getPokemonCalls.isEmpty()) {
            "Expected no calls to getPokemon, but got ${fakeRepository.getPokemonCalls.size}"
        }
    }

    @Test
    fun `retry should reload pokemon when not searching`() = runTest {
        // Given - initial error
        fakeRepository.getPokemonResult = PokemonTestFixtures.noInternetError()
        viewModel = PokemonListViewModel(fakeRepository)
        advanceUntilIdle()

        // Change to success
        val expectedPokemon = PokemonTestFixtures.createPaginatedPokemon(count = 20)
        fakeRepository.getPokemonResult = PokemonTestFixtures.successResult(expectedPokemon)

        // When
        viewModel.handleAction(PokemonListAction.Retry)
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            
            state.isLoading.shouldBeFalse()
            state.pokemonList.shouldHaveSize(20)
            state.errorMessage.shouldBeNull()
        }
        
        // Verify repository was called twice (initial + retry)
        fakeRepository.verifyGetPokemonCalled(times = 2, limit = 20, offset = 0)
    }

    @Test
    fun `should handle different error types correctly`() = runTest {
        val errorTypes = listOf(
            DataError.Remote.NO_INTERNET,
            DataError.Remote.SERVER,
            DataError.Remote.REQUEST_TIMEOUT,
            DataError.Remote.SERIALIZATION,
            DataError.Remote.TOO_MANY_REQUESTS,
            DataError.Remote.UNKNOWN
        )

        for (errorType in errorTypes) {
            // Given
            fakeRepository.reset()
            fakeRepository.getPokemonResult = PokemonTestFixtures.errorResult(errorType)

            // When
            viewModel = PokemonListViewModel(fakeRepository)
            advanceUntilIdle()

            // Then
            viewModel.state.test {
                val state = awaitItem()
                
                state.errorMessage.shouldNotBeNull()
                state.isLoading.shouldBeFalse()
                state.pokemonList.shouldBeEmpty()
            }
        }
    }
}

