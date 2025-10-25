package com.thesomeshkumar.pokepedia.di

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import com.thesomeshkumar.pokepedia.core.presentation.JvmUrlLauncher
import com.thesomeshkumar.pokepedia.core.presentation.UrlLauncher
import com.thesomeshkumar.pokepedia.pokemon.data.database.DatabaseFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import okio.Path.Companion.toOkioPath
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

/**
 * Created by Somesh Kumar on 27 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */

actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { OkHttp.create() }
        single { DatabaseFactory() }

        single {
            ImageLoader
                .Builder(PlatformContext.INSTANCE)
                .components {
                    add(KtorNetworkFetcherFactory(httpClient = get<HttpClient>()))
                }
                .memoryCache {
                    MemoryCache.Builder()
                        .maxSizePercent(PlatformContext.INSTANCE, 0.25) // Use 25% of app memory
                        .build()
                }
                .diskCache {
                    DiskCache.Builder()
                        .directory(getCacheDirectory().resolve("image_cache").toOkioPath())
                        .maxSizeBytes(512L * 1024 * 1024) // 512 MB disk cache
                        .build()
                }
                .crossfade(300) // Smooth crossfade animation
                .build()
        }
        single<UrlLauncher> { JvmUrlLauncher() }
    }

private fun getCacheDirectory(): File {
    val userHome = System.getProperty("user.home")
    val osName = System.getProperty("os.name").lowercase()

    val cacheDir = when {
        osName.contains("mac") -> File(userHome, "Library/Caches/PokePedia")
        osName.contains("win") -> File(System.getenv("LOCALAPPDATA") ?: "$userHome\\AppData\\Local", "PokePedia\\Cache")
        else -> File(userHome, ".cache/PokePedia") // Linux and others
    }

    cacheDir.mkdirs()
    return cacheDir
}