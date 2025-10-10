package com.thesomeshkumar.pokepedia.core.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Created by Somesh Kumar on 23 September, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */

class AndroidUrlLauncher(private val context: Context) : UrlLauncher {

    override suspend fun openUrl(url: String): Boolean {
        return try {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            ).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun openYouTubeVideo(videoKey: String): Boolean {
        return try {
            // Try to open in YouTube app first
            val youTubeAppIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("vnd.youtube:$videoKey")
            ).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            // Check if YouTube app is available
            if (youTubeAppIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(youTubeAppIntent)
                true
            } else {
                // Fallback to browser
                openUrl("https://www.youtube.com/watch?v=$videoKey")
            }
        } catch (e: Exception) {
            // Fallback to browser if YouTube app fails
            openUrl("https://www.youtube.com/watch?v=$videoKey")
        }
    }
}

actual fun getUrlLauncher(): UrlLauncher {
    // This will be injected via DI in practice
    throw IllegalStateException("UrlLauncher should be injected via DI")
}
