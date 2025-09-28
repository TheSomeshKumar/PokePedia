package com.thesomeshkumar.pokepedia

import android.app.Application
import com.thesomeshkumar.pokepedia.di.initKoin
import org.koin.android.ext.koin.androidContext

/**
 * Created by SixFlags on 27 July, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
 */
class PokePediaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin { androidContext(this@PokePediaApplication) }
    }
}