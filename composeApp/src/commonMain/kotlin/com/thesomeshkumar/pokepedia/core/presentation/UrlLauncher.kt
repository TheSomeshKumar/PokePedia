package com.thesomeshkumar.pokepedia.core.presentation

/**
 * Created by SixFlags on 23 September, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
 */

interface UrlLauncher {
    suspend fun openUrl(url: String): Boolean
    suspend fun openYouTubeVideo(videoKey: String): Boolean
}

expect fun getUrlLauncher(): UrlLauncher
