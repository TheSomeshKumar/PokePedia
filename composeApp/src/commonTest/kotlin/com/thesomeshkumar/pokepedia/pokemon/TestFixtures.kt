package com.thesomeshkumar.pokepedia.pokemon

import com.thesomeshkumar.pokepedia.core.domain.DataError
import com.thesomeshkumar.pokepedia.core.domain.Result
import com.thesomeshkumar.pokepedia.core.presentation.UiText
import com.thesomeshkumar.pokepedia.pokemon.domain.*

/**
 * Test fixtures and factory functions for creating test data.
 * Following Clean Architecture principles for testable code.
 */

object PokemonTestFixtures {

    fun createPokemon(
        id: Int = 1,
        name: String = "bulbasaur",
        height: Int = 7,
        weight: Int = 69,
        baseExperience: Int = 64,
        isLegendary: Boolean = false,
        isMythical: Boolean = false
    ) = Pokemon(
        id = id,
        name = name,
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        order = id,
        sprites = createPokemonSprites(id),
        stats = createPokemonStats(),
        types = createPokemonTypes(),
        abilities = createPokemonAbilities(),
        species = createPokemonSpecies(name, isLegendary, isMythical),
        description = "A strange seed was planted on its back at birth. The plant sprouts and grows with this POKéMON.",
        evolutionChain = createEvolutionChain()
    )

    fun createPokemonSprites(id: Int = 1) = PokemonSprites(
        frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png",
        frontShiny = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/$id.png",
        backDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/$id.png",
        backShiny = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/shiny/$id.png",
        officialArtwork = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
    )

    fun createPokemonStats() = listOf(
        PokemonStat(
            baseStat = 45,
            effort = 0,
            stat = PokemonStat.Stat(
                name = "hp",
                url = "https://pokeapi.co/api/v2/stat/1/"
            )
        ),
        PokemonStat(
            baseStat = 49,
            effort = 0,
            stat = PokemonStat.Stat(
                name = "attack",
                url = "https://pokeapi.co/api/v2/stat/2/"
            )
        ),
        PokemonStat(
            baseStat = 49,
            effort = 0,
            stat = PokemonStat.Stat(
                name = "defense",
                url = "https://pokeapi.co/api/v2/stat/3/"
            )
        ),
        PokemonStat(
            baseStat = 65,
            effort = 1,
            stat = PokemonStat.Stat(
                name = "special-attack",
                url = "https://pokeapi.co/api/v2/stat/4/"
            )
        ),
        PokemonStat(
            baseStat = 65,
            effort = 0,
            stat = PokemonStat.Stat(
                name = "special-defense",
                url = "https://pokeapi.co/api/v2/stat/5/"
            )
        ),
        PokemonStat(
            baseStat = 45,
            effort = 0,
            stat = PokemonStat.Stat(
                name = "speed",
                url = "https://pokeapi.co/api/v2/stat/6/"
            )
        )
    )

    fun createPokemonTypes() = listOf(
        PokemonType(
            slot = 1,
            type = PokemonType.Type(
                name = "grass",
                url = "https://pokeapi.co/api/v2/type/12/"
            )
        ),
        PokemonType(
            slot = 2,
            type = PokemonType.Type(
                name = "poison",
                url = "https://pokeapi.co/api/v2/type/4/"
            )
        )
    )

    fun createPokemonAbilities() = listOf(
        PokemonAbility(
            isHidden = false,
            slot = 1,
            ability = PokemonAbility.Ability(
                name = "overgrow",
                url = "https://pokeapi.co/api/v2/ability/65/"
            )
        ),
        PokemonAbility(
            isHidden = true,
            slot = 3,
            ability = PokemonAbility.Ability(
                name = "chlorophyll",
                url = "https://pokeapi.co/api/v2/ability/34/"
            )
        )
    )

    fun createPokemonSpecies(
        name: String = "bulbasaur",
        isLegendary: Boolean = false,
        isMythical: Boolean = false
    ) = PokemonSpecies(
        name = name,
        url = "https://pokeapi.co/api/v2/pokemon-species/1/",
        isLegendary = isLegendary,
        isMythical = isMythical,
        captureRate = 45,
        baseHappiness = 50,
        growthRate = "medium-slow",
        habitat = "grassland",
        eggGroups = listOf("Monster", "Grass"),
        genderRate = 1,
        generation = "generation-i"
    )

    fun createEvolutionChain() = listOf(
        EvolutionStage(
            pokemonId = 1,
            pokemonName = "bulbasaur",
            minLevel = null,
            trigger = "level-up",
            item = null
        ),
        EvolutionStage(
            pokemonId = 2,
            pokemonName = "ivysaur",
            minLevel = 16,
            trigger = "level-up",
            item = null
        ),
        EvolutionStage(
            pokemonId = 3,
            pokemonName = "venusaur",
            minLevel = 32,
            trigger = "level-up",
            item = null
        )
    )

    fun createPokemonList(count: Int = 5): List<Pokemon> {
        return (1..count).map { id ->
            createPokemon(
                id = id,
                name = "pokemon$id"
            )
        }
    }

    fun createPaginatedPokemon(
        count: Int = 20,
        offset: Int = 0,
        hasNextPage: Boolean = true
    ) = PaginatedPokemon(
        pokemon = createPokemonList(count),
        offset = offset,
        limit = count,
        hasNextPage = hasNextPage,
        totalCount = 1000
    )

    // Result helpers
    fun <T> successResult(data: T): Result<T, DataError.Remote> = Result.Success(data)

    fun <T> errorResult(error: DataError.Remote = DataError.Remote.UNKNOWN): Result<T, DataError.Remote> =
        Result.Error(error)

    // Create specific error results
    fun <T> noInternetError(): Result<T, DataError.Remote> = 
        Result.Error(DataError.Remote.NO_INTERNET)

    fun <T> serverError(): Result<T, DataError.Remote> = 
        Result.Error(DataError.Remote.SERVER)

    fun <T> timeoutError(): Result<T, DataError.Remote> = 
        Result.Error(DataError.Remote.REQUEST_TIMEOUT)
}

/**
 * Creates a test UiText.DynamicString for testing
 */
fun testUiText(message: String = "Test error"): UiText = UiText.DynamicString(message)



