package com.thesomeshkumar.pokepedia.core.presentation

/**
 * Created by Somesh Kumar on 23 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */

interface UrlLauncher {
    suspend fun openUrl(url: String): Boolean
    suspend fun openYouTubeVideo(videoKey: String): Boolean
}

expect fun getUrlLauncher(): UrlLauncher
