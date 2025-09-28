package com.thesomeshkumar.pokepedia.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Created by SixFlags on 25 July, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
 */

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            pokemonModule,
            platformModule
        )
    }
}