package com.thesomeshkumar.pokepedia.di

import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.thesomeshkumar.pokepedia.core.presentation.AndroidUrlLauncher
import com.thesomeshkumar.pokepedia.core.presentation.UrlLauncher
import com.thesomeshkumar.pokepedia.pokemon.data.database.DatabaseFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import okio.Path.Companion.toOkioPath
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

        single {
            ImageLoader
                .Builder(androidContext())
                .memoryCache {
                    MemoryCache.Builder()
                        .maxSizePercent(androidContext(), 0.25) // Use 25% of app memory
                        .build()
                }
                .diskCache {
                    DiskCache.Builder()
                        .directory(androidContext().cacheDir.resolve("image_cache").toOkioPath())
                        .maxSizeBytes(512L * 1024 * 1024) // 512 MB disk cache
                        .build()
                }
                .crossfade(300) // Smooth crossfade animation
                .build()
        }
        single<UrlLauncher> { AndroidUrlLauncher(androidContext()) }
    }