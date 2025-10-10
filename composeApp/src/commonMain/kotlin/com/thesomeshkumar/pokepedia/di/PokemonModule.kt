package com.thesomeshkumar.pokepedia.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.thesomeshkumar.pokepedia.core.data.HttpClientFactory
import com.thesomeshkumar.pokepedia.pokemon.data.database.DatabaseFactory
import com.thesomeshkumar.pokepedia.pokemon.data.database.PokemonDatabase
import com.thesomeshkumar.pokepedia.pokemon.data.network.PokemonRemoteDataSource
import com.thesomeshkumar.pokepedia.pokemon.data.network.RemoteDataSource
import com.thesomeshkumar.pokepedia.pokemon.data.repository.DefaultPokemonRepository
import com.thesomeshkumar.pokepedia.pokemon.domain.PokemonRepository
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_detail.PokemonDetailViewModel
import com.thesomeshkumar.pokepedia.pokemon.presentation.pokemon_list.PokemonListViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Created by Somesh Kumar on 27 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */
expect val platformModule: Module

val pokemonModule = module {
    single { HttpClientFactory.create(get()) }
    singleOf(::PokemonRemoteDataSource).bind<RemoteDataSource>()
    singleOf(::DefaultPokemonRepository).bind<PokemonRepository>()
    single {
        get<DatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<PokemonDatabase>().pokemonDao }
    viewModelOf(::PokemonListViewModel)
    viewModelOf(::PokemonDetailViewModel)
}
