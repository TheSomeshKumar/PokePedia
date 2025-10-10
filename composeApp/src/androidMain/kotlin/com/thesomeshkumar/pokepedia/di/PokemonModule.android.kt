package com.thesomeshkumar.pokepedia.di

import coil3.ImageLoader
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.thesomeshkumar.pokepedia.core.presentation.AndroidUrlLauncher
import com.thesomeshkumar.pokepedia.core.presentation.UrlLauncher
import com.thesomeshkumar.pokepedia.pokemon.data.database.DatabaseFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Created by Somesh Kumar on 27 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */
actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { OkHttp.create() }
        single { DatabaseFactory(androidApplication()) }

        single { DatabaseFactory(androidApplication()) }

        single {
            ImageLoader
                .Builder(androidContext())
                .components {
                    add(KtorNetworkFetcherFactory(httpClient = get<HttpClient>()))
                }
                .build()
        }
        single<UrlLauncher> { AndroidUrlLauncher(androidContext()) }
    }