package com.thesomeshkumar.pokepedia.di

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import com.thesomeshkumar.pokepedia.core.presentation.IOSUrlLauncher
import com.thesomeshkumar.pokepedia.core.presentation.UrlLauncher
import com.thesomeshkumar.pokepedia.pokemon.data.database.DatabaseFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { Darwin.create() }
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
                        .directory(getCacheDirectory().toPath().resolve("image_cache"))
                        .maxSizeBytes(512L * 1024 * 1024) // 512 MB disk cache
                        .build()
                }
                .crossfade(300) // Smooth crossfade animation
                .build()
        }
        single<UrlLauncher> { IOSUrlLauncher() }
    }

@OptIn(ExperimentalForeignApi::class)
private fun getCacheDirectory(): String {
    val paths = NSSearchPathForDirectoriesInDomains(
        NSCachesDirectory,
        NSUserDomainMask,
        true
    )
    return paths.first() as String
}