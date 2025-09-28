package com.thesomeshkumar.pokepedia.core.presentation

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

/**
 * Created by SixFlags on 23 September, 2025.
 * Copyright ©2025 SixFlags. All rights reserved.
 */

class IOSUrlLauncher : UrlLauncher {

    override suspend fun openUrl(url: String): Boolean {
        return try {
            val nsUrl = NSURL.URLWithString(url)
            if (nsUrl != null && UIApplication.sharedApplication.canOpenURL(nsUrl)) {
                UIApplication.sharedApplication.openURL(nsUrl)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun openYouTubeVideo(videoKey: String): Boolean {
        return try {
            // Try to open in YouTube app first
            val youTubeAppUrl = "youtube://watch?v=$videoKey"
            val nsYouTubeUrl = NSURL.URLWithString(youTubeAppUrl)

            if (nsYouTubeUrl != null && UIApplication.sharedApplication.canOpenURL(nsYouTubeUrl)) {
                UIApplication.sharedApplication.openURL(nsYouTubeUrl)
                true
            } else {
                // Fallback to Safari
                openUrl("https://www.youtube.com/watch?v=$videoKey")
            }
        } catch (e: Exception) {
            // Fallback to Safari if YouTube app fails
            openUrl("https://www.youtube.com/watch?v=$videoKey")
        }
    }
}

actual fun getUrlLauncher(): UrlLauncher = IOSUrlLauncher()
