package com.thesomeshkumar.pokepedia

import android.app.Application
import com.thesomeshkumar.pokepedia.di.initKoin
import org.koin.android.ext.koin.androidContext

/**
 * Created by Somesh Kumar on 27 July, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */
class PokePediaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin { androidContext(this@PokePediaApplication) }
    }
}