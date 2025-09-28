package com.thesomeshkumar.pokepedia.di

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.thesomeshkumar.pokepedia.core.presentation.JvmUrlLauncher
import com.thesomeshkumar.pokepedia.core.presentation.UrlLauncher
import com.thesomeshkumar.pokepedia.pokemon.data.database.DatabaseFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Created by SixFlags on 27 September, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
 */

actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { OkHttp.create() }
        single { DatabaseFactory() }

        single { DatabaseFactory() }

        single {
            ImageLoader
                .Builder(PlatformContext.INSTANCE)
                .components {
                    add(KtorNetworkFetcherFactory(httpClient = get<HttpClient>()))
                }
                .build()
        }
        single<UrlLauncher> { JvmUrlLauncher() }
    }